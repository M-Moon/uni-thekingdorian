package managers.crittermanagers;

import managers.AbstractManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Collectibles.keyCollectible;
import controllers.Game;

public class KeyManager extends AbstractManager<keyCollectible> {

    protected static KeyManager INSTANCE = null;
    protected KeyManager(Game game) {
        super(game);
        this.active = true;
    }

    public static final KeyManager get(Game game) {
        if (INSTANCE == null) {
            INSTANCE = new KeyManager(game);
        }
        return INSTANCE;
    }

    @Override
    protected keyCollectible create() {
        return keyCollectible.get(this.controller);
    }

    @Override
    public List<keyCollectible> spawn(float time) {
        if (keyCollectible.get(this.controller).hasBeenCollected()) {
            return new ArrayList<>();
        }
        boolean spawn = this.decideToSpawn(time);
        return spawn ? Arrays.asList(keyCollectible.get(this.controller)) : new ArrayList<>();
    }
    
}
