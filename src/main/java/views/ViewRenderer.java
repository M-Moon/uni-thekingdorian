package views;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.jsfml.graphics.RenderTarget;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.View;

import sys.Renderable;
import sys.Renderer;

/**
 * A renderer tasked with rendering a specific view on a window. One such view may be the UI View, the Platformer View,
 * the Home Screen view, etc. ViewRenderers only display a specific aspect of the game.
 */
public class ViewRenderer implements Renderer
{
    public final View view;
    public final RenderWindow window;

    private List<Renderable> renderingList = new ArrayList<>();

    // Whether this view is currently drawn on screen
    private boolean visible = false;

    public ViewRenderer(RenderWindow window, View view) {
        this.window = window;
        this.view = view;
    }

    /**
     * Sets the visibility switch for the view. 
     * 
     * If the given argument is true, then the elements in this rendering list will be 
     * rendered on the window.
     * 
     * @param visible whether the elements in this scene should be rendered
     */
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isVisible() {
        return visible;
    }

    @Override
    public void addToRenderingList(Renderable renderable) {
        // util.GameLogger.get().info("msg");
        // System.out.println("Adding " + renderable);
        this.renderingList.add(renderable);
    }

    @Override
    public void removeFromRenderingList(Renderable renderable) {
        this.renderingList.remove(renderable);
    }

    public void render(RenderTarget window) {
        if (!visible) {
            return;
        }
        // System.out.println("Rendering");
        window.setView(this.view);
        this.renderingList.sort(Comparator.comparing(Renderable::getLayer));
        for (Renderable drawable : this.renderingList) {
            window.draw(drawable);
        }
    }

    @Override
    public void render() {
        this.render(this.window);
    }
    
}
