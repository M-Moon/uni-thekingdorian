package platformer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.jsfml.graphics.FloatRect;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.Sprite;
import org.jsfml.graphics.Text;
import org.jsfml.graphics.Texture;
import org.jsfml.graphics.View;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;

import devtools.*;
import assets.TextureCatalogue;
import managers.AbstractManager;
import models.Critter;
import sys.Renderable;
import sys.Renderer;


/**
 * This class offers an alternative to the {@link AbstractPlatformer AbstractPlatformer} class,
 * which is also an implementation of the the Platformer interface.
 * 
 * In this implementation, the scene does not keep track of the elements in it.
 * The World class in this package relies on SceneElements having a tick method
 * which updates them.
 */
public class DefaultScene implements Platformer, Renderable, Observable
{
    public static final int EVENT_FLOOR_REMOVED = Critter.EVENT_CRITTER_RELEASED;
    public static final int EVENT_FLOOR_SET = - EVENT_FLOOR_REMOVED;
    public static final int EVENT_PERMISSIBLE_FLOORS_DATA = 401;
    public static final int EVENT_SCENE_CHANGE = 402;
    protected List<Observer> observers = new ArrayList<>();

    public final ScrollableSprite background;
    // protected final Queue<SceneElement> sceneElements;
    public final View view;
    protected AbstractManager<Tile> sceneBuilder;

    protected Vector2i worldSize;
    protected Vector2f worldPosition;

    protected boolean viewInitialized = false;
    protected boolean visible = true;
    protected boolean rendered = true;
    protected int layer = 0;
    
    protected int nbPlatform = 3;
    protected Vector2i platformLevelSize = new Vector2i(80,50);
    protected Vector2i platformTileSize = new Vector2i(120,600);

    // protected FloatRect worldArea;
    protected float[] platformWidths;
    protected List<Tile.Floor> sceneFloors = new ArrayList<>();;
    protected boolean[] floorLayout;

    private int type = 0;
    private int[] floorlessLevels;

    public DefaultScene() {
        this.background = new ScrollableSprite();
        // this.sceneElements = new LinkedList<>();

        this.worldPosition = Vector2f.ZERO;
        this.platformWidths = null;
        // this.visibleArea = FloatRect.EMPTY;

        this.view = new View();
    }

    /**
     * Initializes the view of this platformer. 
     * <p>
     * The scene elements are all placed on this view, and as such it is important that their
     * position be set accordingly.
     * 
     * @param window the window in which this GameScene is placed
     * @return the view on which SceneElements will be placed
     */
    public View createView(RenderWindow window) {
        Vector2f size = new Vector2f(this.worldSize);
        this.view.reset(new FloatRect(this.getPosition(), size));
        // System.out.println("View center " + view.getCenter() + ", and size " + view.getSize());

        Vector2f winSize = new Vector2f(window.getSize());
        FloatRect viewPort = new FloatRect(Vector2f.componentwiseDiv(this.worldPosition, winSize),
            Vector2f.componentwiseDiv(size, winSize));
        // System.out.println("View port " + view.getViewport() + ", and size " + view.getSize());
            this.view.setViewport(viewPort);
        if (!viewInitialized) {
            this.viewInitialized = true;
        }
        return this.view;
    }

    /**
     * Attaches an AbstractManager object to this scene for easier scene building.
     * The DefaultScene can work without it.
     * 
     * @param tileManager the platform chunk manager to attach to this GameScene
     */
    public void setSceneBuilder(AbstractManager<Tile> tileManager) {
        this.sceneBuilder = tileManager;
    }

    /**
     * Obtains the scene builder which was attached to this DefaultScene
     * 
     * @return the abstract manager for tiles to be placed onto this platformer
     */
    public AbstractManager<Tile> getSceneBuilder() {
        return this.sceneBuilder;
    }

    @Override
    public void scroll(float dx, float dy) {
        this.scroll(new Vector2f(dx,dy));
    }

    @Override
    public void scroll(Vector2f speed) {
        this.background.scroll(speed);
        this.view.move(speed);
    }

    @Override
    public void setPlatformerDimensions(int nbPlatform, Vector2i tileSize, Vector2i levelSize) {
        this.setNumberFloors(nbPlatform);
        this.setPlatformLevelSize(levelSize);
        this.setPlatformTileSize(tileSize);
        this.setSceneSize(new Vector2i(this.platformLevelSize.x, this.computePlatformerHeight()));
    }

    @Override
    public float getFloorPosition(int platformIndex) {
        return (float)this.getSceneSize().y - ((platformIndex + 1) * this.getPlatformTileSize().y + platformIndex * this.getPlatformLevelSize().y); 
    }

    @Override
    public Vector2i getPlatformTileSize() {
        return this.platformTileSize;
    }

    @Override
    public void setPlatformTileSize(Vector2i tileSize) {
        this.platformTileSize = tileSize;
    }

    @Override
    public void setNumberFloors(int nbPlatform) {
        this.nbPlatform = nbPlatform;

        // Update the number of floors
        if (this.platformWidths == null) {
            this.platformWidths = new float[this.nbPlatform];
            Arrays.fill(this.platformWidths, 0f);
        } else if (this.platformWidths.length != this.nbPlatform) {
            this.platformWidths = Arrays.copyOf(this.platformWidths, nbPlatform);
        }
    }

    @Override
    public int getNumberFloors() {
        return nbPlatform;
    }

    @Override
    public int computePlatformerHeight() {
        return (nbPlatform)* (this.getPlatformTileSize().y + this.getPlatformLevelSize().y);
    }

    @Override
    public Vector2i getPlatformLevelSize() {
        return this.platformLevelSize;
    }

    @Override
    public void setPlatformLevelSize(Vector2i levelSize) {
        this.platformLevelSize = levelSize;
        // Set the platform widths
        if (this.platformWidths == null) return;
        if (this.platformWidths[0] == 0.0f) {
            Arrays.fill(platformWidths, this.platformLevelSize.x);
        }
        
    }

    @Override
    public void setSceneSize(Vector2i size) {
        this.setSceneSize(size, SizingOption.FILL_UP);
    }

    public void setSceneSize(Vector2i size, SizingOption option) {
        Vector2i sz = size;
        final int platformerHeight = this.computePlatformerHeight();
        switch (option) {
            case RESET_HEIGHT:
                sz = new Vector2i(size.x, platformerHeight);
                break;
            case FILL_UP:
                // if the extra height is negative reset it
                int xtra = size.y - platformerHeight;
                if (xtra < 0) {
                    this.setSceneSize(size, SizingOption.RESET_HEIGHT);
                }
                break;
            default: break;
        }

        // Set the size of the background
        this.worldSize = sz;
        this.background.setSize(this.worldSize);
    }

    @Override
    public Vector2i getSceneSize() {
        return this.worldSize;
    }

    @Override
    public void addToPlatformer(SceneElement element) {
        int platform = element.getPlatformLevel();

        // The position of the offset
        float offsetX = platformWidths[platform];
        // The height of the level
        float platformY = this.getFloorPosition(platform);
        Vector2f platformFloor = new Vector2f(offsetX, platformY);
        element.place(this, platformFloor);

        // Update the end of worlds
        platformWidths[platform] += element.getSprite().getGlobalBounds().width;
    }

    /**
     * Prepares a critter to be placed onto a platformer.
     * @param element
     * @param window
     */
    public void addToPlatformer(Critter element, RenderWindow window) {
        int platform = element.getPlatformLevel();

        // The position of the offset
        float offsetX = platformWidths[platform];
        // The height of the level
        float platformY = this.getFloorPosition(platform);
        Vector2f platformFloor = new Vector2f(offsetX, platformY);

        Vector2f coordinatesView = window.mapPixelToCoords(new Vector2i(platformFloor), view);
        element.place(this, new Vector2f((float)Math.ceil(coordinatesView.x), (float)Math.ceil(coordinatesView.y)));
        
        // element.place(this,platformFloor);
        // System.out.println("Element added to platformer at pos " + element.getPosition() + " availble pos was " +new Vector2i(platformFloor));

        // Update the end of the worlds
        // platformWidths[platform] += element.getBounds().width;

    }

    
    public View getView() {
        return this.view;
    }

    @Override
    public void draw(RenderTarget arg0, RenderStates arg1) {
        this.background.draw(arg0, arg1);
        this.sceneFloors.forEach(f -> f.draw(arg0, arg1));
    }

    @Override
    public boolean isBeingRendered() {
        return rendered;
    }

    @Override
    public void startRendering(boolean rendered) {
        this.rendered = rendered;
    }

    @Override
    public Sprite getSprite() {
        return background;
    }

    @Override
    public void setTexture(Texture texture) {
        this.background.setTexture(texture);
    }

    @Override
    public int getLayer() {
        return layer;
    }

    @Override
    public void setLayer(int layer) {
        this.layer = layer;
    }

    @Override
    public void addToRenderer(Renderer renderer) {
        renderer.addToRenderingList(this);
    }

    @Override
    public void removeFromRenderer(Renderer renderer) {
        renderer.removeFromRenderingList(this);

    }

    @Override
    public Vector2f getPosition() {
        return worldPosition;
    }

    @Override
    public void setPosition(Vector2f position) {
        this.worldPosition = position;
    }

    @Override
    public final void setPosition(float x, float y) {
        this.setPosition(new Vector2f(x,y));
    }

    public void zoomIn(Critter center, Vector2f padding) {
        FloatRect critterBounds = center.getBounds();
        FloatRect zoomedArea = new FloatRect(critterBounds.left + critterBounds.width/2,
            critterBounds.top + critterBounds.height/2,
            critterBounds.width + 2 * padding.x, critterBounds.height + 2*padding.y);
        this.zoomIn(zoomedArea);
    }

    public void zoomIn(FloatRect zoomedArea) {
        Vector2f center = new Vector2f(zoomedArea.left + zoomedArea.width/2,
            zoomedArea.top + zoomedArea.height/2);
        Vector2f size = new Vector2f(zoomedArea.width, zoomedArea.height);
        view.setCenter(center);
        view.setSize(size);
    }

    public void zoomOut() {
        Vector2f sceneSize = new Vector2f(worldSize);
        Vector2f sceneCenter = Vector2f.add(worldPosition, Vector2f.div(sceneSize, 2f));
        view.setCenter(sceneCenter);
        view.setSize(sceneSize);
    }

    /**
     * Determines whether the critter is visible in the given window
     * 
     * @param critter the critter to check the visibility of
     * @param window the window in which the critter may be visible
     */
    public boolean checkCritterVisible(Critter critter, RenderWindow window) {
        Vector2f pixelPosition = new Vector2f(window.mapCoordsToPixel(critter.getPosition(), this.view));
        FloatRect boundsPixel = new FloatRect(pixelPosition, critter.getSize());
        FloatRect world = new FloatRect(worldPosition, new Vector2f(worldSize));
        return boundsPixel.intersection(world)!=null;
    }

    public float[] getEndOfWorlds() {
        return platformWidths;
    }

    
    public boolean hasFloor(int platform) {
        if (platform >= nbPlatform || platform < 0) return false;
        return this.floorLayout[platform];
    }

    
    /**
     * Obtains the platform on which the Critter is in.
     * <p>
     * This method calculates the level this Critter should be at.
     * 
     * @param critter the critter to find the level of
     * 
     */
    public int getLevelOf(Critter critter) {
        final float feet = critter.getFeetPosition();
        System.out.println("Feet at " + feet);
        // java.util.function.Function<Integer,Float> floor = i -> i==nbPlatform ? (float)computePlatformerHeight() : getFloorPosition(i);
        for (int lv = 1; lv < nbPlatform; lv ++) {
            System.out.println("Floor " + lv + " between " + getFloorPosition(lv-1) + " and " + getFloorPosition(lv));
            if (feet > getFloorPosition(lv)) {
                return lv - 1;
            }
        }
        return nbPlatform - 1;
    }

    public void setLevelOf(Critter critter) {
        final int level = this.getLevelOf(critter);
        critter.setPlatformLevel(level);
    }


    /**
     * Determines which levels always have tiles, rather than sporadically spawned ones.
     * 
     * @param levels an enumeration of the levels which always have tiles
     */
    public void createFloorsAt(int... levels) {
        if (levels==null) return;

        // Notify the observers that the floors will be reset
        this.sceneFloors.forEach((Tile.Floor floor) -> this.notifyObservers(new ObservableEvent(floor, EVENT_FLOOR_REMOVED)));
        // Reset the floors
        this.sceneFloors = new ArrayList<>();
        this.floorLayout = new boolean[nbPlatform];

        int[] valids = Arrays.stream(levels).filter(i -> i>=0 && i<nbPlatform).distinct().sorted().toArray();
        for (int level : valids) {
            Tile.Floor floor = new Tile.Floor(this, level);
            floor.startRendering(true);
            this.setTileTexture(floor);
            this.sceneFloors.add(floor);
            this.floorLayout[level] = true;
            continue;
        }

        List<Integer> floorLess = new ArrayList<>();
        for (int i = 0; i < floorLayout.length; i++) {
            if (!floorLayout[i]) {
                floorLess.add(i);
            }
        }
        this.floorlessLevels =  floorLess.stream().mapToInt(Integer::intValue).toArray();
        
        // Notify the observers that new floors have been created 
        this.sceneFloors.forEach((Tile.Floor floor) -> this.notifyObservers(new ObservableEvent(floor, EVENT_FLOOR_SET)));
        // this.floorlessLevels = invalids;
        // this.notifyObservers(new ObservableEvent(this.floorlessLevels, EVENT_PERMISSIBLE_FLOORS_DATA));
        
    }

    public int[] getFloorlessLevels(){
        if (floorlessLevels==null) {
            this.floorlessLevels = IntStream.of(nbPlatform).toArray();
        }
        return this.floorlessLevels;
    }

    public void setType(int type) {
        this.type = type;
        // Set the appropriate texture
        TextureCatalogue graphics = TextureCatalogue.get();
        Texture texture = new Texture();
        switch (this.type) {
            case 0:
                texture = graphics.BG_DUNGEON_01;
                break;
            case 1:
                texture = graphics.BG_GROUND_01;
                break;
            case 2: 
                texture = graphics.BG_SKY_01;
                break;
            default:
               texture = graphics.TEMP_TILE_03;
        }
        this.setTexture(texture);
        this.notifyObservers(new ObservableEvent(this, EVENT_SCENE_CHANGE));
        // this.sceneFloors.forEach(this::setTileTexture);


    }

    /**
     * Sets the texture of a Tile
     * 
     * @param tile
     */
    public void setTileTexture(Tile tile) {
        TextureCatalogue graphics = TextureCatalogue.get();
        Texture texture = null;
        switch (this.type) {
            case 0:
                texture = graphics.TILE_DUNGEON;
                break;
            case 1:
                texture = graphics.TILE_GROUND;
                break;
            case 2: 
                texture = graphics.TILE_SKY;
                break;
            default:
               texture = graphics.TEMP_TILE_03;
        }
        tile.setTexture(texture);
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
}
