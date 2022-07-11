package platformer;

import org.jsfml.graphics.FloatRect;
import org.jsfml.system.Vector2f;

import sys.Renderable;

/**
 * An interface to mark objects which are attached to a scene and therefore
 * scroll along with it. 
 * 
 * This interface offers a way to keep track of scene elements
 * @author Lydie Toure
 */
public interface SceneElement extends Renderable
{
    /**
     * Checks whether this element is fully visible in the scene.
     * @return true only if the element is completely visible in the screen
     */
    public boolean isHidden();

    /**
     * Updates the visibility of this tile
     * 
     * @param area the coordinates within which this Tile is meant to be visible
     */
    public void updateVisibility(FloatRect area);
    /**
     * 
     * @param visibleYet whether this SceneElement is visible
     */
    public void setVisibility(boolean visibleYet);

    public void setPlatformLevel(int platform);
    public int getPlatformLevel();

    /**
     * Determines whether this SceneElement is colliding with another one
     * in a way that would make their respective sprites overlap on the screen.
     * 
     * @param element
     * @return true only if the two elements overlap in an undesirable way
     * @deprecated 
     */
    @Deprecated(forRemoval = true)
    public boolean overlapsWith(SceneElement element);

    /**
     * Places this SceneElement on a given platformer, on a given platform
     * 
     * @param scene the platformer on which this SceneElement is to be placed
     * @param platformPosition the position of the bottom left corner of a platform
     */ 
    public default void placeInScene(AbstractScene scene, Vector2f platformPosition){
        // Old implementation of the scene
    }

    // public void placeInPlatformer(Platformer platformer, Vector2f platformPosition);

    /**
     * Places this SceneElement on a given platformer, on a given platform
     * 
     * @param scene the platformer on which this SceneElement is to be placed
     * @param availablePosition an available position in the scene to put this Scene element
     */ 
    public void place(Platformer scene, Vector2f availablePosition);
}

