package screens;


import assets.FontCatalogue;

import org.jsfml.audio.Music;
import org.jsfml.graphics.*;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;
import org.jsfml.window.Keyboard.Key;
import org.jsfml.window.event.Event;

import controllers.Game;
import controllers.GameState;
import sys.Renderable;
import sys.Renderer;
import ui.Button;
import ui.TextBox;

/**
 * Class that creates a Renderable homescreen to show at the start of the game
 * This includes a title, subtitle and buttons aswell as an instructions image.
 *
 *
 */
public class HomeScreen implements Renderable {
    public final Game controller;

    private boolean rendered;
    private int layer = 0;
    Vector2f position;
    Vector2i size;

    public final Sprite background = new Sprite();
    protected Texture[] heroTextures = new Texture[3];
    // private int chosenHero = 0;

    private Sprite instructions = new Sprite();
    private Text title;
    private Text subTitle;
    private TextBox synopsis = new TextBox();
    private Button startButton;
    private Button exitButton;

    // private Music introMusic;

    /**
     * Constructor for the Homescreen
     * initialises all UI elements and sprites.
     *
     *
     * @param game the main game loop from which to source the graphics catalogue.
     */
    public HomeScreen(Game game) {
        this.controller = game;
        this.background.setTexture(controller.GRAPHICS.HOMESCREEN_BACKGROUND);
        this.background.scale((float)game.window.getSize().x/130,(float)game.window.getSize().y/90);

        final Vector2f sizeArea = new Vector2f(1200,680);
        Vector2f winSize = new Vector2f(game.window.getSize());
        final Vector2f positionArea = Vector2f.div(Vector2f.sub(winSize,sizeArea),2);

        // System.out.println("BEFORE we load the fonts");
        Font ui_font = FontCatalogue.get().FONT_UI_BAR;
        this.title = new Text("Dorian's Key", ui_font, 100);
        title.setPosition( (controller.window.getSize().x - title.getGlobalBounds().width)/2,
        positionArea.y + 50);
        title.setColor(new Color(251, 205, 66));

        instructions.setTexture(controller.GRAPHICS.UI_INSTRUCTIONS);
        instructions.setScale(2,2);
        instructions.setPosition(20, 380);

        subTitle = new Text("Can you save us all?", ui_font, 50);
        subTitle.setPosition((controller.window.getSize().x - subTitle.getGlobalBounds().width)/2,
                this.title.getPosition().y + title.getGlobalBounds().height + 50);
        try {
            this.startButton = new Button(1, 0.3f, new Vector2f(game.window.getSize().x/2f, 550), "Play", controller.window);
            this.exitButton = new Button(1, 0.3f, new Vector2f(game.window.getSize().x/2f, 700), "Leave", controller.window);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // String text = "The universe is in peril as Dorian prepares to invade the remaining three free realms, and his evil knows no bounds." + 
        //     "Only the Master Key can stop him in his track, and it just so happens that Dorian lost it in one of the realms during "+
        //     "a recent crusade. You must retrieve it, and fast, Before it is too late.";
        // this.synopsis = new TextBox(text, FontCatalogue.get().FONT_FREESANS, 25, new Vector2f(500,300));
        // this.synopsis.setPosition(110,500);

    }

    @Override
    public void draw(RenderTarget arg0, RenderStates arg1) {
        background.draw(arg0, arg1);
        instructions.draw(arg0, arg1);
        this.title.draw(arg0, arg1);
        this.subTitle.draw(arg0, arg1);
        this.startButton.draw(arg0, arg1);
        this.exitButton.draw(arg0, arg1);
        // this.synopsis.draw(arg0, arg1);

    }

    @Override
    public boolean isBeingRendered() {
        return rendered;
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
        background.setTexture(texture);
    }

    @Override
    public int getLayer() {
        return layer;
    }

    @Override
    public void setLayer(int layer) {
        this.layer = layer;
    }

    @Override
    public void addToRenderer(Renderer renderer) {
        renderer.addToRenderingList(this);
    }

    @Override
    public void removeFromRenderer(Renderer renderer) {
        renderer.removeFromRenderingList(this);
    }

    @Override
    public Vector2f getPosition() {
        return null;
    }

    @Override
    public void setPosition(Vector2f position) {
        this.position = position;

    }

    @Override
    public final void setPosition(float x, float y) {
        this.setPosition(new Vector2f(x, y));
    }

    public void handleEvent(Event event) {
        switch (event.type) {
            case CLOSED:
                this.controller.window.close();
                break;
            case KEY_PRESSED:
                if (event.asKeyEvent().key == Key.RETURN) {
                    // this.controller.startGame(null);
                    this.controller.setGameState(GameState.PLAY);
                    // controller.soundManager.stopPlaying();
                }
                // System.out.println("Key pressed " + event.asKeyEvent().key);
                // this.controller.startGame(null);

                break;
            case MOUSE_BUTTON_PRESSED:
                System.out.println("Position " + event.asMouseButtonEvent().position);
                if (startButton.press()) {
                    // this.controller.startGame(null);
                    this.controller.setGameState(GameState.PLAY);
                    System.out.println("Game starting");
                    startButton.release();

                }
                if (exitButton.press()) {
                    // System.out.println("Exiting");
                    this.controller.window.close();
                }
                break;
            default:
                break;
        }
    }
    
}
