package screens;

// import assets.TextureCatalogue;
import assets.FontCatalogue;
import controllers.Game;
import controllers.GameState;
import models.Critter;
import org.jsfml.graphics.*;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;
import org.jsfml.window.event.Event;
import sys.Renderable;
import sys.Renderer;
import ui.Button;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/**
 * The Deathscreen shows after the player dies, it displays their score and highScore as
 * well as the chance to retry or back out into the menu.
 * it implements renderable and so the whole UI layout can be drawn on screen using the single draw() method.
 *
 * @Author Ben Tomczyk
 */
public class DeathScreen implements Renderable {
    public final Game controller;

    private boolean rendered;
    private int layer = 0;
    Vector2f position;
    Vector2i size;

    private Sprite background = new Sprite();


    private Text title;
    private Text scoretext;
    private Text highScoreText;
    private Button retry;
    private Button menu;

    private int score;
    private int highScore = 0;

    /**
     * Constructor for the Death Screen.
     *
     * Initialises all UI elements, as well as Score and high Score.
     *
     * @param game the Game Loop it is part of, from here it can source the hero and other game elements.
     */
    public DeathScreen(Game game) {
        this.controller = game;

        this.background.setTexture(controller.GRAPHICS.DEATHSCREEN_BACKGROUND);
        background.setScale((float)game.window.getSize().y/1080, (float)game.window.getSize().y/1080);

        this.title = new Text("YOU DIED", assets.FontCatalogue.get().FONT_UI_BAR, 100);
        title.setOrigin(title.getGlobalBounds().width/2,title.getGlobalBounds().height/2);
        title.setPosition(game.window.getSize().x/2, 200);
        title.setColor(Color.RED);

        try {
            this.retry = new Button(2, 0.3f, new Vector2f(game.window.getSize().x/2, 500), "Retry?", controller.window);
            this.menu = new Button(2, 0.3f, new Vector2f(game.window.getSize().x/2, 60), "Menu", controller.window);

        } catch (Exception e) {
            //
        }

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

    @Override
    public void draw(RenderTarget x, RenderStates y) {
        background.draw(x,y);
        retry.draw(x,y);
        menu.draw(x,y);
        title.draw(x,y);
        scoretext.draw(x, y);
        highScoreText.draw(x, y);

    }

    /**
     * Event handler for Death Screen.
     * Handles button presses and releases
     *
     *
     * @param event GameEvent that is to be checked
     */
    public void handleEvent(Event event) {
        switch (event.type) {
            case CLOSED:
                this.controller.window.close();
                break;
            case KEY_PRESSED:
                // System.out.println("Key pressed " + event.asKeyEvent().key);
                // this.controller.startGame(null);

                break;
            case MOUSE_BUTTON_PRESSED:
                if (retry.press()) {
                    this.controller.restart();
                    System.out.println("Game restarting");
                    retry.release();
                }
                if (menu.press()) {
                    System.out.println("Menu");
                    controller.setGameState(GameState.HOME_SCREEN);
                    menu.release();
                }
                break;
            default:
                break;
        }
    }

    /**
     * Method to update the score text on the screen.
     * Sources Score from the main Game and
     * displays it on the screen.
     *
     */
    public void updateScore(){
        score = controller.getScore();
        System.out.println(score);
        scoretext = new Text(("Score: " + score), FontCatalogue.get().FONT_UI_BAR, 40);
        scoretext.setOrigin(scoretext.getGlobalBounds().width/2, scoretext.getGlobalBounds().height/2);
        scoretext.setPosition(controller.window.getSize().x/2, 300);
    }

    /**
     * Method for sourcing the high score from the high score
     * file and checking if current score is the new high score,
     * if so writes the new score to the file.
     *
     * if no high score file exists creates a new one.
     *
     */
    public void highScoreCheck(){
        try{
            File highScoreFile = new File("highScore.txt");
            if(highScoreFile.createNewFile()){
                System.out.println("Created File");
                FileWriter writer = new FileWriter(highScoreFile);
                writer.write("" + score);
                highScore = score;
                writer.close();
            }
            else{
                Scanner reader = new Scanner(highScoreFile);
                String output = reader.nextLine();
                highScore = Integer.parseInt(output);
                System.out.println(output);
                reader.close();
                System.out.println(highScore);
                if(highScore < score){
                    highScore = score;
                    FileWriter writer = new FileWriter(highScoreFile);
                    writer.write("" + highScore);
                    writer.close();

                }
            }
            highScoreText = new Text("HIGHSCORE: " + highScore, FontCatalogue.get().FONT_UI_BAR, 40);
            highScoreText.setOrigin(highScoreText.getGlobalBounds().width/2, highScoreText.getGlobalBounds().height/2);
            highScoreText.setPosition(controller.window.getSize().x/2, 380);

        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
}
