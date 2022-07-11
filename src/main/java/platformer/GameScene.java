package platformer;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import org.jsfml.graphics.*;
import org.jsfml.system.*;

import ManageAnimation.*;
import devtools.*;
import fighter.Dorian;
import fighter.Enemy;
import assets.TextureCatalogue;
import controllers.*;
import managers.crittermanagers.*;
import models.Critter;
import npc.Guide;
import sys.Renderer;

/**
 * A class to represent a set of objects that "move" together.
 * 
 * Typically, a scene scrolls on the screen of the platformer, and consists of a
 * background, tiles (platform chunks) and various critters that all move at the
 * same speed.
 * 
 * @author Lydie Toure
 */
public class GameScene extends DefaultScene implements Observer
{
    /**
     * Different scene types for the scenes.
     * <p>
     * Each scene has a different background and tile layout, 
     * as well as already set grounds.
     */
    public enum SceneType {
        GROUND(5,15), 
        SKY (20,30),
        DUNGEON (10,25), 
        ARENA (0,1);

        public final int minCoin;
        public final int maxCoin;

        SceneType(int minim, int maxim) {
            this.minCoin = minim;
            this.maxCoin = maxim;
        }
    }

    /**
     * The current state of the GameScene.
     * It determines what is happening.
     */
    private enum Status {SCENE_CHANGE, SCENE_ZOOM, SCENE_SCROLL;}

    private static final TextureCatalogue GRAPHICS = TextureCatalogue.get();
    
    protected SceneType sceneType = null;
    protected Sprite wall = new Sprite(TextureCatalogue.get().SPIKE_WALL);
    
    public final Game controller;
    private Vector2f scrollingSpeed;

    protected Status status = Status.SCENE_SCROLL;
    protected Transition transition;
    // protected Guide guide;
    protected boolean inZone = false;

    public GameScene(Game gameController) {
        super();
        this.controller = gameController;
        this.wall.setScale(50f/wall.getLocalBounds().width,900f/wall.getLocalBounds().height);
        this.wall.setPosition(0, 100);
        // this.setPlatformerDimensions(3, new Vector2i(100,50), new Vector2i(controller.window.getSize().x,150));
        // this.setPosition(Vector2f.ZERO);
        // this.setSceneSize(controller.window.getSize());
        // this.setSceneType(SceneType.GROUND);
        // this.createView(controller.window);

    }

    @Override
    public void draw(RenderTarget arg0, RenderStates arg1) {
        super.draw(arg0, arg1);
        this.wall.draw(arg0, arg1);
    }
    

    @Override
    public void receiveUpdate(ObservableEvent event) {
        int id = event.getEventID();
        Object source = event.getSource();
    }

    @Override
    public void addToRenderer(Renderer renderer) {
        // Add every element to the renderer
        // The scene controls what is added
        renderer.addToRenderingList(this);
        // this.sceneElements.forEach(renderer::addToRenderingList);
    }

    /**
     * Removes this Scene from the rendering list of the given renderer
     * WARNING! The scene is typically always rendered, so this method
     * should be used with caution
     * 
     * @param renderer
     */
    @Override
    public void removeFromRenderer(Renderer renderer) {
        // The scene is typically always rendered,
        // and should not be removed but here we go
        renderer.removeFromRenderingList(this);
        // Do not remove all the scene elements
    }

    public void setScrollingSpeed(Vector2f speed) {
        this.scrollingSpeed = speed;
    }
    public Vector2f getScrollingSpeed() {
        return scrollingSpeed;
    }
    public void scroll() {
        this.scroll(this.scrollingSpeed);
    }

    @Override
    public void scroll(Vector2f speed) {
        if (this.status == Status.SCENE_SCROLL) {
            if (!this.controller.clockRunning() || this.inZone) return;
            super.scroll(speed);
        }
        else {
            this.transition.apply();
        }
    }

   
    public void setSceneType(SceneType type) {
        // if (this.sceneType == type) return;
        this.sceneType = type;
        reset();
        TextureCatalogue graphics = controller.GRAPHICS;
        Texture backgroundTexture = graphics.BG_GROUND_01;
        switch (sceneType) {
            case GROUND:
                backgroundTexture = graphics.BG_GROUND_01;
                this.createFloorsAt(0);
                this.sceneBuilder.setConstraints(1,3,2,5);
                // GuideManager.get(controller).setConstraints(1,2,10,20);
                MerchantManager.get(controller).setTimeBetweenSpawnsLimits(15f,30f);
                KeyManager.get(controller).setTimeBetweenSpawnsLimits(45f,90f);
                break;
            case DUNGEON:
                backgroundTexture = graphics.BG_DUNGEON_01;
                this.createFloorsAt(0);
                // Managers
                GuideManager.get(controller).setConstraints(1,2,10,20);
                MerchantManager.get(controller).setTimeBetweenSpawnsLimits(15f,30f);
                KeyManager.get(controller).setTimeBetweenSpawnsLimits(45f,90f);
                break;
            case SKY:
                backgroundTexture = graphics.BG_SKY_01;
                this.createFloorsAt();
                this.createTemporaryFloor(0);
                
                this.sceneBuilder.setConstraints(1, 4, 2f, 4f);
                GuideManager.get(controller).setConstraints(1,2,7,14);
                MerchantManager.get(controller).setTimeBetweenSpawnsLimits(15f,30f);
                KeyManager.get(controller).setTimeBetweenSpawnsLimits(35f,70f);
                break;
            case ARENA:
                backgroundTexture = graphics.BG_ARENA_01;
                Enemy.add(new Dorian(false,7,3,GRAPHICS.DorianIdleTexture,GRAPHICS.DorianRunTexture,0.4f));

                break;
            default: break; 
        }
        this.setTexture(backgroundTexture);
        this.notifyObservers(new ObservableEvent(this, EVENT_SCENE_CHANGE));
    }


    @Override
    public void setTileTexture(Tile tile) {
        TextureCatalogue graphics = TextureCatalogue.get();
        Texture texture = null;
        switch (this.sceneType) {
            case DUNGEON:
                texture = graphics.TILE_DUNGEON;
                break;
            case GROUND:
                texture = graphics.TILE_GROUND;
                break;
            case SKY: 
                texture = graphics.TILE_SKY;
                break;
            default:
               texture = graphics.TEMP_TILE_03;
        }
        tile.setTexture(texture);
    }

    public void appendToScene(Critter element) 
    {
        this.addToPlatformer(element, controller.window);
    }
    
    @Override
    public void addToPlatformer(SceneElement element) {
        this.addToPlatformer((Critter) element, controller.window);
    }

    @Override
    public void addToPlatformer(Critter element, RenderWindow window) {
        // super.addToPlatformer(element, window);
        int platform = element.getPlatformLevel();

        // The position of the offset
        float offsetX = platformWidths[platform];
        // The height of the level
        float platformY = this.getFloorPosition(platform);
        Vector2f platformFloor = new Vector2f(offsetX, platformY);

        Vector2f coordinatesView = window.mapPixelToCoords(new Vector2i(platformFloor), view);
        element.place(this, new Vector2f((float)Math.ceil(coordinatesView.x), (float)Math.ceil(coordinatesView.y)));
        // Update the end of the worlds
        // platformWidths[platform] += element.getBounds().width;

        // If it is not a tile, request a tile for it
        if (! (element instanceof Tile) ) {
            if (! this.hasFloor(platform)) {
                // if (element.getCritterType().isNPC())
                int nb = element.getTileCountRequirements();
                // Create a tile for this
                for (int i = 0; i < nb; i++) {
                    Tile t = this.sceneBuilder.acquire();
                    t.setPlatformLevel(element.getPlatformLevel()); 
                    this.addToPlatformer(t, window);
                    this.controller.critterManager.addActive(t);
                    t.addToRenderer(controller.gameRenderer);
                }
            }
            platformWidths[platform] += this.getPlatformTileSize().x;
            // System.out.println("World's no. " + platform + " ends at " + platformWidths[platform]);
        }
    }

    /**
     * Resets the platformer
     */
    public void reset() {
        // Replaces the view:
        this.createView(controller.window);
        
        // Platform widths
        if (platformWidths!=null) Arrays.fill(platformWidths, this.platformLevelSize.x);
    }

    /**
     * Obtains the type of scene that this GameScene is displaying.
     * 
     * @return the current scene type
     * @see SceneType
     */
    public SceneType getSceneType() {
        return this.sceneType;
    }


    /**
     * Starts a scene change
     * 
     * @param type
     */
    public void travel(SceneType type) {
        // if (this.guide == null) {
        //     return;
        // }
        Observer observer = evt -> {
            Guide.get(controller).exitZone();
            controller.gameRenderer.remove(transition);
            transition = null;
            controller.setClockState(true);
            status = Status.SCENE_SCROLL;
        };

        // Create a transition to change the scene
        this.createTransition(Status.SCENE_CHANGE, 20,()->{
            // controller.critterManager.resetScene();
            controller.critterManager.empty();
            setSceneType(type);
            // Add the guide:
            Guide guide = Guide.get(controller);
            guide.addToRenderer(controller.gameRenderer);
            this.controller.critterManager.addActive(guide);

            if (!this.hasFloor(guide.getPlatformLevel())) {
                for (int i = 0; i < guide.getTileCountRequirements(); i++) {
                    Tile tileGuide = sceneBuilder.acquire();
                    tileGuide.setPlatformLevel(guide.getPlatformLevel());
                    tileGuide.setPosition(guide.getPosition().x, guide.getPosition().y + guide.getBounds().height);
                    
                    tileGuide.addToRenderer(controller.gameRenderer);
                    this.controller.critterManager.addActive(tileGuide);
                }
                
            }
            // Tile tileGuide = sceneBuilder.acquire();
            // tileGuide.setPlatformLevel(guide.getPlatformLevel());
            // tileGuide.setPosition(guide.getPosition().x, guide.getPosition().y + guide.getBounds().height);
            
            // tileGuide.addToRenderer(controller.gameRenderer);
            // this.controller.critterManager.addActive(tileGuide);
            // Create a temporary at the bottom
            // this.createFloorsAt(0);
            // Re-place the place
            this.controller.hero.resetPosition(100f);
            
        }, observer);
    }

    // public List<Tile.Floor> getFloors() {
    //     return this.sceneFloors;
    // }

    

    private void createTransition(Status transitionType, int speed, Lambda effect, Observer observer) {
        final FloatRect area = new FloatRect(this.getPosition(), new Vector2f(this.getSceneSize()));
        this.transition = new FadeTransition(area, speed){
            @Override
            public void applyEffect() {
                effect.run();
            }
        };
        this.controller.gameRenderer.add(transition);
        this.controller.setClockState(false);
        transition.addObserver(observer);
        this.status = transitionType;
        this.transition.start();
    }


    /**
     * Creates a series a tiles to pave the ground of a scene.
     * The tiles are automatically added both to the game manager and renderer.
     * 
     * @param level the level at which the floor is to be created
     * @see #hasFloor(int) - if this Scene has a permanent floor at the given level, no tiles are made
     */
    public void createTemporaryFloor(int level) {
        // If the scene has a floor, return
        // if (this.hasFloor(level)) return;

        int tilesToFloor = 2 * this.getSceneSize().x / this.getPlatformTileSize().x;
        // final int floor = (int) this.getFloorPosition(0);
        // final float tileX = controller.window.mapPixelToCoords(new Vector2i(0,floor), view).x;
        // for (int i = 0; i < 2*tilesToFloor; i++) {
        //     Tile tile = this.sceneBuilder.acquire();
        //     tile.setPlatformLevel(level);
        //     // Vector2f pos = new Vector2f(i*this.getPlatformTileSize().x+tileX,floor);
        //     tile.setPosition(i*this.getPlatformTileSize().x + tileX, floor);
        //     // System.out.println("Tile " + (i+1) + " (pix: " + controller.window.mapCoordsToPixel(tile.getPosition())+") at pos: " + tile.getPosition());
        //     tile.addToRenderer(controller.gameRenderer);
        //     this.controller.critterManager.addActive(tile);
        // }
        this.createTemporaryFloor(level, tilesToFloor);
    }

    public void createTemporaryFloor(int level, int length) {
        // If the scene has a floor, return
        if (this.hasFloor(level)) return;

        
        final int floor = (int) this.getFloorPosition(0);
        final float tileX = controller.window.mapPixelToCoords(new Vector2i(0,floor), view).x;
        for (int i = 0; i < length; i++) {
            Tile tile = this.sceneBuilder.acquire();
            tile.setPlatformLevel(level);
            // Vector2f pos = new Vector2f(i*this.getPlatformTileSize().x+tileX,floor);
            tile.setPosition(i*this.getPlatformTileSize().x + tileX, floor);
            // System.out.println("Tile " + (i+1) + " (pix: " + controller.window.mapCoordsToPixel(tile.getPosition())+") at pos: " + tile.getPosition());
            tile.addToRenderer(controller.gameRenderer);
            this.controller.critterManager.addActive(tile);
        }   
    }

    
}

	

