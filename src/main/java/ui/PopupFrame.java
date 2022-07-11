package ui;

import org.jsfml.graphics.*;
import org.jsfml.graphics.Sprite;
import org.jsfml.graphics.Texture;
import org.jsfml.system.*;
import org.jsfml.window.event.Event;

import assets.FontCatalogue;
import sys.*;

/**
 * A fix sized panel which does not handle any events.
 * 
 */
public class PopupFrame extends PanelOverlay implements Renderable {

    public static final Vector2f DEFAULT_POPUP_SIZE = new Vector2f(450,120);
    public static final Vector2f DEFAULT_POPUP_POSITION = new Vector2f(250,0);

    protected boolean rendered;
    protected int layer;

    protected Text title = new Text();

    public PopupFrame() {
        super();
    }

    public PopupFrame(Vector2f size, Vector2f position) {
        super(size, position);
        this.addComponent(title);
    }

    public PopupFrame(RenderWindow window, String text) {
        super(window, new Vector2f(400,120), new Vector2f(250,0));
        // Title in the middle
        this.title = new Text(text, FontCatalogue.get().FONT_UI_BAR, 20);
        Vector2f titlePos = new Vector2f(size.x/2f-title.getGlobalBounds().width/2f, 10f);
        this.title.setPosition(mapPanelToWindow(titlePos));
        // this.title.setColor(Color.YELLOW);
        System.out.println("Panel dimensions " + this.getGlobalBounds() );
        System.out.println("Panel dimensions " + title.getGlobalBounds() );

        this.addComponent(title);
    }

    /**
     * Sets the title of this frame with the given parameters.
     * The font used is the default UI font.
     * 
     * @param text the title of of the frame
     * @param position the position (in the frame) of the title; if null, it is placed at the center
     * @param characterSize the character size of the text
     */
    public void setTitle(String text, Vector2f position, int characterSize) {
        this.title = new Text(text, FontCatalogue.get().FONT_UI_BAR, characterSize);
        Vector2f titlePos = position==null ? new Vector2f(size.x/2f-title.getGlobalBounds().width/2f, 10f) : position;
        this.title.setPosition(mapPanelToWindow(titlePos));
        this.removeComponent(title);
        this.addComponent(title);
        // System.out.println("Position of title: " + title.getGlobalBounds());
    }

    @Override
    public void handleEvent(Event event) {
        // No events in a pop-up
    }

    @Override
    public boolean isBeingRendered() {
        return this.rendered;
    }

    @Override
    public void startRendering(boolean rendered) {
        this.rendered = rendered;
    }

    @Override
    public Sprite getSprite() {
        return background;
    }

    @Override
    public void setTexture(Texture texture) {
        this.setTexture(texture);
    }

    @Override
    public int getLayer() {
        return this.layer;
    }

    @Override
    public void setLayer(int layer) {
        this.layer = layer;
    }

    @Override
    public void addToRenderer(Renderer renderer) {
        renderer.addToRenderingList(this);
        this.startRendering(true);
    }

    @Override
    public void removeFromRenderer(Renderer renderer) {
        renderer.removeFromRenderingList(this);
        this.startRendering(false);
    }

    @Override
    public Vector2f getPosition() {
        return this.position;
    }
    
}
