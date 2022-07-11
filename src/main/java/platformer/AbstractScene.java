package platformer;

import java.util.LinkedList;
import java.util.Queue;

import org.jsfml.graphics.Sprite;
import org.jsfml.graphics.Texture;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;

import sys.Renderable;
import sys.Renderer;
import views.*;

/**
 * Abstract class encapsulating the functionality of a scrollable scene.
 * 
 * A scene typically is a graphical entity comprised of a background (itself) and other SceneElements
 * which scrolls.
 * How Scene elements are rendered is up to the programmer:
 * they may choose to add scene elements to the renderer from this class or otherwise.
 * 
 * @author Lydie Toure
 */
public abstract class AbstractScene implements Renderable, Scrollable
{
    protected final ScrollableSprite background;
    protected final Queue<SceneElement> sceneElements;
    protected Vector2i sceneSize;

    protected AbstractScene() {
        this.background = new ScrollableSprite();
        this.sceneElements = new LinkedList<>();
    }

	@Override
    public final void scroll(Vector2f speed) {
        this.scroll(speed.x, speed.y);
    }
    
    @Override
    public final void setPosition(Vector2f position) {
       this.setPosition(position.x, position.y);
    }

	@Override
	public boolean isBeingRendered() {
		return true;
	}

	@Override
	public Sprite getSprite() {
		return background;
	}

	@Override
	public void setTexture(Texture texture) {
        this.background.setTexture(texture);
    }
    
    public void setSceneSize(Vector2i sz) {
        this.sceneSize = sz;
        this.background.setSize(this.sceneSize);
        
    }

    public final void setSceneSize(int x, int y) {
        this.setSceneSize(x, y);
    }

    public Vector2i getSceneSize() {
        return this.sceneSize;
    }

    /**
     * Adds a critter to the end of this Scene to one of the three platform levels
     * 
     * @param critter the critter to append to the scene
     */
    public abstract void appendToScene(SceneElement element);
    
    /**
     * Updates the scene on the given renderer.
     * 
     * Call this method periodically during the rendering process to
     * keep up to date with changes in the various elements which constitute this AbstractScene
     * 
     * @param renderer the object on which to render this AbstractScene
     */
    public abstract void updateScene(Renderer renderer);

	
}
