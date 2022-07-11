package managers.collectiblemanagers;

import Collectibles.Collectible;
import Collectibles.potionCollectible;
import Collectibles.weaponCollectible;
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

public class WeaponryManager extends AbstractManager<Collectible> {

    private static WeaponryManager INSTANCE = null;
    // private GameScene GameScene;

    protected WeaponryManager(Game game) {
        super(game);
        // this.active = true;
    }

    public static final WeaponryManager get(Game game) {
        if (INSTANCE == null) {
            INSTANCE = new WeaponryManager(game);
        }
        return INSTANCE;
    }

    /**
     * renders weapon
     * @return weapon
     */
    @Override
    protected Collectible create() {
        Collectible weapon = new weaponCollectible(this.controller);
        return weapon;
    }
}
