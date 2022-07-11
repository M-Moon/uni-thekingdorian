package managers.crittermanagers;

import Collectibles.Collectible;
import Collectibles.coinCollectible;
import controllers.Game;
import managers.AbstractManager;
import models.Critter.CritterType;
import platformer.GameScene.SceneType;

public class CoinManager extends AbstractManager<coinCollectible> {

    /**
     * The increase in the value of each generated coin
     */
    private float markup = 1f;

    protected static CoinManager INSTANCE = null;
    protected CoinManager(Game game) {
        super(game);
        this.active = true;

        
    }
    public static final CoinManager get(Game game) {
        if (INSTANCE == null) {
            INSTANCE = new CoinManager(game);
        }
        return INSTANCE;
    }

    @Override
    protected coinCollectible create() {
        return new coinCollectible(controller);
    }

    @Override
    protected void parametrizeElement(coinCollectible element) {
        super.parametrizeElement(element);

        SceneType type = controller.scene.getSceneType();
        int value = (int) Math.ceil( markup * (random.nextInt(type.maxCoin-type.minCoin) + type.maxCoin) );
        // element.setValue(controller.scene.getSceneType().);
        element.setValue(value);
    }

    @Override
    public void reset() {
        super.reset();
        this.markup = 1f;
    }

    /**
     * Determines how the 
     */
	public void setCoinValueMarkup(float f) {
        this.markup += f;
	}

    
    
}
