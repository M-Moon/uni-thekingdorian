package Collectibles;

import hero.Hero;
import models.Critter;
import org.jsfml.system.Vector2f;

import controllers.Game;
import platformer.AbstractScene;
import platformer.Platformer;

import java.io.IOException;
import java.nio.file.Path;

public class keyCollectible extends Collectible {

    // private static final Object Collectible = null;
    //Critter key = createCollectible(Path.of("/resources/placeholder/key.png"));
    private static keyCollectible KEY = null;
    private boolean heroHasKey;

    protected keyCollectible(Game game) {
        super(game);
        this.setLayer(3);
        this.setTexture(game.GRAPHICS.KEY);
        this.setSize(70f,70f);
        // this.setTexture(Path.of("resources/placeholder/key.png"));
        // this.resizeCritter(newSize);
    }

    public static keyCollectible get(Game game){
        if (KEY==null){
            KEY = new keyCollectible(game);
        }
        return KEY;
    }

    @Override
    public void tick(float time, Game controller) {
        super.tick(time, controller);
        // System.out.println("Key");
        this.handleCollision(controller, () ->{
            controller.hero.collect(CritterType.KEY, 1);
            this.heroHasKey = true;
            this.removeFromRenderer(controller.gameRenderer);
            this.isColliding = false;
        });
    }

    public boolean hasBeenCollected() {
        return this.heroHasKey;
    }

    @Override
    public CritterType getCritterType() {
        return CritterType.KEY;
    }


    /**
     * When the player collides with the key, return boolean
     *
     * @param hero- player
     * @param key- Dorian's key
     * @return boolean
     */
    public boolean keyCollision(Hero hero, Critter key) {
        if (collectibleCollision(hero, key) == true) {
            moveCollectibleToInventory(key);
        }
        return heroHasKey == true;
    }

}

