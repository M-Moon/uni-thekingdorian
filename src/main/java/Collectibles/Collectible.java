package Collectibles;

import ManageAnimation.Animation;
import controllers.Game;
import devtools.ObservableEvent;
import devtools.Observer;
import hero.Hero;
import managers.collectibleManager;
import models.Critter;
import org.jsfml.graphics.*;
import org.jsfml.system.*;
import platformer.AbstractScene;
import platformer.GameScene;
import platformer.Platformer;
import sys.Renderer;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Collectible extends Critter {

    private Critter collectible;
    public boolean collectibleCollision;
    public boolean inInventory = false;
    static float xCoordinate = 0, yCoordinate = 0;
    Texture collectibleTexture = new Texture();
    public int coinCounter;

    protected Game controller;
    private Renderer renderer;

    /**
     * The value of this collectible
     */
    protected int value;
    /**
     * 
     */
    protected Text valueLabel = new Text();

    protected Animation animation;
    

    public Collectible(Game game) {
        this.controller = game;
        this.setLayer(2);

        // Init the label
        valueLabel.setCharacterSize(10);
        valueLabel.setFont(assets.FontCatalogue.get().FONT_FREESANS);

    }

    /**
     * 
     * @param texturePath
     * @param game
     * @deprecated - never used
     */
    @Deprecated(forRemoval = true)
    public Collectible(Path texturePath, Game game) {

        this(game);
        // this.setTexture(texturePath);
        try {
            this.changeTexture(this, texturePath);
        } catch (IOException e) {}
        
        // Set size
        // this.resizeCritter(newSize);
    }


    //Critter collectibleCritter = new Critter() {
    @Override
    public void placeInScene(AbstractScene scene, Vector2f platformPosition) { 
        /* */
    }

    @Override
    public void place(Platformer scene, Vector2f availablePosition) {
        float x = availablePosition.x + (scene.getPlatformTileSize().x-this.getSize().x)/2;
        float y = availablePosition.y - this.getSize().y;
        this.setPosition(x, y);
        // this.valueLabel.setPosition(x ,y-valueLabel.getGlobalBounds().height);

    }

    @Override
    public void setPosition(float x, float y) {
        super.setPosition(x, y);
        this.valueLabel.setPosition(x ,y-valueLabel.getGlobalBounds().height);

        // this.valueLabel.setPosition(x+(x-valueLabel.getGlobalBounds().width)/2f ,y-valueLabel.getGlobalBounds().height);
    }

    public void setValue(int value) {
        this.value = value;
        this.valueLabel.setString(Integer.toString(this.value));
    }

    public int getValue() {
        return this.value;
    }


    // @Override
    // public void draw(RenderTarget arg0, RenderStates arg1) {
    //     super.draw(arg0, arg1);
    //     this.valueLabel.draw(arg0, arg1);
    // }

    // @Override
    // public void tick(float time, Game game) {
    //     super.tick(time, game);
    //     // boolean collided = this.collides(game.hero, game);
    //     // if (!this.rendered || collided==this.colliding) {
    //     //     return;
    //     // }
    //     // if (collided && !this.colliding) {
    //     //     this.colliding = true;
    //     //     this.removeFromRenderer(game.gameRenderer);
    //     //     this.colliding = false;
    //     // }
    // }

    public CritterType getType() {
        return CritterType.COIN;
    }

    public static Collectible createCollectible(CritterType type, Game game) {
        switch (type) 
        {
            case COIN:
                return new coinCollectible(game);
            // case WEAPON:
            //     return new weaponCollectible();
            default:break;
        }
        return null;
    }

    /**
     * Creates a Critter, adds a texture to it and adds to window
     *
     * @param collectiblePath path
     * @return a Collectible with image from texture, and x and y Coordinates
     */
    public static Critter createCollectible(Path collectiblePath, Renderer r) throws IOException {
        Critter collectible = new Critter() {
            @Override
            public void placeInScene(AbstractScene scene, Vector2f platformPosition) {
                //
            }

            @Override
            public void place(Platformer scene, Vector2f availablePosition) {
                //
            }

            @Override
            public Critter changeTexture(Critter collectible, Path collectiblePath) {
                return null;
            }
        };
        // Set the texture of the Collectible
        collectible.changeTexture(collectible, collectiblePath);

        // Set the position of the Collectible
        xCoordinate = 0; //ground level
        yCoordinate = randomInt(500); //set limit as screen length- is this 500?
        collectible.setPosition(xCoordinate, yCoordinate);
        // Animate the collectible
       // animateCollectible(Critter collectible, row, column) - uncomment when we have set sprite sheets
        //window.draw(collectible);
        return collectible;
    }

    /**
     * Random number between and parameter- used to randomise the Collectible's y coordinate
     * also used to randomise number of collectibles in the chest
     * @param limit aka the 'cap' on the random number generated
     * @return the random integer generated
     */
    public static int randomInt(int limit) {
        Random randomNumber = new Random();
        int randomInt = randomNumber.nextInt(limit); //random number between 0 and limit
        return randomInt;
    }

    /**
     *  Randomise number of collectibles to put in the chest
     * @param
     * @return (probably gonna change this)
     */
    public List<String> collectiblesForChest(GameScene scene, Renderer renderer) throws IOException {
        List<Critter> collectiblesForChest = null;
        //REPLACE THESE STRINGS WITH THE PATHS OF THE SPRITES
        List<String> collectiblesList = Arrays.asList("/resources/placeholder/textures/potions/icon3.png", "/resources/placeholder/textures/potions/icon9.png", "/resources/placeholder/textures/bowAndArrow.png", "/resources/UI/Sword.png");
        List<String> collectiblePaths = null;
        int levelNumber = collectibleManager.levelSceneToNumber(scene);
        //int limit = levelNumber *1;
        //generate random number of collectibles based on the level
        int limit = levelNumber*5; //so level 1 capped at 5, 2 capped at 10, 3 capped at 15...
        int numberOfCollectibles = randomInt(limit); //random number between 0 and limit

        for(int i =0; i<numberOfCollectibles; i++){
            //iterate through collectiblesList and randomly pick a collectible to place in chest
            Random randomizeCollectible = new Random();
            int randomItem = randomizeCollectible.nextInt(collectiblesList.size());
            String randomCollectible = collectiblesList.get(randomItem);
            Critter chestCollectible = createCollectible(Path.of(randomCollectible), renderer);
            collectiblePaths.add(chestCollectible.getPath());
            collectiblesForChest.add(chestCollectible);
            //moveCollectibleToInventory(randomCollectible);
        }

        //return collectiblesForChest;
        return collectiblePaths;
    }

    /**
     * changes the Texture of a collectible
     *
     * @param collectible that you want to change the texture of
     * @param collectiblePath path of the image to change the texture to
     * @return collectible with changed texture
     * @throws IOException
     */
    public Critter changeTexture(Critter collectible, Path collectiblePath) throws IOException {
        collectibleTexture.loadFromFile(collectiblePath);
        collectible.setTexture(collectibleTexture);
        return collectible;
    }


    /**
     * Removes the Collectible from the screen and moves to the player's Inventory
     *
     * @param collectible- the Collectible to be removed
     * @return boolean saying whether the collectible is in the inventory or not
     */
    public boolean moveCollectibleToInventory(Critter collectible) {
        //window.remove(collectible)
        // return collectible.inInventory = true;
        return true;
    }

    /**
     * Detects collisions between the Player and a given Collectible
     *
     * @param hero        aka the player
     * @param collectible to be collided with
     * @return the Coordinates of the collision
     */
    public boolean collectibleCollision(Hero hero, Critter collectible) {

        FloatRect collisionDetector = hero.getSprite().getGlobalBounds().intersection(collectible.getGlobalBounds());
        if (collisionDetector == null) {
            return collectibleCollision = false;
        } else {
            xCoordinate = collisionDetector.height; //returns xCoordinate of the collision rectangle
            yCoordinate = collisionDetector.top; //returns yCoordinate of the collision rectangle
            moveCollectibleToInventory(collectible);
            //controller.gameRenderer.removeFromRenderer();
            return collectibleCollision = true;
        }
    }

    /**
     * Animate the collectibles until they are collided with
     * @param collectible the collectible to be animated
     * @param row number of rows in sprite sheet
     * @param column number of columns in sprite sheet
     */
    public void animateCollectible(Critter collectible, int row, int column) {
        float speed = 5; //set standard animation speed
        // while (!collectible.collectibleCollision = false)
        // {
        //     Animation collectibleAnimation = new Animation(collectibleTexture, collectible, row, column);
        //     collectibleAnimation.update(collectible, speed, row);
        // }
    }

    public void animate(int row, int column) {
        float speed = 5; //set standard animation speed
        while (!this.isColliding)
        {
            Animation collectibleAnimation = new Animation(collectibleTexture, collectible, row, column);
            collectibleAnimation.update(collectible, speed, row);
        }
    }

    /**
     * Creates a Chest, adds a texture
     *
     * @return a Chest with image from texture
     */
    public Critter createChest(Renderer r) throws IOException {
        Critter chest = createCollectible(Path.of("/resources/placeholder/textures/chest_closed.png"), r);
        return chest;
    }


}





