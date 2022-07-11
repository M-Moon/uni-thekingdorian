package managers.crittermanagers;

import controllers.Game;
import devtools.*;
import managers.AbstractManager;
import platformer.DefaultScene;
import platformer.GameScene;
import platformer.Platformer;
import platformer.Tile;
import sys.Renderer;

public class TileManager extends AbstractManager<Tile> implements Observer
{

    protected static TileManager INSTANCE = null;

    protected int[] permissibleFloors;
    private int lastPlatformLevel;          // the last level at which we put a platform

    protected TileManager(Platformer platformer, Renderer renderer) {
        super(platformer, renderer);
        this.active = true;
    }

    protected TileManager(Game game) {
        super(game);
        this.active = true;
    }

    public static TileManager get(Platformer platformer, Renderer renderer) {
        if (INSTANCE == null) {
            INSTANCE = new TileManager(platformer, renderer);
        }
        return INSTANCE;
    }

    public static TileManager get(Game game) {
        if (INSTANCE == null) {
            INSTANCE = new TileManager(game);
        }
        return INSTANCE;
    }

    @Override
    public Tile create() {
        Tile element = new Tile(this.platformer);
        // element.setLayer(1);
        // element.setTexture(assets.TextureCatalogue.get().TEMP_TILE_03);
        // element.setSizeFrom(this.platformer);
        return element;
    }

    @Override
    public void parametrizeElement(Tile element) {
        // super.parametrizeElement(element);

        // if (controller.scene.hasFloor(0)) {
        //     element.setPlatformLevel(random.nextInt(2)+1);
        // } else {
        //     element.setLayer(this.lastPlatformLevel);   // 
        //     lastPlatformLevel = (lastPlatformLevel+1)%3;
        // }

        // GameScene scene = (GameScene) platformer;
        int[] lvs = controller.scene.getFloorlessLevels();
        element.setPlatformLevel(lvs[random.nextInt(lvs.length)]);
        controller.scene.setTileTexture(element); 
    }

    /**
     * Process a request
     */
    public void request(ObservableEvent event) {
        // Remember that there is a need for a tile
    }

    /**
     * Set the levels on which tiles can be spawned
     */
    protected void setPermissibleFloors(int[] floors) {
        this.permissibleFloors = floors;
    }

    @Override
    public void receiveUpdate(ObservableEvent event)
    {
        if (event.getEventID()==DefaultScene.EVENT_PERMISSIBLE_FLOORS_DATA) {
            this.setPermissibleFloors((int[]) event.getSource());
        }
    }
    
}
