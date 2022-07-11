package platformer;

import java.util.Random;

import org.jsfml.graphics.*;
import org.jsfml.system.*;

import devtools.*;
import controllers.*;
import hero.Hero;
import models.Critter;

/**
 * A Tile is a chunk of platform on which the hero can jump, and on which other critters can be placed
 */
public class Tile extends Critter implements PlatformChunk {
    // public static final int TILE_WIDTH = 50;
    // public static final int TILE_HEIGHT = 50;

    public static final Vector2f TILE_SIZE = new Vector2f(50,50);

    protected Platformer platformer;
    protected Game controller;

    public Tile() {
        super();  
    }

    public Tile(Game controller) {
        this(controller.scene);
        this.controller = controller;
    }

    public Tile(Platformer platformer) {
        this();
        this.setLayer(1);
        this.platformer = platformer;
        ((GameScene)this.platformer).setTileTexture(this);
        this.setSizeFrom(this.platformer);

        // Sets the appropriate 
        // this.setPlatformLevel(new Random().nextInt(platformer.getNumberFloors()));
    }

    @Override
    public FloatRect getBounds() {
        return new FloatRect(this.getPosition(), this.getSize());
    }

    @Override
    public CritterType getCritterType() {
        return CritterType.TILE;
    }

    /**
     * Determines whether a critter is on the given tile
     * 
     * @return true only if the critter is currently on top of the tile
     */
    @Override
    public boolean checkCritter(Critter critter) {
        return false;
    }

    @Override
    public int getTileCountRequirements() {
        return 0;
    }


    
    public boolean holdsHero(Game game, Hero hero) {
        final IntRect rect = this.getPixelBounds(game);
        final IntRect player = hero.getPixelBounds(game);
        // boolean exactHeight = rect.top == player.top + player.height;
        // boolean falling = hero.isFallingDown() && rect.intersection(player)!=null;
        final int feet = player.top + player.height;
        IntRect intersection = player.intersection(rect);
        // boolean collides = intersection!=null;

        // When the player is going down
        // their feet may sometimes be "in" the tile > put them back on the tile
        // other times (when it's the bottom) we have to assume that it's going down and put it back on the tile
        boolean in = this.platformLevel==0 ? rect.intersection(player)!=null : rect.contains(player.left, feet) && rect.top!=feet;
        // boolean falling = hero.isFallingDown() && in;
        // boolean falling = hero.isFallingDown() && this.collides(hero, game);
        boolean falling = in;
        if (falling) {
            Vector2f position = new Vector2f(hero.getPosition().x, platformer.getFloorPosition(this.platformLevel)- player.height);
            // System.out.println("Hero (feet: " + (player.height + player.top)+") falling (intersection: " + intersection + "), will land at " + position);
            // System.out.println("Hero has feet " + (player.height + player.top) +" in the tile("+rect.top+"): " + rect.contains(player.left, player.top+player.height));
            hero.setPosition(hero.getPosition().x, platformer.getFloorPosition(this.platformLevel)- player.height);
        }
        final boolean correctHeight = (rect.top == player.top + player.height) || falling;

        boolean farLeft = rect.left > player.left + player.width;
        boolean farRight = player.left > rect.left + rect.width;
        if (correctHeight && !(this instanceof Floor)) {
            // System.out.println("Tile " + this + " vs. Hero (" + player +") - tooLeft=" + farLeft + ", tooRight=" + farRight);
        }
        
        // final boolean nextTo = rect.left > player.left + player.width                       // player to the right of the tile
        //     || (rect.left > player.width + player.left && player.left > 0);                  // player to the left
        final boolean besides = farLeft || farRight;
        // return !nextTo && correctHeight;
        return !besides && correctHeight;
    }

    public boolean testTick(Game game, Hero hero) {
        final IntRect rect = this.getPixelBounds(game);
        final IntRect player = hero.getPixelBounds(game);
        // boolean exactHeight = rect.top == player.top + player.height;
        // boolean falling = hero.isFallingDown() && rect.intersection(player)!=null;
        final int feet = player.top + player.height;
        IntRect intersection = player.intersection(rect);
        // boolean collides = intersection!=null;

        // When the player is going down
        // their feet may sometimes be "in" the tile > put them back on the tile
        // other times (when it's the bottom) we have to assume that it's going down and put it back on the tile
        boolean in = this.platformLevel==0 ? rect.intersection(player)!=null : rect.contains(player.left, feet) && rect.top!=feet;
        // boolean falling = hero.isFallingDown() && in;
        // boolean falling = hero.isFallingDown() && this.collides(hero, game);
        boolean falling = in;
        if (falling) {
            Vector2f position = new Vector2f(hero.getPosition().x, platformer.getFloorPosition(this.platformLevel)- player.height);
            // System.out.println("Hero (feet: " + (player.height + player.top)+") falling (intersection: " + intersection + "), will land at " + position);
            // System.out.println("Hero has feet " + (player.height + player.top) +" in the tile("+rect.top+"): " + rect.contains(player.left, player.top+player.height));
            hero.setPosition(hero.getPosition().x, platformer.getFloorPosition(this.platformLevel)- player.height);
        }
        final boolean correctHeight = (rect.top == player.top + player.height) || falling;

        boolean farLeft = rect.left > player.left + player.width;
        boolean farRight = player.left > rect.left + rect.width;
        if (correctHeight && !(this instanceof Floor)) {
            System.out.println("Tile (" +rect  + ") vs. Hero (" + player +") - tooLeft=" + farLeft + ", tooRight=" + farRight);

        }
        
        final boolean nextTo = rect.left > player.left + player.width                       // player to the right of the tile
            || (rect.left > player.width + player.left && player.left > 0);                  // player to the left
        final boolean besides = farLeft || farRight;
        return !besides && correctHeight;
    }

    @Override
    public void placeInScene(AbstractScene scene, Vector2f platformPosition) {
        this.setPosition(platformPosition);
    }

    @Override
    public void setSizeFrom(Platformer platformer) {
        this.setSize(new Vector2f(platformer.getPlatformTileSize()));
        // this.tileSize = this.getSize();
    }

    @Override
    public void place(Platformer scene, Vector2f availablePosition) {
        this.setPosition(availablePosition);
    }

    @Override
    public void tick(float time, Game controller) {
        super.tick(time, controller);
        if (!this.isVisibleYet) return;
        this.tileTick(time, controller);
    }

    protected void tileTick(float time, Game controller) {
        // System.out.println("Tile " + this + " rendered [" + this.isBeingRendered +"]");
        boolean lands = this.holdsHero(controller, controller.hero);
        // boolean lands = this.checkCritter(controller.hero);
        if (lands) {
            // Tell the manager, and the hero will fetch it
            controller.critterManager.receiveUpdate(new ObservableEvent(this, GameManager.EVENT_HERO_LANDING));
        }
    }

    public static class Floor extends Tile implements Scrollable
    {
        protected ScrollableSprite[] sprites;
        private float lastSpot = 0;
        
        private Vector2f size;
        private Texture texture;
    
        private boolean init = false;
        public Floor(Platformer platformer, int platform) {
            super();
            this.platformer = platformer;
            this.platformLevel = platform;
            this.setSizeFrom(platformer);
            this.setLayer(1);
            this.init = true;
            // System.out.println(" bounds " + getBounds());
        }
    
        @Override
        public void setPlatformLevel(int platform) {
            if (!this.init) {
                this.platformLevel = platform;
            }
        }
    
        @Override
        public void setTexture(Texture texture, boolean keepSize) {
            this.texture = texture;
            if (this.sprites!=null) {
                for (ScrollableSprite s : this.sprites) {
                    s.setTexture(this.texture);
                }
            }
        }
    
        @Override
        public void setPosition(Vector2f position) {
            float positionX = ((DefaultScene)platformer).getPosition().x;
            float chunkWidth = this.platformer.getPlatformTileSize().x;
            for (int i = 0; i < sprites.length; i++ ) {
                sprites[i].setPosition(positionX + i*chunkWidth, platformer.getFloorPosition(this.platformLevel));
            }
        }
    
        @Override
        public Vector2f getPosition() {
            return (this.sprites==null) ? Vector2f.ZERO : this.sprites[0].getPosition();
        }
    
        @Override
        public FloatRect getBounds() {
            // DefaultScene scene = (DefaultScene) platformer;
            return new FloatRect(this.getPosition(), this.getSize());
        }
    
        @Override
        public Vector2f getSize() {
            return new Vector2f(platformer.getPlatformLevelSize().x, platformer.getPlatformTileSize().y);
        }
    
        @Override
        public void draw(RenderTarget arg0, RenderStates arg1) {
            for (ScrollableSprite sprite : sprites) {
                sprite.draw(arg0, arg1);
            }
        }
    
        @Override
        public void setSizeFrom(Platformer platformer) {
            int nb = platformer.getPlatformLevelSize().x / platformer.getPlatformTileSize().x;
            this.size = new Vector2f(platformer.getPlatformLevelSize().x, platformer.getPlatformTileSize().y);
            float positionX = ((DefaultScene)platformer).getPosition().x;
            float chunkWidth = this.platformer.getPlatformTileSize().x;
            this.sprites = new ScrollableSprite[nb+1];
            for (int i = 0; i < sprites.length; i++ ) {
                sprites[i] = new ScrollableSprite();
                if (this.texture != null) sprites[i].setTexture(this.texture);
                sprites[i].setSize(platformer.getPlatformTileSize());
                sprites[i].setPosition(positionX + i*chunkWidth, platformer.getFloorPosition(this.platformLevel));
                this.lastSpot += chunkWidth;
            }
    
        }
    
        @Override
        public void place(Platformer platformer, Vector2f platformPosition) {
            if (this.lastSpot > this.size.x) return;
        }
    
        @Override
        public final void scroll(float dx, float dy) {
            this.scroll(new Vector2f(dx,dy));
        }
    
        @Override
        public void scroll(Vector2f speed) {
            for (ScrollableSprite scroller : this.sprites) scroller.scroll(speed);
        }

        @Override
        public void tick(float time, Game controller) {
            this.tileTick(time, controller);
        }

        @Override
        public IntRect getPixelBounds(Game game) {
            return new IntRect(this.getBounds());
        }
    }
}
