package ui;

import controllers.Game;
import hero.Hero;
import models.Critter.CritterType;

import org.jsfml.graphics.*;
import org.jsfml.system.Vector2f;
import org.jsfml.window.event.Event;

import assets.FontCatalogue;
import assets.TextureCatalogue;

import java.io.IOException;
import java.nio.file.Paths;

/**
 * The UI overlay that displays stats during the game.
 *
 * UI overlay extends Drawable.
 * The UI overlay displays the health, coins and items the player has and the amount of those items.
 *
 * @author Ben Tomczyk
 */

public class UIOverlay implements Drawable {

    private static TextureCatalogue TEXTURES = TextureCatalogue.get();

    private HealthBar health;
    private Coin coin;
    private Text coinText;

    private Sprite potion, weapon;
    private Text potionText, weaponText;

    private Button increaseHealth;
    private Button decreaseHealth;
    private Button increaseCoin;
    private Button decreaseCoin;
    private Font font = FontCatalogue.get().FONT_UI_BAR;
    private int coinAmount = 0;

    /**
     * Constructor for the UI overlay.
     *
     * Initialises the health, coins, items and their corresponding texts.
     *
     * @param window RenderWindow in which the UI overlay will be drawn in.
     */

    public UIOverlay(RenderWindow window) {
        health = new HealthBar(new Vector2f(20, 20), 0.3f);
        coin = new Coin(new Vector2f(740, 42), 0.4f);
        coinText = new Text(Integer.toString(coinAmount), font, 50);
        coinText.setPosition(new Vector2f(1100, 12));

        // coinText = new Text(Integer.toString(coinAmount), font, 50);
        // // coinText.setPosition(new Vector2f(640, 12));
        // coinText.setPosition(new Vector2f(1100, 12));

        // Potions
        this.potion = new Sprite(TEXTURES.POTION_TYPE_2);
        this.potion.scale(Vector2f.componentwiseDiv(new Vector2f(64,64), new Vector2f(potion.getGlobalBounds().width, potion.getGlobalBounds().height)));
        this.potion.setPosition(1030,42);
        this.potionText = new Text("0",font,50);
        potionText.setPosition(930,12);

        increaseHealth = new Button(2, 0.2f, new Vector2f(700, 200), "+ Health", window);
        decreaseHealth = new Button(2, 0.2f, new Vector2f(700, 280), "- Health", window);
        increaseCoin = new Button(3, 0.2f, new Vector2f(664, 360), "+$1", window);
        decreaseCoin = new Button(3, 0.2f, new Vector2f(740, 360), "-$1", window);
    }

    /**
     * Constructor for the UI overlay.
     *
     * Initialises the health, coins, items and their corresponding texts.
     *
     * @param game The main game loop the UI overlay.
     */
    public UIOverlay(Game game) {
        // this(game.window);
        RenderWindow window = game.window;

        health = new HealthBar(new Vector2f(20, 20), 0.3f);
        coin = new Coin(new Vector2f(1240, 42), 0.4f);
        // System.out.println("Coin: " + coin.coin.getGlobalBounds());

        coinText = new Text(Integer.toString(coinAmount), font, 50);
        // coinText.setPosition(new Vector2f(640, 12));
        coinText.setPosition(new Vector2f(1130, 12));

        // Potions
        this.potion = new Sprite(TEXTURES.POTION_TYPE_1);
        // System.out.println(potion.getGlobalBounds());
        this.potion.scale(2f,2f);
        // System.out.println(potion.getGlobalBounds());
        this.potion.setPosition(1030,12);
        this.potionText = new Text("0",font,50);
        potionText.setPosition(970,12);
        // System.out.println("Potion text " + potionText.getGlobalBounds());

        // Weapons
        this.weapon = new Sprite(TEXTURES.WEAPON_BOW);
        this.weapon.scale(Vector2f.componentwiseDiv(new Vector2f(64,64), new Vector2f(233,233)));
        // System.out.println("Weapon : " + weapon.getGlobalBounds());
        this.weapon.setPosition(880,12);
        this.weaponText = new Text("0", font, 50);
        this.weaponText.setPosition(810,12);

        
    }

    /**
     * Overridden draw class which draws all elements of the UI overlay.
     *
     *
     * @param renderTarget
     * @param renderStates
     */
    @Override
    public void draw(RenderTarget renderTarget, RenderStates renderStates) {
        health.draw(renderTarget, renderStates);
        coin.draw(renderTarget, renderStates);
        coinText.draw(renderTarget, renderStates);

        potion.draw(renderTarget, renderStates);
        potionText.draw(renderTarget, renderStates);

        weapon.draw(renderTarget, renderStates);
        weaponText.draw(renderTarget, renderStates);
        //increaseHealth.draw(renderTarget, renderStates);

    }


    /**
     * Reset the coinAmount, text and health to starting values
     */
    public void reset(){
        coinAmount = 0;
        coinText.setString(Integer.toString(coinAmount));
        health.reset();
    }

    /**
     * Getter for the healthBar
     *
     * @return Healthbar that the UI is using
     */

    public HealthBar getHealthBar() {
        return health;
    }

    public void handleEvent(Event event) {
        // Nothing;
    }


    /**
     * Setter for the display values of the UI elements
     *
     * @param collectible Collectible type you want to update
     * @param newValue integer value to set the text display to
     */

    public void setValue(CritterType collectible, int newValue) {
        switch (collectible) {
            case COIN:
                this.coinText.setString(Integer.toString(newValue));
                break;
            case POTION:
                this.potionText.setString(Integer.toString(newValue));
                break;
            case WEAPON:
                this.weaponText.setString(Integer.toString(newValue));
                break;
            default: break;
        }
    }

    /**
     * Method that updates the UI with new values and draws them to the screen
     *
     * @param hero The Hero of which get the inventory of
     */
    public void updateUI(Hero hero) {
        for (CritterType type : CritterType.values()) {
            if (type.isCollectible()) {
                this.setValue(type, hero.getInventoryOf(type));
            }
        }
    }




}
