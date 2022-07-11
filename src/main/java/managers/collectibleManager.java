package managers;

import Collectibles.Collectible;
import controllers.Game;
import models.Critter;
import platformer.GameScene;
import platformer.Platformer;
import sys.Renderer;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class collectibleManager extends AbstractManager<Collectible>  //private static collectibleManager collectibleMan;
{

    private Collectible Collectible;
    private boolean collision;
    GameScene gameScene = new GameScene(controller);

    public collectibleManager(Game controller) {
        super(collectibleMan.platformer, collectibleMan.renderer);
    }

    private static collectibleManager collectibleMan = null;

    public static int levelSceneToNumber(GameScene gameScene){
        // GROUND, SKY, DUNGEON, ARENA
        int levelNumber = 0; //initialise variable
        if(gameScene.getSceneType() == GameScene.SceneType.valueOf("GROUND")){
            levelNumber = 1;

        }
        else if (gameScene.getSceneType() == GameScene.SceneType.valueOf("SKY")) {
            levelNumber = 2;
        }

        else if(gameScene.getSceneType() == GameScene.SceneType.valueOf("DUNGEON")) {

            levelNumber = 3;
        }
        else if (gameScene.getSceneType() == GameScene.SceneType.valueOf("ARENA")){
            levelNumber = 4;
        }
        return levelNumber;
    }

    protected Collectibles.Collectible collectibleManager(Platformer p, Renderer r) throws IOException {
        {
            if (collectibleMan == null) {
                collectibleMan = new collectibleManager(controller) {

                    //changed this to Critter- check w Lydie

                    /**
                     * @return coins on screen
                     * @throws IOException
                     */
                    protected ArrayList<Critter> createCoins() throws IOException {
                        int levelNumber = levelSceneToNumber(gameScene);
                        ArrayList<Critter> coins = new ArrayList<>();
                        Critter coin = null;
                        int limit = levelNumber*50;
                        int numberOfCoins = Collectible.randomInt(limit);

                        for(int i = 0; i< numberOfCoins; i++){
                            //create coins with given texture
                            coin = Collectible.createCollectible(Path.of("/resources/UI/CoinSpriteSheet.png"), this.renderer);
                            //animate the coins
                            Collectible.animateCollectible(coin, 1, 8);
                            // Set the position of the coins
                            int xCoordinate = 0; //ground level
                            int yCoordinate = Collectible.randomInt(500); //set limit as screen length- is this 500?
                            coin.setPosition(xCoordinate, yCoordinate);
                            // Set the collision- have to have hero as a local variable
                            //coinCollectible.coinCollision(hero, coin);
                            coins.add(coin);
                            //return coin;
                        }
                        return coins;
                    }

                    /**
                     * creates potions
                     * @return potion
                     * @throws IOException
                     */
                    protected Critter createPotions() throws IOException {
                        Critter potion = null;
                        int levelNumber = levelSceneToNumber(gameScene);
                        int limit = levelNumber *2;
                        int numberOfPotions = Collectible.randomInt(limit);
                        List<String> potionList = Arrays.asList("/resources/placeholder/textures/potions/icon3.png", "/resources/placeholder/textures/potions/icon9.png");

                        for(int i = 0; i< numberOfPotions; i++){
                            //randomise which potion it is
                            Random randomizeCollectible = new Random();
                            int randomPotion = randomizeCollectible.nextInt(potionList.size());
                            String randomPotionPath = potionList.get(randomPotion);
                            potion = Collectible.createCollectible(Path.of(randomPotionPath), this.renderer);
                            //Collectible.animateCollectible(potion, 1, 8);
                            // Set the position of the potions
                            int xCoordinate = 0; //ground level
                            int yCoordinate = Collectible.randomInt(500); //set limit as screen length- is this 500?
                            potion.setPosition(xCoordinate, yCoordinate);
                            //set the collisions
                            //potion.potionCollision(hero, potion);
                        }
                        return potion;
                    }

                    /**
                     * creates weapons
                     * @return weapon
                     * @throws IOException
                     */
                    protected Critter createWeapons() throws IOException {
                        Critter weapon = null;
                        int levelNumber = levelSceneToNumber(gameScene);
                        int limit = levelNumber *1;
                        int numberOfWeapons = Collectible.randomInt(limit);
                        List<String> weaponList = Arrays.asList("/resources/placeholder/textures/bowAndArrow.png", "/resources/UI/Sword.png");

                        for(int i = 0; i< numberOfWeapons; i++){
                            //randomise which potion it is
                            Random randomizeCollectible = new Random();
                            int randomPotion = randomizeCollectible.nextInt(weaponList.size());
                            String randomWeaponPath = weaponList.get(randomPotion);
                            weapon = Collectible.createCollectible(Path.of(randomWeaponPath), this.renderer);
                            //Collectible.animateCollectible(weapon, 1, 8);
                            // Set the position of the weapons
                            int xCoordinate = 0; //ground level
                            int yCoordinate = Collectible.randomInt(500); //set limit as screen length- is this 500?
                            weapon.setPosition(xCoordinate, yCoordinate);
                        }
                        return weapon;
                    }

                    @Override
                    protected Collectible create() {
                        return null;
                    }

                    @Override
                    public List<Collectible> spawn(float time) {
                        return null;
                    }


                };
            }
        }
    return Collectible;
    }

    @Override
    protected Collectibles.Collectible create() {
        return null;
    }

    /**
     * deals with collectible collision
     * @param collectible
     * @param time
     * @param game
     */
    public void tick(Critter collectible, float time, Game game) {
        tick(collectible, time, game);
        boolean collision = collectible.getPixelBounds(game).intersection(game.hero.getPixelBounds(game))!=null;
        if (!collectible.rendered  || collision== this.collision) {
            return;
        }
        if (collision && !this.collision) {
            System.out.println("New collision!");
            this.collision = true;
        }
        if (this.collision) {
            // game.hero.collect(this.tokenType, this.value);
            //this.handleCollection((Player)game.hero);
            //(this.renderer).removeFromRenderer(game.gameRenderer);
            this.collision = false;
            // this.addToInventory(game.hero, renderer);
        }
    }
}