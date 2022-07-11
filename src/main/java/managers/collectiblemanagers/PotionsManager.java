package managers.collectiblemanagers;

import Collectibles.Collectible;
import Collectibles.potionCollectible;
import controllers.Game;
import managers.AbstractManager;
import managers.collectibleManager;
import managers.crittermanagers.ObstacleManager;
import models.Critter;
import models.Obstacle;
import platformer.GameScene;
import platformer.Platformer;
import sys.Renderer;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class PotionsManager extends AbstractManager<potionCollectible> {

    private static PotionsManager INSTANCE = null;
    // private GameScene GameScene;


    protected PotionsManager(Game game) {
        super(game);
        // this.active = true;
    }

    public static final PotionsManager get(Game game) {
        if (INSTANCE == null) {
            INSTANCE = new PotionsManager(game);
        }
        return INSTANCE;
    }

    /**
     * renders potion
     * @return potion
     */
    @Override
    protected potionCollectible create() {
        // Collectible potion = new potionCollectible(this.controller);
        return new potionCollectible(this.controller);
    }
}
