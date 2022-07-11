package views;

import org.jsfml.graphics.FloatRect;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.View;
import org.jsfml.system.Vector2f;

/**
 * A class which wraps a View object to make it simpler to manipulate on the scene.
 * 
 */
public interface ViewWrapper 
{
    public View getView();

    public void setViewPosition(Vector2f position);
    public Vector2f getViewPosition();

    public void setViewSize(Vector2f size);
    public Vector2f getViewSize();

    public void setSceneCenter(Vector2f center);
    public Vector2f getSceneCenter();

    /**
     * Defines the dimensions of the view through its rectangle (typically in a window).
     * 
     * @param rectangle
     */
    public void setViewRectangle(FloatRect rectangle);
    public void setViewRectangle(Vector2f position, Vector2f size);

    public void setTargetViewPort(RenderWindow window);
    
    /**
     * This method resets a view and places it on the given window.
     * It calls {@link #setViewRectangle(FloatRect)} to give this view its dimensions
     * (a center, a size) and then sets its target port onto the given window
     * 
     * @param rectangle defines the dimensions of the view
     * @param window the window onto which the view is defined
     */
    public void setTargetViewPort(FloatRect rectangle, RenderWindow window);

    
}
