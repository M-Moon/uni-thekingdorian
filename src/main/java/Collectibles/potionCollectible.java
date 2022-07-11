package Collectibles;

import hero.Hero;
import managers.collectibleManager;
import models.Critter;
import platformer.GameScene;
import sys.Renderer;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import controllers.Game;

public class potionCollectible extends Collectible {
    private static final Object Collectible = null;

    public static final int POTION_TYPE_1 = 1;
    public static final int POTION_TYPE_2 = 2;

    public static final String PATH_STRING_POTION_1 = "resources/placeholder/textures/potions/icon3.png" ;
    public static final String PATH_STRING_POTION_2 = "resources/placeholder/textures/potions/icon9.png";

    //Critter potion1 = createCollectible(Path.of("/resources/placeholder/potion1.png"));
    private Object Critter;

    private Critter createCollectible(Path of) {
    return (models.Critter) Critter;
    }

    //Critter potion2 = createCollectible(Path.of("/resources/placeholder/potion2.png"));

    int potion1Health = 30;
    int potion2Health = 50;

    public potionCollectible(Game game) {
        this(game, new Random().nextInt(2)+1);
        
    }

    public potionCollectible(Game game, int type) {
        super(game);
        this.setPotionType(type);
        this.setSize(64, 64);
    }

    /**
     * sets the potion type based on type identifier
     * @param typeIdentifier
     */
    protected void setPotionType(int typeIdentifier) {
        if (typeIdentifier>2 || typeIdentifier < 1) return;

        String path = typeIdentifier==POTION_TYPE_1 ? PATH_STRING_POTION_1 : PATH_STRING_POTION_2;
        int damage = typeIdentifier;
        this.setTexture(Path.of(path));
        this.setValue(damage);
    }

    @Override
    public void tick(float time, Game controller) {
        super.tick(time, controller);
        //System.out.println("Potion");

        this.handleCollision(controller, () ->{
            controller.hero.collect(CritterType.POTION, value);
            this.removeFromRenderer(controller.gameRenderer);
            this.isColliding = false;
        });
    } 

    @Override
    public CritterType getCritterType() {
        return CritterType.POTION;
    }

    public potionCollectible(GameScene scene, Renderer renderer) throws IOException {
        super(scene.controller);
        
        Critter potion = null;
        //GameScene scene = new GameScene(controller);
        int levelNumber = collectibleManager.levelSceneToNumber(scene);
        int limit = levelNumber *2;
        int numberOfPotions = randomInt(limit);
        List<String> potionList = Arrays.asList("/resources/placeholder/textures/potions/icon3.png", "/resources/placeholder/textures/potions/icon9.png");

        for(int i = 0; i< numberOfPotions; i++){
            //randomise which potion it is
            Random randomizeCollectible = new Random();
            int randomPotion = randomizeCollectible.nextInt(potionList.size());
            String randomPotionPath = potionList.get(randomPotion);
            potion = createCollectible(Path.of(randomPotionPath), renderer);
            //note: potions aren't animated
            // Set the position of the potions
            int xCoordinate = 0; //ground level
            int yCoordinate = randomInt(500); //set limit as screen length- is this 500?
            potion.setPosition(xCoordinate, yCoordinate);
            //set the collisions
            //potion.potionCollision(hero, potion);
        }
        //return potion;

    }
    //changing for commit


    /**
     * When the player collides with a potion, add to the health system
     *
     * @param hero - player
     * @param potion - potion to be collided with
     */
    public void potionCollision(Hero hero, Collectible potion) {

        //if (collectibleCollision(hero, potion1) == true) {
            //heroHealth = heroHealth + potion1Health;
        }

       // else if (collectibleCollision(hero, potion2) == true)
        {
            //heroHealth = heroHealth + potion2Health
        }
    }
//}
