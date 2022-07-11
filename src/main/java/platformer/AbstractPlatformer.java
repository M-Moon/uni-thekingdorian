package platformer;

import java.util.function.Predicate;

import org.jsfml.graphics.FloatRect;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;

import devtools.GameLogger;
import views.*;
import sys.Renderer;

/**
 * This class attemps a platformer which takes care of its elements.
 * While it implements the Platformer interface, it does not function in the same
 * way as the {@link DefaultScene DefaultScene}, which uses a View to display the scene elements.
 * 
 * @author Lydie Toure
 */
public abstract class AbstractPlatformer extends AbstractScene implements Platformer
{
    public static final int NOT_ON_ANY_PLATFORM = -1;
    // Default values

    protected Vector2f scenePosition = Vector2f.ZERO;
    protected int nbPlatform;
    protected Vector2i platformLevelSize;
    protected Vector2i platformTileSize;
    
    protected int platformThickness;        // only if nbPlatform > 1
    protected int platformHeight;
    protected boolean rendered = true;


    @Override
    public int getLayer() {
        return 0;
    }

    @Override
    public void setLayer(int layer) {
        // Do nothing
    }

    @Override
    public void startRendering(boolean rendered) {
        this.rendered = rendered;
    }

    @Override
    public void addToRenderer(Renderer renderer) {
        renderer.addToRenderingList(this);
    }

    @Override
    public void removeFromRenderer(Renderer renderer) {
        renderer.removeFromRenderingList(this);
        // Remove all of the scene elements
        this.sceneElements.forEach(e -> e.removeFromRenderer(renderer));
    }

    @Override
    public Vector2f getPosition() {
        return this.scenePosition;
    }

    @Override
    public void setPosition(float x, float y) {
        this.scenePosition = new Vector2f(x,y);
        this.background.setPosition(this.scenePosition);
    }

    @Override
    public void draw(RenderTarget arg0, RenderStates arg1) {
        this.background.draw(arg0, arg1);
    }

    @Override
    public void addToPlatformer(SceneElement element) {
        this.appendToScene(element);
    }

    @Override
    public void appendToScene(SceneElement element) {

        this.sceneElements.add(element);
        element.setVisibility(false);
        // Append to the end of the scene
        float x = this.sceneSize.x;
        // Obtain the position of the appropriate platform
        int platform = element.getPlatformLevel();
        float platformFloorY = this.getFloorPosition(platform);
        Vector2f platformFloor = new Vector2f(x, platformFloorY);
        element.placeInScene(this, platformFloor);
        element.setVisibility(false);
        element.startRendering(true);
        
    }

    @Override
    public void updateScene(Renderer renderer) {
        FloatRect sceneArea = new FloatRect(this.getPosition(), new Vector2f(this.sceneSize));
        for (SceneElement element : this.sceneElements) {
            // If the element can be placed (i.e., it is hidden and wouldn't overlap with anything else)
            // Then we add it to the renderer
            if (element.isHidden()) {
                if (canBePlaced(element)) {
                    element.addToRenderer(renderer);
                    // Make it visible
                    element.setVisibility(true);
                }
            }

            // Otherwise, if the element is visible
            else {
                // Update the visibility of already visible elements
                element.updateVisibility(sceneArea);

                // If it is not visible anymore, remove it from the renderer
                if (element.isHidden()) {
                    element.removeFromRenderer(renderer);
                }
            }
        }

        // Clean-up the list from elements which are not rendered anymore
        this.sceneElements.removeIf(Predicate.not(SceneElement::isBeingRendered));
    }

    /**
     * Determines whether a given element can be added to the renderer without
     * overlapping with another in any meaningful way (for example, two tiles on top of oneanother rather than one after the other)
     * 
     * Note that this only makes sense for elements which are still hidden on the right side
     * of this Scene, that is, for elements for which isHidden()==true. If the given parameter
     * is already visible then it will return false
     * 
     * @param element a hidden element 
     * @return true only if the element is hidden and can be added to the renderer
     */
    protected boolean canBePlaced(SceneElement element) {

        // If the element is already on-screen, it's nonsense
        if (!element.isHidden()) {
            return false;
        }

        // Otherwise, 
        boolean canBe = this.sceneElements.stream()
            .filter(e -> e.getPlatformLevel()==element.getPlatformLevel())      // same platform level
            .filter(Predicate.not(SceneElement::isHidden))                      // only those which are visible
            .filter(Predicate.not(Predicate.isEqual(element)))                  // which aren't itself
            .filter(element::overlapsWith)                                      // and that overlap with the OG element
            .count() == 0;                                                      // none of them
        return canBe;
    }

    
    /**
     * Obtains the Y-position of a given platform
     * 
     * @param platformIndex
     * @return
     */
    public final float getFloorPosition(int platformIndex) {
        // float floor = this.computePlatformerHeight() - (platformIndex+1) * platformTileSize.y - platformIndex * platformLevelSize.y;
        // util.GameLogger.get().info("Floor : " + floor);
        return (float)this.getSceneSize().y - ((platformIndex + 1) * this.getPlatformTileSize().y + platformIndex * this.getPlatformLevelSize().y); 
    }

    public void setNumberFloors(int nbPlatform) {
        this.nbPlatform = nbPlatform;
    }
    public int getNumberFloors() {
        return nbPlatform;
    }

    public void setPlatformerDimensions(int nbLevels, Vector2i sizeTile, Vector2i levelSize) {
        this.setNumberFloors(nbLevels);
        this.setPlatformLevelSize(levelSize);
        this.setPlatformTileSize(sizeTile);
        // this.setPlatformerSize(new Vector2i(this.platformLevelSize.x, this.computePlatformerHeight()), false);
        this.setSceneSize(new Vector2i(this.platformLevelSize.x, this.computePlatformerHeight()));
    }

    @Override
    public void setSceneSize(Vector2i size) {
        // this.setPlatformerSize(size, false);
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
        super.setSceneSize(sz);
    }

    /**
     * 
     * @param size
     * @param resetHeight
     * @deprecated use setSceneSize with the sizing options
     */
    @Deprecated(forRemoval = true)
    protected void setPlatformerSize(Vector2i size, boolean resetHeight) {
        Vector2i sz;
        if (!resetHeight) {
            // Ignore the new height
            sz = new Vector2i(size.x, this.computePlatformerHeight());
        } else {
            sz = size;
        }
        super.setSceneSize(sz);
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
    }

    @Override
    public Vector2i getPlatformTileSize() {
        return this.platformTileSize;
    }
    @Override
    public void setPlatformTileSize(Vector2i tileSize) {
        this.platformTileSize = tileSize;
    }
    
}
