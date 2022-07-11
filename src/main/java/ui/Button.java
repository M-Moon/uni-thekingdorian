package ui;

import org.jsfml.graphics.*;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;
import org.jsfml.window.Mouse;

import assets.FontCatalogue;
import assets.TextureCatalogue;
import devtools.Lambda;

import java.io.IOException;
import java.nio.file.Paths;

import java.nio.file.Paths;

/**
 * Button class that creates drawable button UI elements
 * They animate when pressed with the press() method
 * and return to idle when release() is called
 *
 */
public class Button implements Drawable
{
    private static TextureCatalogue TEXTURES = TextureCatalogue.get();

    public static final int LARGE_BUTTON = 1;
    public static final int MEDIUM_BUTTON = 2;
    public static final int SMALL_BUTTON = 3;

    public static final Vector2i[] BUTTON_SIZES = { new Vector2i(960, 320), new Vector2i(640, 320),
        new Vector2i(320, 320) };

    private Texture ButtonTexture = new Texture();
    private Texture ButtonPressTexture = new Texture();
    
    
    public Sprite ButtonSprite;
    private Texture[] buttonTextures = new Texture[2]; //0=OFF, 1=ON(pressed)
    private RenderWindow window;
    private Text ButtonText;
    private boolean isPressed;
    private Font font = new Font();

    protected Lambda onPressAction;

    /**
     * Creates a button (Large, medium or small) at the given position
     * The available sizes range from large to small, respectively: 960x320 640x320 320x320
     * 
     * @param type the type of button from large (1) to small (3)
     * @param scale the scale given to the button
     * @param position the position of the center of the button
     * @param text the text displayed on the button
     * @param window the window in which the button is drawn
     * 
     */
    public Button(int type, float scale, Vector2f position, String text, RenderWindow window)  {
        
        switch (type){
            case 1:
                buttonTextures[0] = TEXTURES.UI_BUTTON_LARGE;
                buttonTextures[1] = TEXTURES.UI_BUTTON_LARGE_PRESSED;
                
                // ButtonTexture.loadFromFile(Paths.get("resources/UI/LargeButton.png"));
                // ButtonPressTexture.loadFromFile(Paths.get("resources/UI/LargeButtonPress.png"));
                // ButtonSprite = new Sprite(buttonTextures[0], new IntRect(0, 0, 960, 320));
                break;
            case 2:
                buttonTextures[0] = TEXTURES.UI_BUTTON_MEDIUM;
                buttonTextures[1] = TEXTURES.UI_BUTTON_MEDIUM_PRESSED;
                // ButtonTexture.loadFromFile(Paths.get("resources/UI/MediumButton.png"));
                // ButtonPressTexture.loadFromFile(Paths.get("resources/UI/MediumButtonPress.png"));
                // ButtonSprite = new Sprite(buttonTextures[0], new IntRect(0, 0, 640, 320));
                break;
            case 3:
                buttonTextures[0] = TEXTURES.UI_BUTTON_SMALL;
                buttonTextures[1] = TEXTURES.UI_BUTTON_SMALL_PRESSED;
                // ButtonTexture.loadFromFile(Paths.get("resources/UI/SmallButton.png"));
                // ButtonPressTexture.loadFromFile(Paths.get("resources/UI/SmallButtonPress.png"));
                // ButtonSprite = new Sprite(buttonTextures[0], new IntRect(0, 0, 320, 320));
                break;
            default: break;
        }

        ButtonSprite = new Sprite(buttonTextures[0], new IntRect(Vector2i.ZERO, BUTTON_SIZES[type-1]));



        ButtonSprite.setOrigin((ButtonSprite.getGlobalBounds().width/2), (ButtonSprite.getGlobalBounds().height/2));
        ButtonSprite.setScale(scale, scale);
        ButtonSprite.setPosition(position);
        // font.loadFromFile(Paths.get("resources/UI/Font.ttf"));
        ButtonText = new Text(text, FontCatalogue.get().FONT_UI_BAR, (int)(100 * scale));
        ButtonText.setOrigin((ButtonText.getGlobalBounds().width/2),ButtonText.getGlobalBounds().height/2);
        ButtonText.setPosition(position.x, position.y - ButtonSprite.getGlobalBounds().height/8);
        this.window = window;
    }

    /**
     * Method that when called checks the mouse position against the bounds
     * the button and checks if it has been pressed.
     *
     * @return true if pressed, false if not.
     */
    public boolean press(){
        if(ButtonSprite.getGlobalBounds().contains(Mouse.getPosition(window).x, Mouse.getPosition(window).y)){
            // ButtonSprite.setTexture(ButtonPressTexture);
            ButtonSprite.setTexture(buttonTextures[1]);
            ButtonText.setPosition(ButtonText.getPosition().x, (ButtonText.getPosition().y + (ButtonSprite.getGlobalBounds().height/16)));
            this.isPressed = true;
            return true;
        }
        else{
            // this.isPressed = true;
            return false;
        }
    }


    /**
     * Method that checks if the button has been released. if so reverts the animation to idle.
     *
     *
     */
    public void release(){
        if(ButtonSprite.getGlobalBounds().contains(Mouse.getPosition(window).x, Mouse.getPosition(window).y)) {
            // ButtonSprite.setTexture(ButtonTexture);
            ButtonSprite.setTexture(buttonTextures[0]);
            ButtonText.setPosition(ButtonText.getPosition().x, (ButtonText.getPosition().y - (ButtonSprite.getGlobalBounds().height/16) ));
        }
        this.isPressed = false;
    }

    public boolean isPressed() {
        return isPressed;
    }

    /**
     * Obtains the action to be undertaken when the button is pressed
     */
    public Lambda getAction() {
        return onPressAction;
    }

    public void setAction(Lambda action) {
        this.onPressAction = action;
    }

    @Deprecated
    public boolean buttonPress(Vector2f position) {
        if(ButtonSprite.getGlobalBounds().contains(position)){
            ButtonSprite.setTexture(ButtonPressTexture);
            ButtonText.setPosition(ButtonText.getPosition().x, (ButtonText.getPosition().y + (ButtonSprite.getGlobalBounds().height/16)));
            this.isPressed = true;
            return true;
        }
        else{
            this.isPressed = false;
            return false;
        }
        // return isPressed;
    }
    @Deprecated
    public void buttonRelease(Vector2f position) {
        if(ButtonSprite.getGlobalBounds().contains(position)) {
            ButtonSprite.setTexture(ButtonTexture);
            ButtonText.setPosition(ButtonText.getPosition().x, (ButtonText.getPosition().y - (ButtonSprite.getGlobalBounds().height/16) ));
        }
    }
    
    /**
     * Sets the position of the top left corner of this Button
     * @param position
     */
    public void setCornerPosition(Vector2f position) {
        Vector2f taille = new Vector2f(ButtonSprite.getGlobalBounds().width, ButtonSprite.getGlobalBounds().height);
        Vector2f origin = Vector2f.add(position, Vector2f.div(taille,2f));
        // ButtonSprite.setOrigin((ButtonSprite.getGlobalBounds().width/2), (ButtonSprite.getGlobalBounds().height/2));
        // ButtonSprite.setScale(scale, scale);
        ButtonSprite.setPosition(origin);

        float scale = ButtonSprite.getScale().x;
        ButtonText.setCharacterSize((int)(100*scale));
        ButtonText.setPosition(origin.x, origin.y - ButtonSprite.getGlobalBounds().height/8);
    }

    /*public void draw(){
        window.draw(ButtonSprite);
        window.draw(ButtonText);
    }*/

    @Override
    public void draw(RenderTarget renderTarget, RenderStates renderStates) {
       ButtonSprite.draw(renderTarget, renderStates);
       ButtonText.draw(renderTarget, renderStates);
    }
}