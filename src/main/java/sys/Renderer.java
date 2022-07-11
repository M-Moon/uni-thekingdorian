package sys;

/**
 * An object that is able to manage Renderable objects.
 * 
 * This interface can be coupled with the CritterPool to manage game critters.
 * It can also aid in managing other objects that are rendered in the screen,
 * even when they are not game critters (for examples, UI elements such as buttons).
 * RenderControllers help manage objects, but need not render them; 
 * they simply keep track of Renderable objects 
 * 
 */
public interface Renderer 
{
    /**
     * Adds a Renderable to a list for it to be then drawn on screen
     * 
     * @param renderable
     */
    public void addToRenderingList(Renderable renderable);

    public void removeFromRenderingList(Renderable renderable);

    public void render();


}

