package npc;

import java.util.ArrayList;
import java.util.List;

import org.jsfml.graphics.*;
import org.jsfml.system.*;
import org.jsfml.window.event.Event;

import assets.FontCatalogue;
import controllers.Game;
import devtools.Lambda;
import sys.Renderable;
import sys.Renderer;
import ui.Button;
import ui.Overlay;
import ui.PanelOverlay;
import ui.TextBox;

/**
 * A UI Overlay which presents a basic fixed dialog box with an icon on the right, a cancel button,
 * an (possibly) other buttons as well as a title and a dialog box
 */
public abstract class Zone extends PanelOverlay implements Renderable {

    private static final FontCatalogue FONTS = FontCatalogue.get();
    public static final Vector2f ZONE_SIZE = new Vector2f(900,500);

    public static final Vector2f ICON_SIZE = new Vector2f(125,400);
    
    private static final int BUTTON_TYPE = 2;
    private static final float SCALE = 0.25f;

    /**
     * The character whose zone it is
     */
    protected NPC character;


    protected Sprite icon;
    protected Text title;
    protected TextBox dialog;

    protected RectangleShape border;
    protected boolean withBorder = true;
    protected float borderWidth = 5f;

    protected boolean rendered;
    protected int layer;

    protected Button[] options;
    private int iterator = 0;       // last index
    protected int numberOfOptions;

    protected Zone(Game controller, NPC npc, int nbOptions) {
        super(controller.window, ZONE_SIZE, Vector2f.div(
            Vector2f.sub(new Vector2f(controller.window.getSize()),ZONE_SIZE), 2f));
        this.setLayer(6);

        this.character = npc;
        options = new Button[nbOptions];
        numberOfOptions = nbOptions;

        // Draw the icon
        // this.icon = new Sprite(this.character.getSprite().getTexture());
        // this.icon.scale(125f/icon.getGlobalBounds().width, 400f/icon.getGlobalBounds().height);
        // // this.icon.setPosition(this.mapPanelToWindow(new Vector2f(size.x - 95, (size.y - 400)/2f)));
        // this.icon.setPosition(940,250);
        // System.out.println("Zone icon " + icon.getGlobalBounds() );
        this.setBackground(controller.GRAPHICS.PANEL_BACKGROUND);
        
        // Draw the cancel button
        this.options[numberOfOptions-1] = new Button(BUTTON_TYPE,SCALE,
            mapPanelToWindow(new Vector2f(size.x*0.75f-Button.BUTTON_SIZES[1].x*0.125f - 25f, size.y*4f/5f)), 
            "Return", controller.window);

        // Draw the title
        this.title = new Text();
        this.title.setCharacterSize(50);
        this.title.setFont(FONTS.FONT_UI_BAR);
        this.title.setPosition(this.mapPanelToWindow(new Vector2f(180,40)));

        // Create the dialog box
        this.dialog = new TextBox();

        // Add everythin
        this.addComponent(title);
        this.addComponent(dialog);
        // this.addComponent(icon);
        this.addComponent(options[options.length-1]);
    }

    protected Zone(Game game, NPC npc) {
        this(game,npc,3);
    }

    public void setZoneTitle(String title) {
        this.title.setString(title);
    }

    public void setDialog(String dialogText) {
        this.dialog = new TextBox(dialogText, FONTS.FONT_FREESANS, 30, new Vector2f(600,size.y*3/5));
        this.dialog.setPosition(this.mapPanelToWindow(new Vector2f(80,120)));
        this.components.remove(dialog);
        this.components.add(dialog);
    }

    public void setIcon(Texture texture) {
        this.icon = new Sprite(texture);
        this.icon.scale(125f/icon.getGlobalBounds().width, 400f/icon.getGlobalBounds().height);
        // this.icon.setPosition(this.mapPanelToWindow(new Vector2f(size.x - 95, (size.y - 400)/2f)));
        this.icon.setPosition(940,250);
        // System.out.println("Zone icon " + icon.getGlobalBounds() );
        this.components.remove(icon);
        this.components.add(icon);
    }

    /**
     * 
     * @param buttonLabel
     * @param action action to be undertaken when the button is pressed
     */
    public void addOption(String buttonLabel, Lambda action) {
        if (iterator==options.length-1) return;
        float x = 60 + (2*iterator + 1) * 400f*0.5f/(options.length-1);
        Vector2f pos = mapPanelToWindow(new Vector2f(x, size.y*4/5f));
        options[iterator] = new Button(2, 0.25f, pos, buttonLabel, this.window);
        options[iterator].setAction(action); 
        // System.out.println("Option " + iterator + ": " + pos);
        this.addComponent(options[iterator]);
        iterator++;
    }

    public void onCancel(Lambda actionOnCancel) {
        this.options[options.length-1].setAction(actionOnCancel);
    }

    public void handleEvent(Event event) {
        if (!this.rendered) {
            return;
        }
        if (event.type == Event.Type.CLOSED) {
            window.close();
        }

        if (event.type == Event.Type.MOUSE_BUTTON_PRESSED) {
            for (Button button : options) button.press();
        }

        if (event.type == Event.Type.MOUSE_BUTTON_RELEASED) {
            for (Button button : options) if (button.isPressed()) //button.getAction().compose(button::release);
            {
                button.release();
                button.getAction().run();
            }
        }
    }

    

    // public void setSize(Vector2f size) {
    //     final Vector2f bounds = new Vector2f(background.getGlobalBounds().width, background.getGlobalBounds().height);
    //     this.background.scale(Vector2f.componentwiseDiv(size, bounds));
    //     this.border.setSize(Vector2f.add(size, new Vector2f(2*borderWidth, 2*borderWidth)));
    //     this.size = size;
    // }

    // public void setPosition(Vector2f position) {
    //     this.position = position;
    //     this.background.setPosition(position);
    // }

    // public final void setPosition(float x, float y) {
    //     this.setPosition(new Vector2f(x, y));
    // }

    // @Override
    // public void draw(RenderTarget arg0, RenderStates arg1) {
    //     if (withBorder) this.border.draw(arg0, arg1);
    //     this.background.draw(arg0, arg1);
    // }

    // public void setBorderColor(Color borderColor) {
    //     this.border.setFillColor(borderColor);
    // }

    // public void hasBorder(boolean bordered) {
    //     this.withBorder = bordered;
    // }

    // public void setBorderWidth(float thickness) {
    //     this.borderWidth = thickness;
    //     this.border.setPosition(Vector2f.sub(this.position, new Vector2f(borderWidth, borderWidth)));
    // }

    // public void setBorder(Color color, float width) {
    //     this.border.setSize(Vector2f.add(size, new Vector2f(2*borderWidth, 2*borderWidth)));
    //     this.border.setFillColor(color);
    //     this.setBorderWidth(width);
    // }

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
