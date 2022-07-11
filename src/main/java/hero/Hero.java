package hero;

import Collectibles.Collectible;
import controllers.Game;
import devtools.ObservableEvent;
import fighter.Damageable;
import fighter.Projectile;
import models.Critter;

import org.jsfml.graphics.*;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;
import org.jsfml.window.Keyboard;

import platformer.AbstractScene;
import platformer.Platformer;
import sound.SoundManager;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.EnumMap;
import java.util.LinkedList;
import java.util.concurrent.ThreadLocalRandom;

/**
 * The hero of the game, whom the player controls.
 *
 * Hero extends Critter and implements Damageable.
 *
 * The hero contains attributes that track things such as the player's health, the amount of coins the player has,
 * the player's currently set texture, the player model's height and width, their position (from Critter),
 * movement indicators for left and right, a jumping and standing-on-ground indicator.
 * It also contains default values for its speed and jump strength. (NOTE: This can be made to be changed with getters
 * and setters)
 *
 * Hero has functions for making the player move left and right, which are activated in the tick method (which should be
 * called every tick of the program) if one of the movement indicators are true. These can be toggled with the rightMovement
 * and leftMovement functions.
 *
 * The functions relevant to the Damageable, such as die(), takeDamage(), and inflictDamage(), are all implemented.
 *
 * Collision is handled by other critters
 *
 * Finally, methods for placing the player in the scene are also given, overriding the interface methods from SceneElement which
 * Critter inherits.
 *
 * @author Joe Moon
 */
public class Hero extends Critter implements Damageable/* . Changeable */
{
    // For when the hero is moving    
    public static final int EVENT_HERO_MOVED = 104;

    // Window
    public final Game controller;

    private RenderWindow window;
    private View view = new View();

    // Sound
    private SoundManager soundManager = new SoundManager();

    // Health system
    protected int HEALTH_MAX = 6;
    protected int healthPoints;
    protected boolean invulnerable = false;
    protected float invulnerabilityTimer = 0f;
    protected float INVULNERABILITY_TIME_LIMIT = 1f;
    private boolean healing = false;

    // Inventory
    protected EnumMap<CritterType, Integer> inventory = new EnumMap<>(CritterType.class);
    private int coinsCount = 0;

    // Player definition
    // private Sprite sprite;
    private Texture playerTexture;

    // Player graphical measurements
    private int height;
    private int width;
    private Vector2i heightWidth;

    // Positional information
    private Vector2f position;

    // Character's movement variables
    private boolean isMovingLeft = false;
    private boolean isMovingRight = false;
    private boolean isJumping = false;
    private boolean onGround;

    private float velocityY = 0;
    private float velocityX = 0;
    private float accelerationX = 1f;
    private final int jumpStrength = 20;
    private float gravity = 0.6f;
    private float maxVelocity = 10;

    ////////Shooting parameters//////////
    LinkedList<Projectile> Bullets=new LinkedList<>();
    private float fireRate = 10;
    private float ammo = 0;
    private float bulletSpeed = 20;
    private float shootDelay = 50;
    private float tempDelay;
    private boolean canShoot = true;


    ///////////////////////////////////////////////////////////////////////////////////
    // Constructor
    ///////////////////////////////////////////////////////////////////////////////////

    /**
     * Constructor for the hero, whom the player controls.
     *
     * Initialises all the basic attributes for the player.
     * Possibility for overloading the constructor to allow for health to be given within the constructor.
     *
     * @param position Vector2f, the initial position of the player
     * @param heightWidth Vector2i, the height and width of the player
     */
    public Hero(Vector2f position, Vector2i heightWidth, Game controller)
    {
        // setting and initialising attributes

        // initialise window
        // this.window = window;
        this.controller = controller;
        this.window = controller.window;

        this.soundManager.setSoundVolume(70);


        // initialise player health
        healthPoints = HEALTH_MAX;

        // initialise player sprite and texture
        sprite = new Sprite();
        playerTexture = new Texture();

        // initialise player position
        this.position = position;
        this.sprite.setPosition(position);

        // initialise player height and width for sprite
        this.heightWidth = heightWidth;
        this.height = heightWidth.x;
        this.width = heightWidth.y;

        // Inventory
        this.inventory.put(CritterType.COIN, 0);
        this.inventory.put(CritterType.WEAPON, 0);
        this.inventory.put(CritterType.POTION, 0);
        this.inventory.put(CritterType.COIN, 0);


        // initialising the player graphics
        initPlayerSprite();
    }

    /**
     * Procedure for initialising the player's sprite when it's created, separated for cleanliness.
     * Initialises based on texture currently loaded for the player, which can be set with setPlayerTexture method.
     */
    private void initPlayerSprite()
    {
        try
        {
            playerTexture.loadFromFile(Paths.get("resources/placeholder/textures/hero/noBKG_KnightIdle_strip.png"));
            sprite.setTexture(playerTexture);
            sprite.setTextureRect(new IntRect(20, 0, 30, 45));
            sprite.setScale(new Vector2f(2F, 2F));
        } catch (IOException e)
        {
            try
            {
                playerTexture.loadFromFile(Paths.get("Game/resources/placeholder/textures/hero/noBKG_KnightIdle_strip.png"));
                sprite.setTexture(playerTexture);
                sprite.setTextureRect(new IntRect(20, 0, 30, 45));
                sprite.setScale(new Vector2f(2F, 2F));

            } catch (IOException e2)
            {
                System.out.println("[ERROR] Could not initialise player sprite.");
                e2.printStackTrace();
            }
        }
    }

    /**
     * Method for changing the texture of the player.
     * Unlikely to be used much from the outside; could be used for outfit changes.
     * Texture shouldn't be changed each time for animation. Instead, a texture sheet should be given and the animation
     * cycled through in a separate function.
     *
     * @param newTexture the texture to be assigned to the player, of type Texture.
     */
    public void setPlayerTexture(Texture newTexture)
    {
        playerTexture = newTexture;
        sprite.setTexture(playerTexture);
    }

    /**
     * Resets all the attributes and movement of the player.
     */
    public void resetHero() {
        this.resetMovement();
        this.healthPoints = HEALTH_MAX;
        this.inventory.replace(CritterType.COIN, 0);
        this.inventory.replace(CritterType.WEAPON, 0);
        this.inventory.replace(CritterType.POTION, 0);
        this.inventory.replace(CritterType.COIN, 0);
    }


    /**
     * The tick method, the main way the player will be updated within the game world.
     * Takes a time parameter that measures time from last tick, and a game controller to allow the player to interact
     * directly with the game window itself if necessary.
     *
     * @param time time since last tick
     * @param controller the game instance in which this Critter ticks
     */
    @Override
    public void tick(float time, Game controller)
    {
        // check to see if player is dead
        if (isDead()){
            return;
        }

        // Check the invulnerability thing
        if (this.invulnerable) {
            this.invulnerabilityTimer += time;
            // It stops being invulnerable when the time limit has gone
            this.invulnerable = invulnerabilityTimer <= INVULNERABILITY_TIME_LIMIT;
        }

        this.onGround = controller.critterManager.getLanding();

        if (velocityX == 0)
        {
            isMovingRight = false;
            isMovingLeft = false;
        }

        // tick over the player movement
        playerMovement();
        healTick();

        // if player is not on ground and not currently jumping, make them fall with gravity. Else, check if jumping and make player jump if true
        if (!onGround && !isJumping)
        {
            gravityEffect();
            //System.out.println("Falling");
        } else if (isJumping)
        {
            //System.out.println("Jump triggered");
            jump();
        }

        // get the current position of the player
        this.position = sprite.getPosition();

        if(Keyboard.isKeyPressed(Keyboard.Key.SPACE) && canShoot && this.getInventoryOf(CritterType.WEAPON) > 0){
            shoot(1);
            canShoot = false;
            tempDelay = shootDelay;
            this.spend(CritterType.WEAPON, 1);
        }
        if(!canShoot){
            shootCheck();
        }
        float Speed = bulletSpeed * time * 100;
        updateBullet(Speed);
    }

    ///////////////////////////////////////////////////////////////////////////////////
    // Position
    ///////////////////////////////////////////////////////////////////////////////////

    /**
     * Returns if the player is currently falling
     * @return the status of if the player is falling, boolean
     */
    public boolean isFallingDown() {
        return !isJumping && !onGround;
    }

    /**
     * Returns status of jumping
     * @return the value of isJumping, boolean
     */
    public boolean isJumping() {return isJumping;}

    /**
     * Returns status of player being on the ground
     * @return the value of onGround, boolean
     */
    public boolean isOnGround() {return onGround;}

    /**
     * Procedure to set if the player is on the ground, used for collision
     * @param onGroundState the boolean state of if the player is touching the ground
     */
    public void setOnGround(boolean onGroundState)
    {
        this.onGround = onGroundState;
    }

    /**
     * Procedure inherited from Critter, moves the player by a certain x and y.
     * Not a positional placement procedure, moves the player from their current position.
     *
     * @param x x distance to move the player
     * @param y y distance to move the player
     */
    @Override
    public void move(float x, float y) {
        super.move(x,y); 
        // if (x != 0) onGround = false;
        this.notifyObservers(new ObservableEvent(new Vector2f(x,y), Hero.EVENT_HERO_MOVED));
        // System.out.println("Jumping: " + isJumping + " and onTheGround: " + onGround);
        //this.onGround = false;
    }

    /**
     * Method for having the player jump.
     * It is NOT a direct toggle method because the logic would lead to some weird bugs with stopping jumping.
     */
    public void jumpOn()
    {
        // check to see if already jumping, and if position is on the ground
        if (!isJumping && onGround)
        {
            isJumping = true;
            //System.out.println("Jump has happened!");
        }
    }

    /**
     * Method for stopping the player's jump.
     */
    public void jumpOff()
    {
        if (isJumping)
        {
            isJumping = false;
        }
    }

    /**
     * Returns whether the player is currently strafing.
     * [Deprecated] because the moving left and right indicators are no longer accurate
     *
     * @return boolean, true if isMovingLeft or isMovingRight is true; otherwise false.
     */
    public boolean getStrafingStatus()
    {
        return (isMovingLeft || isMovingRight);
    }

    ///////////////////////////////////////////////////////////////////////////////////
    // Health
    ///////////////////////////////////////////////////////////////////////////////////

    /**
     * Method for handling the player's death sequence.
     * Most likely to be called internally, but can be called externally for testing or other reasons.
     */
    @Override
    public void die()
    {
        if (healthPoints > 0)
        {
            healthPoints = 0;
        }
        this.controller.setScore(calculateScore());
        this.controller.deathScreen.updateScore();
        this.controller.deathScreen.highScoreCheck();
        this.controller.end(false);
        // this.controller.setGameState(GameState.DEATH);
    }

    /**
     * First method for making the player take damage, the default version.
     * Takes one health point away from the player.
     */
    public void takeDamage() { 
        takeDamage(1);
    }

    /**
     * Second method for player damage, this time a value can be given for the amount of damage.
     * Damage given must be checked before calling the method to make sure it is not more than intended.
     *
     * @param damage the damage to be dealt to the player (if negative, health points increase)
     * @see #heal(int)
     */
    @Override
    public void takeDamage(int damage){
        this.takeDamage(damage, false);
    }

    /**
     * 
     * @param damage
     */
    public void takeDamage(int damage, boolean reset) {
        if (damage < 0) {
            this.heal(-damage); 
            return;
        } else if (damage == 0) return;

        if (this.invulnerable) return;

        // if (this.invulnerable && !override) return;

        healthPoints-=damage;
        // Update the UI
        this.controller.ui.getHealthBar().loseHealth(damage);

        // If dead, die and move on with your life (or death)
        // Otherwise, reset the hero at a given position
        if (isDead()) {
            die();
        } else {
            // System.out.println("Ouch!");
            //System.out.println("Ouch!");
            playDamageSound();
            if (reset) this.resetPosition(50f);
            // They become invulnerable - check invulnerability end in tick
            this.invulnerable = true;
            this.invulnerabilityTimer = 0; // reset the timer
        }
    }

    /**
     * Method to play damage sound.
     * Generates a random int to randomly play a hit sound based on the int generated:
     * e.g. hit_ + randomNum(1) = hit_1, hit_ + randomNum(2) = hit_2, etc
     */
    private void playDamageSound()
    {
        int randomNum = ThreadLocalRandom.current().nextInt(1, 3);
        soundManager.playSound("hit_" + randomNum);
    }

    /**
     * Makes this player gain health points
     * 
     * @param healthGained the amount of health the player will gain
     */
    public void heal(int healthGained) {
        if (healthGained < 0) {
            this.takeDamage(-healthGained);
            return;
        }
        // int stock = 
        if (this.getInventoryOf(CritterType.POTION) < 1 || this.healthPoints == HEALTH_MAX) {
            // System.out.println("You can't afford/don't need this right now");
            return;
        }
        if (!this.spend(CritterType.POTION, healthGained)) {
            // System.out.println("You can't");
            return;
        }
        // System.out.println("I know right!");
        if (healthGained + healthPoints > HEALTH_MAX) {
            healthPoints = HEALTH_MAX;
        } else
        {
            healthPoints += healthGained;
        }
        soundManager.playSound("potion_pickup_1");
        //System.out.println("Music gets played");

        // Update the UI 
        this.controller.ui.getHealthBar().gainHealth(healthGained);
    }

    public void healTick()
    {
        if (Keyboard.isKeyPressed(Keyboard.Key.H))
        {
            //System.out.println(healing);
            if (!healing)
            {
                healing = true;
                heal(1);
            }
            return;
        }
        healing = false;
    }

    /**
     * Method for making the player do damage to a damageable object.
     *
     * @param damage the damage the player will deal to the critter.
     * @param adversary the critter to whom the damage will be dealt
     */
    @Override
    public void inflictDamage(int damage, Damageable adversary)
    {
        adversary.takeDamage(damage);
    }

    /**
     * Returns the current health points of the player.
     *
     * @return int, player's health points.
     */
    @Override
    public int getHealth()
    {
        return healthPoints;
    }

    /**
     * Returns whether the player is dead.
     *
     * @return boolean, whether the player's health points are 0 or below.
     */
    @Override
    public boolean isDead()
    {
        return healthPoints <= 0;
    }

    /**
     * Returns whether the player's health is at its allowed maximum.
     *
     * @return boolean, whether health points are equal to the maximum health.
     */
    @Override
    public boolean isHealthFull()
    {
        return healthPoints == HEALTH_MAX;
    }

    ///////////////////////////////////////////////////////////////////////////////////
    // Collision // [Deprecated]
    ///////////////////////////////////////////////////////////////////////////////////

    /**
     * Method that registers a collision with the player from another object.
     * To be used when the collision does not cause damage.
     *
     * @param direction the direction the collision takes place, from 1 - 4 in the order Up, Right, Down, Left.
     */
    public void causeCollision(int direction)
    {
        return;
    }

    /**
     * Second iteration of method that registers a collision with player from another object.
     * With this method, damage can be added. Useful for harmful obstacles and static enemies.
     *
     * @param direction the direction the collision takes place, from 1 - 4 in the order Up, Right, Down, Left.
     * @param damage the damage that the player will take.
     */
    public void causeCollision(int direction, int damage)
    {
        takeDamage(damage);
    }

    ///////////////////////////////////////////////////////////////////////////////////
    // Scene and Placement
    ///////////////////////////////////////////////////////////////////////////////////

    /**
     * Method for placing the player in the game window/scene.
     *
     * @param scene the platformer on which this SceneElement is to be placed
     * @param platformPosition the position of the bottom left corner of a platform
     */
    @Override
    public void placeInScene(AbstractScene scene, Vector2f platformPosition)
    {
        this.setPosition(platformPosition.x, platformPosition.y );
    }

    /**
     * NOTE: Not sure how this method differs to the placeInScene methods.
     *
     * @param platformer
     * @param platformPosition
     */
    @Override
    public void place(Platformer platformer, Vector2f platformPosition)
    {
        this.setPosition(platformPosition.x, platformPosition.y - getBounds().height);
    }

    /**
     * Obtains the position, within the window's coordinate's system
     * with respect to where it is in its view
     * 
     * @param window
     * @return
     * @see #getView()
     */
    public Vector2i getPixelPosition(RenderWindow window) {
        return window.mapCoordsToPixel(this.getPosition(), this.getView());
    }

    // @Override
    // public IntRect getPixelBounds(Game game) {
    //     // return new IntRect( getPixelPosition(game.window), new Vector2i(getSize()));
    //     Vector2i pixels = game.window.mapCoordsToPixel(this.getPosition(), game.scene.view);
    //     Vector2i size = new Vector2i(getSize());
    //     return new IntRect(pixels, size);
    // }

    /**
     * The coordinates system in which this hero lives
     * @return
     */
    public View getView() {
        return this.view;
    }

    /**
     * Sets a new coordinate system for this hero
     * @param view
     */
    public void setView(View view) {
        this.view = view;
    }

    /**
     * Resets this hero at its start position, making him fall from a given height
     * to the bottom floor. 
     * 
     * @param height the distance above the floor from which this hero falls
     */
    public void resetPosition(float height) {
        Game game = this.controller;
        final Vector2i pixelStartPos = new Vector2i(game.scene.getSceneSize().x/3, 
            (int)(game.scene.getFloorPosition(0)- this.getBounds().height));
        float y = game.scene.getFloorPosition(0) - this.getBounds().height;
        Vector2f coordinates = game.window.mapPixelToCoords(pixelStartPos, getView());
        this.setPosition(coordinates.x,y-height);
    }

    /**
     * Method for resetting all the movement indicators of the player
     */
    public void resetMovement(){
        isMovingLeft = false;
        isMovingRight = false;
        isJumping = false;
    }

    /**
     * Method for having the player collect a collectible
     *
     * @param type collectible type
     * @param value how many of the collectible the player has collected
     */
    public void collect(CritterType type, int value) {
        if (!type.isCollectible()) return;

        int newAmt = value + inventory.getOrDefault(type, 0) ;
        this.inventory.replace(type, newAmt);
        // controller.ui.setValue(type, newAmt);
    }

    /**
     * Method for spending coins on a collectible
     *
     * @param type type of collectible
     * @param value how much is being spent
     * @return boolean based on if purchase is successful
     */
    public boolean spend(CritterType type, int value) {
        if (!type.isCollectible()) return false;
        if (type==CritterType.KEY) return false;

        int left = inventory.getOrDefault(type,0) - value;
        if (left < 0) return false;

        // Spend it away
        inventory.replace(type, left);
        // controller.ui.setValue(type, left);
        return true;
    }

    /**
     * Purchases an item of this type
     * 
     * @param type
     * @param price
     * @param value the value of the item being purchased
     * @return
     */
    public boolean purchase(CritterType type, int price, int value) {
        if (!type.isCollectible()) return false;
        
        boolean enough = this.spend(CritterType.COIN, price);
        if (enough) {
            this.collect(type, value);
        }
        return enough;
    }

    /**
     * Method to get the inventory amount of a particular type of collectible the player has
     *
     * @param type type of collectible to be checked for
     * @return amount of the collectible the player has in inventory
     */
    public int getInventoryOf(CritterType type) {
        if (!type.isCollectible()) return 0;
        return this.inventory.getOrDefault(type, 0);
    }

    @Override
    public CritterType getCritterType() {
        return CritterType.HERO;
    }

    ///////////////////////// movement ///////////////////////////

    /**
     * Toggle method for making the player move right.
     * [Deprecated]
     */
    /*public void rightMovement()
    {
        this.isMovingRight = !isMovingRight;
    }*/

    /**
     * Toggle method for making the player move left.
     * [Deprecated]
     */
    /*public void leftMovement()
    {
        this.isMovingLeft = !isMovingLeft;
    }*/

    /**
     * Method for ticking over all player movement
     */
    private void playerMovement()
    {
        strafe();
        jump();
    }


    /**
     * Method that effects the player with the variables of the in-game gravity.
     * NOTE: Gravity is currently set internally, but may be better to have an overall world gravity set in the game controller
     * and share that value across both the player and the other critters that will require gravity.
     */
    private void gravityEffect()
    {
        if (velocityY < -0.8)
        {
            velocityY = -1.6f; // set to -1.8 to maintain a slight upwards speed if there previously was one, less sudden of a drop
            // System.out.println("Velocity = " + velocityY);
        }

        velocityY += gravity;

        this.move(0, velocityY); // move player down by falling speed
    }

    /**
     * Method for handling strafing (left/right) movement within the player.
     * The method is called every tick.
     * It polls the keyboard directly for which button is being pressed, so keyboard presses don't have to be worried about
     * outside of the player class
     */
    private void strafe()
    {
        if (Keyboard.isKeyPressed(Keyboard.Key.A) && Keyboard.isKeyPressed(Keyboard.Key.D))
        {
            // Do nothing, both keys pressed
            ;
        } else if (Keyboard.isKeyPressed(Keyboard.Key.A))
        {
            //isMovingLeft = true;
            if (!onGround)
            {
                moveLeft(accelerationX/2); // half acceleration in the air
            } else
            {
                moveLeft(accelerationX);
            }
        } else if (Keyboard.isKeyPressed(Keyboard.Key.D))
        {
            //isMovingRight = true;
            if (!onGround)
            {
                moveRight(accelerationX / 2); // half acceleration in the air
            } else
            {
                moveRight(accelerationX);
            }
        } else
        {
            velocityX = 0;
            //decelerate();
        }
    }

    // Had a go at deceleration for the player, seems unnecessary with our game though. Causes issues with velocity not being 0 when key is pressed
    // (very) shortly after releasing the opposite key too
    /*
    private void decelerate()
    {
        if (isMovingRight) // if moving right, slowly decelerate to 0 with acceleration variable
        {
            if (velocityX > 0) // if velocity is above 0 (moving right)
            {
                velocityX -= accelerationX;
            } else
            {
                velocityX = 0;
            }
        } else if (isMovingLeft) // if moving left, slowly decelerate to 0 with acceleration variable
        {
            if (velocityX < 0) // if velocity is less than 0 (moving left)
            {
                velocityX += accelerationX;
            } else
            {
                velocityX = 0;
            }
        } else // if not moving left or right (for whatever reason) set velocity to 0
        {
            velocityX = 0;
        }
        // could set isMovingLeft and isMovingRight to false when velocity is 0, but should be handled in tick with the checking if statement
    }*/

    /**
     * Method for moving the player towards the left by a certain acceleration
     *
     * @param accelerationX the amount of acceleration the player moves by
     */
    public void moveLeft(float accelerationX){
        if(velocityX >= -maxVelocity)
        {
            velocityX -= accelerationX;
        } else
        {
            velocityX = -maxVelocity;
        }
        // System.out.println("moving left");
        this.move(velocityX, 0);
    }

    /**
     * Method for moving the player towards the right by a certain acceleration
     *
     * @param accelerationX the amount of acceleration the player moves by
     */
    public void moveRight(float accelerationX){
        if(velocityX <= maxVelocity)
        {
            velocityX += accelerationX;
        } else
        {
            velocityX = maxVelocity;
        }
        // System.out.println("moving right");
        this.move(velocityX, 0);
    }

    /**
     * Method for making the player jump.
     * Player's velocity is set directly to the jump strength, and then slowly reduces due to the effect of gravity.
     * Produces an arc due to using velocity.
     *
     * If isJumping is false, this method is no longer called and instead gravity is applied to the player. Allows for the player
     * to stop their jump midway and begin to arc back down.
     */
    public void jump(){
        if(this.isOnGround() && isJumping){
            //System.out.println("jump");
            velocityY = -jumpStrength;
        }else if(this.isOnGround()){
            velocityY = 0;
        } else {
            // System.out.println("falling");
            velocityY += gravity;
        }
        if (velocityY == 0)
        {
            isJumping = false;
        }

        this.move(0, velocityY);
    }


    ////////////////////////shooting methods//////////////////////////////////

    /**
     * Method that creates a projectile with a certain direction.
     * This projectile is added to a list of other projectiles.
     *
     * @param direction integer direction for the projectile 1 = right, 0 = left.
     */
    public void shoot(int direction){
        Projectile bullet = new Projectile(direction, new Vector2f(this.getPosition().x, this.getPosition().y + this.getGlobalBounds().height/2));
        controller.scene.addToPlatformer(bullet);
        bullet.addToRenderer(controller.gameRenderer);
        Bullets.add(bullet);
    }

    /**
     * Method that updates the bullet movement by a set speed.
     *
     * @param Speed float speed at which the projectile will move each frame.
     */
    private void updateBullet(float Speed){
        for(Projectile bullet:Bullets){
            bullet.move(Speed);

        }
    }

    /**
     * Method that checks if the player can shoot again.
     * waits a set delay before the player can shoot.
     *
     */
    private void shootCheck(){
        if(tempDelay > 0){
            tempDelay --;
        }
        else{
            canShoot = true;
        }
    }

    public LinkedList<Projectile> getBullets(){
        return Bullets;
    }

    /**
     * Method that calculates the player's score based on their stats.
     *
     * @return integer score the player is at currently.
     */
    public int calculateScore(){
        int coins = this.getInventoryOf(Critter.CritterType.COIN);
        int chapter = controller.gameFlow.getChapter();
        int ammo = this.getInventoryOf(Critter.CritterType.WEAPON);
        int potions = this.getInventoryOf(Critter.CritterType.POTION);
        float playTime = controller.getPlayTime();
        return (int)(((coins+ammo+potions)+0.1)*playTime*chapter);
    }
}
