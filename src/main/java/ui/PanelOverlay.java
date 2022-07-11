package ui;

import java.util.ArrayList;
import java.util.List;

import org.jsfml.graphics.*;
import org.jsfml.system.*;
import org.jsfml.window.event.Event;

/** 
 * Create a panel which can be rendered on a window.
 * <p>
 * Components of the panels must be drawable, and they should be 
 * placed in relation to the window on which the panel is drawn.
 * Therefore, to add a text in the middle of the panel, the position must be translated 
 * by the position of the panel - see {@link #mapPanelToWindow(Vector2f)}
 * 
 */
public abstract class PanelOverlay implements Overlay
{

    protected final Sprite background = new Sprite();
    protected RenderWindow window;

    protected List<Drawable> components = new ArrayList<>();
    
    // Position
    protected Vector2f position = Vector2f.ZERO;
    protected Vector2f size = Vector2f.ZERO;

    protected boolean upToDate = false;

    /**
     * Creates a new panel to be rendered on a given window
     */
    protected PanelOverlay(RenderWindow window, Vector2f size) {
        this.window = window;
        this.size = size;

        // Set the view
    }

    protected PanelOverlay(RenderWindow window, Vector2f size, Vector2f position) {
        this.window = window;
        this.size = size;
        this.setPosition(position);
        // Set the view
    }

    protected PanelOverlay() {}

    protected PanelOverlay(Vector2f size, Vector2f position) {
        this.setSize(size);
        this.setPosition(position);
    }

    
	@Override
    public void draw(RenderTarget arg0, RenderStates arg1) {
        // arg0.setView(this.view);

        this.background.draw(arg0, arg1);
        this.components.forEach(drawable -> drawable.draw(arg0, arg1));;
    }

    public void setPosition(Vector2f position) {
        this.position = position;
        this.upToDate = false;
        this.background.setPosition(position);
    }
    public final void setPosition(float x, float y) {
        this.setPosition(new Vector2f(x,y));
    }

    public void setSize(Vector2f size) {
        this.size = size;

        // Vector2f bgSz = new Vector2f(background.getGlobalBounds().width, background.getGlobalBounds().height);
        // this.background.scale(Vector2f.componentwiseDiv(size, bgSz));
    }

    public void addComponent(Drawable component) {
        this.components.add(component);
    }

    public void removeComponent(Drawable component) {
        this.components.remove(component);
    }

    public void setBackground(Texture texture) {
        this.background.setTexture(texture);
        
        Vector2f bgSz = new Vector2f(background.getGlobalBounds().width, background.getGlobalBounds().height);
        this.background.scale(Vector2f.componentwiseDiv(size, bgSz));
    }

    public FloatRect getGlobalBounds() {
        return new FloatRect(position,size);
    }

    /**
     * Maps coordinates in the panel to coordinates in the window
     * 
     * @param position
     * @return
     */
    public Vector2f mapPanelToWindow(Vector2f position){
        return Vector2f.add(this.position, position);
    }

    public Vector2f mapWindowToPanel(Vector2f win) {
        return Vector2f.sub(win, position);
    }
    
}
