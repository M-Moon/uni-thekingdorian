package npc;

import Collectibles.Collectible;
import controllers.Game;
import hero.Hero;
import managers.crittermanagers.GuideManager;

import java.nio.file.Path;

import org.jsfml.window.Keyboard;

public class Merchant extends NPC {

    private static Merchant SINGLETON = null;
    
    /**
     * Event ID signaling that the merchant is on screen
     */
    public static final int EVENT_MERCHANT_ON = 712;
    public static final int EVENT_MERCHANT_OFF = -712;

    public final Game controller;

    protected Merchant(Game game) {
        this.controller = game;
        this.addObserver(GuideManager.get(controller));
        this.setTexture(controller.GRAPHICS.NPC_MERCHANT_ZONE);
        this.setSize(Guide.get(game).getSize());

        // Build the area
        buildZone();
    }

    /**
     * Obtain the unique merchant available
     * @param game
     * @return
     */
    public static Merchant get(Game game) {
        if (SINGLETON == null) {
            SINGLETON = new Merchant(game);
        }
        return SINGLETON;
    }

    @Override
    public CritterType getCritterType() {
        return CritterType.MERCHANT;
    }

    private void buildZone() 
    {
        this.zone = new Zone(controller, Merchant.this, 3){};
        this.zone.setZoneTitle("Merchant's Shop");
        this.zone.onCancel(this::exitZone);

        // Text
        this.zone.setDialog("You have entered the Merchant's shop. They are intrigued by your courage and offers to sell you exclusive items."+
            " Do you wish to purchase some tokens? Click the buttons to purchase an item (W stands for weapons, K stands for Key, P stands for potion).");
        this.zone.onCancel(this::exitZone);

        // Icon
        this.zone.setIcon(controller.GRAPHICS.NPC_MERCHANT);

        this.zone.addOption("W (200)", ()->controller.hero.purchase(CritterType.WEAPON, 200, 5));
        this.zone.addOption("P (50)", ()->controller.hero.purchase(CritterType.POTION, 50, 2));
    }

    private void exitZone() {
        this.zone.removeFromRenderer(controller.gameRenderer);
        this.controller.setClockState(true);
    }

    @Override 
    public void tick(float lastTime, Game game) {
        super.tick(lastTime, controller);

        // Verify if we are entering a zone:
        if (this.collides(game.hero, game)) {
            if (Keyboard.isKeyPressed(Keyboard.Key.I)) {
                this.enterZone();
                game.setClockState(false);
            }
        }
    }

    public void enterZone() {
        // Init the zone:
        this.zone.addToRenderer(controller.gameRenderer);
        this.controller.setClockState(false);
    }

    /**
     * @param: the collectible they choose to buy
     * Using the spend() method in hero to buy collectibles from the merchant
     */
    public void buyFromMerchant(Hero hero, Collectible collectible) {
        int price = 0;
        String collectibleType = String.valueOf(collectible.getTexture());
        if(collectibleType == "sword"){
            price = 150;
        }
        if(collectibleType == "bowAndArrow"){
            price = 200;
        }
        if(collectibleType == "potion1"){
            price = 70;
        }
        if(collectibleType == "potion2"){
            price = 50;
        }

        // hero.spend(collectible, price);
    }
}


