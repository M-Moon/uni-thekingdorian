package Collectibles;

import java.util.function.BiConsumer;

import org.jsfml.graphics.*;
import org.jsfml.system.*;

import ManageAnimation.PopupAnimation;
import ManageAnimation.Transition;
import assets.FontCatalogue;
import controllers.Game;
import ui.PopupFrame;

public class Chest extends Collectible {

    // Information relevant to the chest popup
    protected Text coinText, weaponText, potionText;
    protected PopupFrame screen;

    private static Chest CHEST = null;


    protected Chest(Game game) {
        super(game);
        // Set the texture
        // this.setTexture(texture);

        // Create the animation
        this.screen = new PopupFrame(PopupFrame.DEFAULT_POPUP_SIZE, PopupFrame.DEFAULT_POPUP_POSITION);
        screen.setTitle("Chest contents", null, 25);
        screen.setBackground(game.GRAPHICS.HOMESCREEN_BACKGROUND);
        BiConsumer<Sprite,Vector2f> RESCALE = (s,x) -> s.scale(Vector2f.componentwiseDiv(x, new Vector2f(s.getGlobalBounds().width, s.getGlobalBounds().height)));
        final Vector2f center = new Vector2f(16f,16f);
        final Vector2f iconSize = new Vector2f(50f,50f);
        float iconX0 = 75*1.5f-iconSize.x/2;


        Sprite coin = new Sprite(game.GRAPHICS.COIN_SHEET, new IntRect(0,0,160,160));
        RESCALE.accept(coin, iconSize);
        coin.setPosition(screen.mapPanelToWindow(new Vector2f(iconX0, 60f)));
        System.out.println("Coin at " + new Vector2f(75*1.5f-iconSize.x/2, 60f));
        Sprite weapon = new Sprite(game.GRAPHICS.WEAPON_BOW);
        RESCALE.accept(weapon, iconSize);
        weapon.setPosition(screen.mapPanelToWindow(new Vector2f(iconX0 + 150, 60f)));
        // weapon.setOrigin(center);
        Sprite potion = new Sprite(game.GRAPHICS.POTION_TYPE_1);
        RESCALE.accept(potion, iconSize);
        potion.setPosition(screen.mapPanelToWindow(new Vector2f(iconX0 + 300, 60f)));
        screen.addComponent(coin);
        screen.addComponent(weapon);
        screen.addComponent(potion);
    }

    public static Chest get(Game game){
        if (CHEST==null){
            CHEST = new Chest(game);
        }
        return CHEST;
    }

    @Override
    public void tick(float time, Game controller) {
        super.tick(time, controller);
        this.handleCollision(controller, () ->{
            // set the texture to be open
            // this.setTexture(open)
            // Set the prizes in the popup
            this.setChestContents(getPrizes());
            controller.gameFlow.addTransition(new PopupAnimation(screen, 5f));
        });
    }

    public void openChest() {
        this.setChestContents(getPrizes());
        this.controller.gameFlow.addTransition(new PopupAnimation(screen, 5f));
    }

    /**
     * Generates a set of prizes to be collected upon collision with this chest.
     * This method returns an array of three integers corresponding to the number of coins,
     * weapons and potions (respectively)obtained by the player
     * 
     * @return an array of prizes
     */
    protected int[] getPrizes() {
        return new int[]{0,0,0};
    }

    /**
     * Re-initializes the popup  by putting the appropriate values.
     * These values will often be procured via {@link #getPrizes()}
     * 
     * @param prizes the contents of the chest
     */
    protected void setChestContents(int[] prizes) {
        screen.removeComponent(coinText);
        screen.removeComponent(weaponText);
        screen.removeComponent(potionText);

        coinText = new Text(Integer.toString(prizes[0]), FontCatalogue.get().FONT_UI_BAR, 35);
        float x = 75*0.5f - coinText.getGlobalBounds().width/2;
        coinText.setPosition(screen.mapPanelToWindow(new Vector2f(x, 50)));
        weaponText = new Text(Integer.toString(prizes[1]), FontCatalogue.get().FONT_UI_BAR, 35);
        weaponText.setPosition(screen.mapPanelToWindow(new Vector2f(x+150, 50)));
        potionText = new Text(Integer.toString(prizes[2]), FontCatalogue.get().FONT_UI_BAR, 35);
        potionText.setPosition(screen.mapPanelToWindow(new Vector2f(x+300, 50)));

        screen.addComponent(coinText);
        screen.addComponent(weaponText);
        screen.addComponent(potionText);
    }


    
}
