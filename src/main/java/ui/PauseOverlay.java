
package ui;

import assets.FontCatalogue;
import controllers.Game;
import controllers.GameState;
import org.jsfml.graphics.*;
import org.jsfml.system.Vector2f;
import org.jsfml.window.Keyboard.Key;
import org.jsfml.window.event.Event;

import Collectibles.keyCollectible;

import java.io.IOException;
import java.nio.file.Paths;

/**
 *  Class that creates an overlay for when the player pauses the game.
 *  The overlay holds tips, story information and the ability to back out to the main menu.
 *
 */
public class PauseOverlay implements Drawable{

    private Button Menu;
    private Button Continue;
    private Sprite background = new Sprite();
    private Sprite instructions = new Sprite();
    private Sprite[] collect = new Sprite[6];
    private Sprite[] avoid = new Sprite[4];
    private Text goal = new Text();
    private Text title;
    private Text subtitle;
    private Text collectText = new Text();
    private Text avoidText = new Text();

    /**
     * Constructor for the PauseOverlay
     * Creates all UI elements necessary as well as fills lists of sprites for tips
     * @param game the main game from where the graphicsManager is accessed
     */
    public PauseOverlay(Game game) {
        RenderWindow window = game.window;

        /////background//////
        background.setTexture(game.GRAPHICS.PANEL_BACKGROUND);
        background.setOrigin(new Vector2f(background.getGlobalBounds().width/2, background.getGlobalBounds().height/2));
        background.scale(7, 7);
        background.setPosition(game.window.getSize().x/2, game.window.getSize().y/2);

        /////UI images///////
        instructions.setTexture(game.GRAPHICS.UI_INSTRUCTIONS);
        instructions.setOrigin(new Vector2f(instructions.getGlobalBounds().width/2, instructions.getGlobalBounds().height/2));
        instructions.scale(1.5f, 1.5f);
        instructions.setPosition(game.window.getSize().x/2 + 260, (game.window.getSize().y/2)-100);

        //////////UI Text/////////
        title = new Text("A few Tips:", FontCatalogue.get().FONT_UI_BAR);
        title.setCharacterSize(40);
        title.setPosition((window.getSize().x/2)-400,(window.getSize().y/2)-260);

        subtitle = new Text("", FontCatalogue.get().FONT_UI_BAR);
        subtitle.setString("Dorian has put a curse on\nthe land, it is down to you to\nfind the key to his lair\nand defeat him, the fate of the\n210 realms lies in your hands.");
        subtitle.setCharacterSize(20);
        subtitle.setPosition((window.getSize().x/2)-400,(window.getSize().y/2)-200);

        goal = new Text("YOUR GOALS:\n -Collect coins, potions and ammo.\n -Avoid Enemies and obstacles.\n -Find the Key to Dorian's Lair\n -Defeat Dorian, good luck", FontCatalogue.get().FONT_UI_BAR);
        goal.setCharacterSize(15);
        goal.setPosition((window.getSize().x/2)-400,(window.getSize().y/2)-60);

        ////////Collectible images///////
        collectText = new Text("Collect these:", FontCatalogue.get().FONT_UI_BAR);
        collectText.setCharacterSize(20);
        collectText.setPosition((window.getSize().x/2)-400, (window.getSize().y/2) + 55);

        for(int i = 0; i < 6; i++){
            collect[i] = new Sprite();
            collect[i].setScale(0.3f, 0.3f);
        }
        collect[0].setTexture(game.GRAPHICS.POTION_TYPE_1);
        collect[0].setScale(2,2);
        collect[1].setTexture(game.GRAPHICS.POTION_TYPE_2);
        collect[1].setScale(2,2);
        collect[2].setTexture(game.GRAPHICS.COIN_SHEET);
        collect[2].setTextureRect(new IntRect(0,0, 160, 160));
        collect[3].setTexture(game.GRAPHICS.WEAPON_BOW);
        collect[4].setTexture(game.GRAPHICS.WEAPON_SWORD);
        collect[5].setTexture(game.GRAPHICS.KEY);
        float x = (window.getSize().x/2)-400;
        float y = (window.getSize().y/2) + 100;
        for(int i = 0; i < 6; i++){
            collect[i].setPosition(x, y);
            x += 80;
            if(i == 2){
                y += 80;
                x -= 240;
            }

        }

        ///////obstacle images////////

        avoidText = new Text("Avoid These:", FontCatalogue.get().FONT_UI_BAR);
        avoidText.setCharacterSize(20);
        avoidText.setPosition((window.getSize().x/2)-100, (window.getSize().y/2) + 55);


        for(int i = 0; i < avoid.length; i++){
            avoid[i] = new Sprite();
            avoid[i].setScale(0.3f, 0.3f);
        }
        avoid[0].setTexture(game.GRAPHICS.TEMP_OBST_FIRE);
        avoid[1].setTexture(game.GRAPHICS.TEMP_OBST_LAVA);
        avoid[2].setTexture(game.GRAPHICS.TEMP_OBST_POISON);
        avoid[3].setTexture(game.GRAPHICS.TEMP_OBST_SPIKE);
        avoid[1].setScale(0.15f, 0.15f);

        x = (window.getSize().x/2)-100;
        y = (window.getSize().y/2) + 100;

        for(int i = 0; i < avoid.length; i++){
            avoid[i].setPosition(x, y);
            x += 100;
            if(i == 1){
                x -= 100 * 2;
                y += 80;
            }
        }

        /////buttons/////////
        Menu = new Button(2, 0.2f, new Vector2f((window.getSize().x/2) + 280, (window.getSize().y/2)+100), "Home", window);
        Continue = new Button(1, 0.2f, new Vector2f((window.getSize().x/2) + 280, window.getSize().y/2 + 200), "Continue", window);

    }

    @Override
    public void draw(RenderTarget renderTarget, RenderStates renderStates) {
        background.draw(renderTarget, renderStates);
        title.draw(renderTarget, renderStates);
        subtitle.draw(renderTarget, renderStates);
        instructions.draw(renderTarget, renderStates);
        goal.draw(renderTarget, renderStates);
        collectText.draw(renderTarget, renderStates);
        for(int i = 0; i < 6; i++){
            collect[i].draw(renderTarget, renderStates);
        }
        avoidText.draw(renderTarget, renderStates);
        for(int i = 0; i < avoid.length; i++){
            avoid[i].draw(renderTarget, renderStates);
        }

        Menu.draw(renderTarget, renderStates);
        Continue.draw(renderTarget, renderStates);
    }

    /**
     * Event handler for the UI elements such as buttons.
     *
     *
     * @param event event to check against
     * @param game the main game loop
     */
    public void HandleEvent(Event event, Game game){
        if (event.type == Event.Type.CLOSED) {
            game.window.close();
        }

        if (event.type == Event.Type.KEY_PRESSED) {
            if (event.asKeyEvent().key==Key.P) {
                game.setGameState(GameState.PLAY);
            }
        }
        if(event.type == Event.Type.MOUSE_BUTTON_PRESSED){
            if(Menu.press()){
                game.setGameState(GameState.HOME_SCREEN);
                Menu.release();
            }
            if(Continue.press()){
                game.setGameState(GameState.PLAY);
                Continue.release();
            }
        }

    }


}
