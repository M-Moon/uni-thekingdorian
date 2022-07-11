package platformer;

import org.jsfml.system.Vector2f;

/**
 * An interface for game objects (Critters) which are meant to scroll
 * 
 * @author Lydie Toure
 */
public interface Scrollable
{
    /**
     * Makes the Texture scroll
     * 
     * @param dx the amount to scroll by in the horizontal (left to right) direction
     * @param dy the amount to scroll by in the vertical (top to bottom) direction
     */
    public void scroll(float dx, float dy);

    public void scroll(Vector2f speed);
}
