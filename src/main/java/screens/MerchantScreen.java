package screens;

import Collectibles.Collectible;
import hero.Hero;
import models.Critter;
import models.Critter.CritterType;

import org.jsfml.graphics.Texture;
import org.jsfml.system.Vector2f;
import platformer.AbstractScene;
import platformer.Platformer;

import java.io.IOException;
import java.nio.file.Path;

public class MerchantScreen {
    private controllers.Game Game;
    Collectible merchantCollectible = new Collectible(Game);

    /**
     * creates merchant as a critter and implements methods
     * @return Critter (merchant) which the hero can interact with
     * @throws IOException
     */
    public Critter createMerchant() throws IOException {

        Critter merchant = new Critter()
    {
            @Override
            public void placeInScene(AbstractScene scene, Vector2f platformPosition) {
                //
            }

            @Override
            public void place(Platformer scene, Vector2f availablePosition) {
                //
            }

            @Override
            public Critter changeTexture(Critter merchant, Path collectiblePath) {
                return null;
            }
        };
        // Set the texture of the Collectible
        Path collectiblePath = Path.of("resources/placeholder/MerchantWalk.png");
        merchant.changeTexture(merchant, collectiblePath);

        // Set the position of the Collectible
        int xCoordinate = 0; //ground level
        int yCoordinate = Collectible.randomInt(500); //set limit as screen length- is this 500?
        merchant.setPosition(xCoordinate, yCoordinate);
        // Animate the collectible
        // animateCollectible(merchant, row, column) - uncomment when we have set sprite sheets
        //window.draw(merchant);
        return merchant;

    }
    Hero hero;
    Critter.CritterType collectible;
    int price;

    /**
     * deals with the merchant collision
     */
    public void merchantCollision(){
        merchantCollectible.collectibleCollision(hero, merchantCollectible);
    }


}

