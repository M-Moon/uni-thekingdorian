package sys;

import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.Sprite;
import org.jsfml.graphics.Texture;
import org.jsfml.system.Vector2f;

/**
 * An object that can be rendered on the screen.
 * 
 * This interface also provides ways to check whether the object is being rendered. 
 * This is useful for objects which appear on the screen,
 * regarless of whether or not they are interactible (such as UI elements).
 * Essentially, this interface wraps the Drawable interface and can be further used with 
 * a RenderController for management.
 * 
 *
 */
public interface Renderable extends Drawable
{
    /**
     * 
     * @return true only if this Renderable is being rendered on the screen
     */
    public boolean isBeingRendered();

    public void startRendering(boolean rendered);

    public Sprite getSprite();

    /**
     * Sets the texture for this Renderable to be drawn with
     * 
     * @param texture the texture for this object
     */
    public void setTexture(Texture texture);

    /**
     * Obtains the layer at which this Renderable is drawn.
     * 
     * Lower layers are drawn first, and higher layers have their textures 
     * drawn on top of them
     * @return the layer for this Renderable
     */
    public int getLayer();

    public void setLayer(int layer);

    /**
     * Adds this Renderable object to a Renderer
     * for it to manage it.
     * 
     * @param renderer the manager of this Renderable object
     */
    public void addToRenderer(Renderer renderer);

    public void removeFromRenderer(Renderer renderer);


    public Vector2f getPosition();

    public void setPosition(Vector2f position);

    public void setPosition(float x, float y);


    
}
