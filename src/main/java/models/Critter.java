package models;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.jsfml.graphics.FloatRect;
import org.jsfml.graphics.IntRect;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.graphics.Sprite;
import org.jsfml.graphics.Texture;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;

import controllers.Game;
import devtools.*;
import platformer.GameScene;
import platformer.SceneElement;
import sys.Renderer;

/**
 * A Critter represents a game piece, that is anything that can be interacted
 * within the game.
 *
 * Critters are characters, game tokens, but also obstacles, platform chunks.
 */
public abstract class Critter implements SceneElement, Observable
{

    /** Event ID to notify observers that this Critter is no longer visible and was returned to the pool */
    public static final int EVENT_CRITTER_RELEASED = 101;
    public static final int EVENT_ACQUIRED_CRITTER = -EVENT_CRITTER_RELEASED;
    //public boolean isBeingRendered;

    /** Critter types */
    public enum CritterType {  
        HERO, TILE, KEY, COIN, WEAPON, POTION, OBSTACLE, PROJECTILE, ENEMY, GUIDE, MERCHANT;

        /**
         * Determines whether this CritterType represents a collectible
         * 
         * @return true only for KEY,COIN,WEAPON and POTION
         */
        public boolean isCollectible() {
            return this.ordinal() > 1 && this.ordinal() < 6;
        }

        /**
         * Determines whether this CritterType represents a non-playing character
         * @return
         */
        public boolean isNPC() {
            return this==GUIDE || this==MERCHANT;
        }

        public boolean isDamaging() {
            return this==OBSTACLE || this==PROJECTILE || this==ENEMY;
        }
        
    };

    protected Sprite sprite = new Sprite();

    protected int platformLevel;
    // SceneElement parameters
    protected boolean isVisibleYet;

    // Rendering parameters
    public boolean rendered = false;
    protected int layer = 0;

    // Observable
    protected List<Observer> observers = new ArrayList<>();
    protected boolean isColliding = false;

    protected Critter() {
        // Set texture and size
    }

    public void move(float dx, float dy) {
        this.sprite.move(dx, dy);
    }

    public final void move(Vector2f disp) {
        this.move(disp);
    }

    @Override
    public void draw(RenderTarget target, RenderStates states) {
        sprite.draw(target, states);
    }

    @Override // Renderable
    public final void setTexture(Texture texture) {
        // this.sprite.setTexture(texture);
        this.setTexture(texture, true);
    }
    
    /**
     * Changes the texture of this critter
     *
     * @param texture the new texture for this critter
     * @param keepSize whether this Critter should keep the size it had
     */
    public void setTexture(Texture texture, boolean keepSize) {
        // If the size is 0 (there is no texture in the sprite)
        // Simply set the texture
        if (keepSize) {
            if (this.getSize().equals(Vector2f.ZERO)) {
                this.setTexture(texture, false);
            } else {
                Vector2f size = this.getSize();
                this.sprite.setTexture(texture);
                this.sprite.scale(Vector2f.componentwiseDiv(size, this.getSize()));
            }
        }
        else {
            this.sprite.setTexture(texture);
        }
    }

        /**
     * Sets the texture of this Critter
     * 
     * @param file the path to the texture for this Token
     */
    public final void setTexture(Path file) {
        Texture texture = new Texture();
        try {
            texture.loadFromFile(file);
        } catch (IOException e) {
            System.out.println("Could not load texture");
            e.printStackTrace();
        }
        this.setTexture(texture);   
    }

    public Texture getTexture(){
        return (Texture) this.sprite.getTexture();
    }

    public CritterType getCritterType() {
        return null;
    }

    /**
     * Scales the main sprite of this Critter
     * 
     * @param newSize the new size that this critter must have
     */
    public void setSize(Vector2f newSize) {
        this.scale(Vector2f.componentwiseDiv(newSize, this.getSize()));
    }

    /**
     * Scales the main sprite of this Critter
     * 
     * @param x the width of the Critter after scaling
     * @param y the heigh tof the Critter after scaling
     */
    public final void setSize(float x, float y) {
        this.setSize(new Vector2f(x,y));
    }

    public final void scale(Vector2f scale) {
        this.scale(scale.x, scale.y);
    }
    public void scale(float x, float y) {
        this.sprite.scale(x, y);
    }

    @Override // Renderable
    public Vector2f getPosition() {
        return sprite.getPosition();
    }

    @Override // Renderable
    public void setPosition(Vector2f position) {
        this.sprite.setPosition(position);
    }

    @Override // Renderable
    public void setPosition(float x, float y) {
        this.sprite.setPosition(x, y);
    }

    /**
     * Obtains the number of tiles that this Critter typically stands on.
     * 
     * @return how many tiles this Critter sits on (default 1)
     */
    public int getTileCountRequirements() {
        return 1;
    }

    /**
     * Obtains the rectangle surrounding the relevant bounds of this Critter
     * 
     * @return the rectangle representing the part of the texture shown
     */
    public FloatRect getBounds() {
        // return this.sprite.getGlobalBounds();
        return new FloatRect(this.getPosition(), this.getSize());
    }

    public Vector2f getSize() {
        FloatRect rect = this.sprite.getGlobalBounds();
        return new Vector2f(rect.width, rect.height);
    }

    /**
     * Obtains the position of the bottom of this critter
     */
    public float getFeetPosition() {
        return sprite.getGlobalBounds().top + sprite.getGlobalBounds().height;
    }


    @Override
    public final Sprite getSprite() {
        return this.sprite;
    }

    @Override
    public boolean isBeingRendered() {
        return this.rendered;
    }

    @Override
    public void startRendering(boolean rendered) {
        this.rendered = rendered;
    }

    @Override
    public void addToRenderer(Renderer renderController) {
        renderController.addToRenderingList(this);
        this.startRendering(true);
    }

    @Override
    public void removeFromRenderer(Renderer renderer) {
        renderer.removeFromRenderingList(this);
        this.startRendering(false); // stop rendering
        // Tell the observers
        this.notifyObservers(new ObservableEvent(this, EVENT_CRITTER_RELEASED));
    }

    @Override
    public int getLayer() {
        return this.layer;
    }

    @Override
    public void setLayer(int layer) {
        this.layer = layer;
    }


    @Override
    public void notifyObservers(ObservableEvent event) {
        this.observers.forEach(o -> o.receiveUpdate(event));
    }

    @Override
    public void addObserver(Observer observer) {
        this.observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        this.observers.remove(observer);

    }

    @Override
    public boolean isHidden() {
        return !isVisibleYet;
    }

    @Override
    public void updateVisibility(FloatRect area) {
        if (!this.rendered) {
            this.isVisibleYet = false;
            return;
        }

        // It is fully visible when the scene size is fully inside the area
        this.isVisibleYet = this.getBounds().intersection(area) != null;
        // The pool (where applicable) should be notified of this
        // System.out.println("\tElement [P="+getPosition()+";"+getPlatformLevel()+"] is
        // visible:" + this.isVisibleYet);
    }

    @Override
    public void setVisibility(boolean visibleYet) {
        this.isVisibleYet = visibleYet;
    }

    @Override
    public boolean overlapsWith(SceneElement element) {
        // TODO : each subclass should implement its own version of this
        return this.getBounds().intersection(element.getSprite().getGlobalBounds()) != null;
    }

    /**
     * Determines whether this critter is colliding with another critter in an instance of the game.
     * <p>
     * Collision implies that the bounds of their respectives sprites overlap in the game window
     * 
     * @param critter
     * @param game
     * @return true only if the two critters are colliding
     * @see #getPixelBounds(Game)
     */
    public boolean collides(Critter critter, Game game) {
        return this.getPixelBounds(game).intersection(critter.getPixelBounds(game)) != null;
    }

    /**
     * Defines actions to be undertaken by this critter at each tick.
     * 
     * <p>
     * Ideally, this method should 
     * 
     * @param time time since last tick
     * @param controller the game instance in which this Critter ticks
     */
    public void tick(float time, Game controller) {
        // Unspawns the Critter
        GameScene platformer = controller.scene;
        boolean visible = platformer.checkCritterVisible(this, controller.window);
        if (!visible && isVisibleYet)
            this.removeFromRenderer(controller.gameRenderer);
        setVisibility(visible);

        
    }

    /**
     * Performs the appropriate give action when this critter has meaningfully collided
     * with the hero in an instance of the game.
     * <p> Example: in the Obstacle class:
     * <pre> 
     * public void tick(float time, Game game) {
     *  super.tick(time,game);          // Unspawns it disappeared
     *  this.handleCollision(game, game.hero::takeDamage());    
     * }</pre> 
     *
     * 
     * @param controller the game instance in which the collision is happening
     * @param collisionAction a void method describing what must happen whenever the
     */
    protected void handleCollision(Game controller, Lambda collisionAction) {
        // Actions
        boolean collided = this.collides(controller.hero, controller);
        if (!this.rendered || collided==this.isColliding) {
            return;
        }
        if (collided && !this.isColliding) {
            this.isColliding = true;
            collisionAction.run();
        }
        if (!collided && this.isColliding) {
            this.isColliding = false;
        }
    }




    @Override
    public void setPlatformLevel(int platform) {
        this.platformLevel = platform;
    }

    @Override
    public int getPlatformLevel() {
        return this.platformLevel;
    }

    public final FloatRect getGlobalBounds(){
        FloatRect globalBounds = this.getBounds();
        return globalBounds;
    }

    /**
     * Obtains the bounds of this critter in the game window
     * 
     * @param window
     * @return
     */
    public IntRect getPixelBounds(Game controller) {
        Vector2i pixelPos = controller.window.mapCoordsToPixel(this.getPosition(), controller.scene.view);
        return new IntRect( pixelPos, new Vector2i(this.getSize()));
    }


    //public abstract Critter changeTexture(Texture collectibleTexture);

    /**
     * changes the Texture of a collectible
     * @param collectible
     * @param collectiblePath path of the image to change the texture to
     * @return collectible with changed texture
     * @throws IOException
     */
    public Critter changeTexture(Critter collectible, Path collectiblePath) throws IOException
    {
        // TODO : Re-write this, Mara
        Texture texture = new Texture();
        texture.loadFromFile(collectiblePath);
        collectible.setTexture(texture);
        return collectible;
    }

    public String getPath(){

     //    this.Sprite.texture.resource_path;

        return null;
    };
}