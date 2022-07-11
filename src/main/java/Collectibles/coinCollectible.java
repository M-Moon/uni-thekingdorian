package Collectibles;

import hero.Hero;
import models.Critter;

import java.nio.file.Path;

import org.jsfml.graphics.*;
import org.jsfml.system.Vector2f;

import controllers.Game;

public class coinCollectible extends Collectible {
    // private static final Object Collectible = null;
    protected static IntRect textureRect = new IntRect(0, 0, 160, 160);

    //Critter coin = createCollectible(Path.of("/resources/placeholder/coin.png"));

    public coinCollectible(Game game){
        super(game);
        this.setTexture(game.GRAPHICS.COIN_SHEET);
        this.sprite.setTextureRect(textureRect);
        this.setSize(new Vector2f(45,45));
    }

    @Override
    public void draw(RenderTarget arg0, RenderStates arg1) {
        super.draw(arg0, arg1);
        this.valueLabel.draw(arg0, arg1);
    }

    @Override
    public CritterType getCritterType() {
        return CritterType.COIN;
    }

    @Override
    public void tick(float time, Game game) {
        super.tick(time, game);
        //System.out.println("Coin");
        this.handleCollision(game, () -> {
            game.hero.collect(CritterType.COIN, value);
            this.removeFromRenderer(game.gameRenderer);
            this.isColliding = false;
        });

    }

    /**
     *
     * @param levelNumber - number of coins created depends on what level the player is on
     * @deprecated 
     */
    @Deprecated
    public void createCoins(int levelNumber){
        int numberOfCoins = randomInt(levelNumber*50);
        for(int i =0; i <numberOfCoins; i++){
            //Critter coin = createCollectible(Path.of("/resources/UI/CoinSpriteSheet.png"), window);
            //animateCollectible(coin, 1, 8);
        }
    }

    /**
     * When the player collides with a coin, add to the coinCounter
     *
     * @param hero player
     * @param coin coin that is collided with
     */
    public int coinCollision(Hero hero, Critter coin) {
        if (collectibleCollision(hero, coin) == true)
        {
            coinCounter = coinCounter +1;
            moveCollectibleToInventory(coin);
        }
        return coinCounter;
    }

}