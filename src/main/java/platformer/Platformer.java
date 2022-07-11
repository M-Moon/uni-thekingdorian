package platformer;

import org.jsfml.system.Vector2i;
import platformer.*;

/** 
 * An interface which defines a 2D world in which SceneElements navigate
 * 
 * A scene typically is a graphical entity comprised of a background (itself) and other SceneElements
 * which scrolls.
 */
public interface Platformer extends Scrollable
{
    public enum SizingOption {
        /** Resets the size of this platformer according to its given dimensions */
        RESET_ALL,
        /** Resize the height of the window */
        RESET_HEIGHT,
        /** Fill the leftover size by adding space on top */
        FILL_UP,
        /** Do nothing */
        DISMISS;
    }

    public void setPlatformerDimensions(int nbPlatform, Vector2i tileSize, Vector2i levelSize);

    /**
     * Obtains the Y-position of a given platform
     * 
     * @param platformIndex
     * @return
     */
    public float getFloorPosition(int platformIndex);

    public Vector2i getPlatformTileSize();

    public void setPlatformTileSize(Vector2i tileSize);

    public void setNumberFloors(int nbPlatform);

    public int getNumberFloors();

    public int computePlatformerHeight();

    public Vector2i getPlatformLevelSize();

    public void setPlatformLevelSize(Vector2i levelSize);

    // public void setSceneSize(Vector2i size, SizingOption option);

    public void setSceneSize(Vector2i size);

    public Vector2i getSceneSize();

    /**
     * Adds a critter to the end of this Scene to one of the platform levels.
     * SceneElement objects must also implement the {@link SceneElement#place(Platformer, org.jsfml.system.Vector2f) place}
     * method so that they are put at the position they see fit.
     * 
     * @param critter the critter to append to the scene
     */
    public void addToPlatformer(SceneElement element);



}

