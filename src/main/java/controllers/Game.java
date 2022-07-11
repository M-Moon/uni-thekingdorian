package controllers;

import java.io.IOException;
import java.nio.file.Paths;
// import java.time.*;
import java.util.Random;

import org.jsfml.graphics.Image;
import org.jsfml.graphics.IntRect;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.Texture;
import org.jsfml.system.Clock;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;
import org.jsfml.window.Keyboard;
import org.jsfml.window.VideoMode;
import org.jsfml.window.Keyboard.Key;
import org.jsfml.window.Mouse.Button;
import org.jsfml.window.event.Event;

import Collectibles.Chest;
import platformer.*;
import platformer.GameScene.SceneType;
import screens.*;
import assets.*;
import hero.Hero;
import managers.collectiblemanagers.WeaponryManager;
import managers.crittermanagers.GuideManager;
import npc.Guide;
import npc.Merchant;
import sound.SoundManager;
import ui.PauseOverlay;
import ui.UIOverlay;



/**
 * An instance of our game
 */
public class Game 
{
    public final RenderWindow window;
    public final HomeScreen homescreen;
    public final DeathScreen deathScreen;
    public final UIOverlay ui;
    public final PauseOverlay pauseOverlay;
    public final GameAudio gameSounds = new GameAudio();

    // View
    public final GameRenderer gameRenderer;
    public final GameScene scene;
    public final TextureCatalogue GRAPHICS = TextureCatalogue.get();
    public final Hero hero;
    // Critter management
    public final GameManager critterManager;
    

    private boolean hit = false;

    private int score = 0;

    
    // Time management
    private Clock clock;
    /**
     * Whether the clock is still running. The clock may stop to run because the gameMode!=GameState.PLAY
     * or for any other reason (e.g., transitions)
     * @see #playTime
     */
    private boolean clockRunning;       // whether the clock is still running
    /**
     * The number of seconds that the actual game play has been going on
     */
    private float playTime = 0;

    
    /** A class which periodically checks how the game progresses */
    public final GameFlow gameFlow;
    private GameState gameMode;
    private boolean hasBeenInitialized = false;

    int trials = 1;
    Tile testTile;
    public Game(RenderWindow window)
    {
        this.window = window;
        this.window.create(new VideoMode(1300, 900), "Keymasters", RenderWindow.CLOSE|RenderWindow.TITLEBAR);
        this.window.setFramerateLimit(60);
        this.window.setKeyRepeatEnabled(false);

        // Set the icon:
        Image icon = new Image();
        try { icon.loadFromFile(Paths.get("resources/placeholder/armisius_Skeleton_Key_Icon.png"));}
        catch (IOException e) { try {  icon.loadFromFile(Paths.get("resources/placeholder/armisius_Skeleton_Key_Icon.png")); } 
        catch (IOException f) { System.out.println("Failed to load icon");} }
        this.window.setIcon(icon);

        this.gameRenderer = new GameRenderer(this);
        this.clock = new Clock();

        // Create the various screens
        this.homescreen = new HomeScreen(this);
        // System.out.println("Got past the homescreen");
        this.scene = new GameScene(this);
        this.ui = new UIOverlay(this);
        this.deathScreen = new DeathScreen(this);
        this.pauseOverlay = new PauseOverlay(this);
        // this.menuScreen;

        // main player
        this.hero = new Hero(new Vector2f(0, 0), new Vector2i(50, 50), this);
        this.critterManager = GameManager.get(this);
        this.gameFlow = new GameFlow(this);

        this.gameSounds.setMusicVolume(50);

        this.setGameState(GameState.HOME_SCREEN);
    }

    public void start() throws IOException {
        if (!hasBeenInitialized) {
            this.initializeAll();
        }

        // Clock for time measurement
        // Start the clock for the spawners
        // We keep track of how long between the ticks
        this.clock = new Clock();
        while (window.isOpen())
        {
            // critterManager.tickCritters(clock.getElapsedTime().asMilliseconds());
            // Handle events
            for (Event event : window.pollEvents()) {
                // Dispatch event to the appropriate class
                // event will be handled, model will be updated as a response to user input
                this.handleEvent(event);
                // ui.HandleEvent(event, this);
            }

            // System.out.println("The view is being updated: the UI elements are updated");
            this.updateModel();     // Gets updates from the main controller and critter pool

            this.window.clear();
            // System.out.println("The scenes are being redrawn");
            this.gameRenderer.render(window);
            // System.out.println("Displaying the window");
            this.window.display();
        }
    }

    /**
     * Initialize everything
     */
    protected void initializeAll() {

        if (hasBeenInitialized) return;

        // Create the background
        // this.scene.setTexture(GRAPHICS.TEMP_BG_01);
        this.scene.setPlatformerDimensions(3, new Vector2i(120,80), new Vector2i(window.getSize().x,200));
        this.scene.setPosition(Vector2f.ZERO);
        this.scene.setSceneSize(window.getSize());
        this.scene.createView(window);
        // this.scene.setSceneType(SceneType.GROUND);
        // this.scene.setScrollingSpeed(new Vector2f(2,0));
        // System.out.println("Dimensions: level " + scene.getPlatformLevelSize() + ", tiles: " + scene.getPlatformTileSize()+ ", platformer height "+ scene.computePlatformerHeight());
        // Add it to the rendering list
        gameRenderer.add(ui);
        this.scene.addToRenderer(this.gameRenderer); 

        // Place the hero at the bottom (platform 0)
        this.hero.setLayer(4);
        this.hero.setView(scene.view);
        // this.hero.resetPosition(this);
        // this.hero.resetPosition(50f);
        // System.out.println("Hero ("+ hero.getPixelPosition(window)+") view: " + hero.getView().getCenter() +", " + hero.getView().getSize());
        hero.addObserver(this.gameRenderer);
        hero.addToRenderer(this.gameRenderer);
        
            
        // Start with a few tiles at a slow pace
        // this.critterManager.setManagerParameter(GameManager.MANAGER_TILE, new Vector2i(1,3), new Vector2f(2,5));
        // this.critterManager.setManagerParameter(GameManager.MANAGER_OBSTACLE, new Vector2i(1,3), new Vector2f(2,6));
        // this.critterManager.setManagerParameter(GameManager.MANAGER_ENEMY, new Vector2i(1,2), new Vector2f(3,5));
        // this.critterManager.setManagerParameter(GameManager.MANAGER_GUIDE, new Vector2i(1,2), new Vector2f(3,5));

        // Initial parameters
        this.reset();

        this.hasBeenInitialized = true;
    }

    /**
     * Creates the initial parameters for a game
     */
    public void reset() {
        // Empty the manager
        this.critterManager.resetManager();
        // this.gameRenderer.resetRenderer();
        // Sets the ground scene
        this.scene.reset();
        this.scene.setSceneType(SceneType.GROUND);
        this.scene.setScrollingSpeed(new Vector2f(5,0));
        this.scene.addToRenderer(gameRenderer);
        // for (int i=0;i<3;i++)System.out.println("i="+sscene.getFloorPosition(i));
        // Puts the player in the right place
        this.hero.resetPosition(50f);
        this.hero.resetHero();
        // this.hero.heal(6);
        // this.hero.resetMovement();
        this.hero.addToRenderer(gameRenderer);
        this.ui.reset();

        score = 0;
        

        // Restarts the playtime
        this.playTime = 0;
        // Re-initialize the game flow
        this.gameFlow.restart();

        // Resets the appropriate critters
        // this.critterManager.setManagerParameter(GameManager.MANAGER_TILE, new Vector2i(1,3), new Vector2f(5,6));
        // this.critterManager.setManagerParameter(GameManager.MANAGER_OBSTACLE, new Vector2i(1,2), new Vector2f(4,6));
        // this.critterManager.setManagerParameter(GameManager.MANAGER_ENEMY, new Vector2i(1,2), new Vector2f(3,5));
        // this.critterManager.setManagerParameter(GameManager.MANAGER_GUIDE, new Vector2i(1,2), new Vector2f(7,10));
        this.critterManager.setManagerParameter(GameManager.MANAGER_COINS, new Vector2i(2,4), new Vector2f(5,8));
        this.critterManager.setManagerParameter(GameManager.MANAGER_WEAPONS, new Vector2i(2,4), new Vector2f(5,8));
        // this.critterManager.setManagerParameter(GameManager.MANAGER_POTIONS, new Vector2i(1,3), new Vector2f(5,10));
    
        // Test tile
        // testTile = new Tile(this);
        // testTile.setPlatformLevel(1);
        // scene.addToPlatformer(testTile);
        // testTile.addToRenderer(gameRenderer);
        // critterManager.addActive(testTile);
        // System.out.println("Guide can spawn? " + GuideManager.get(this).isActive());
    }

    /**
     * Starts the actual gameplay. 
     * <p> This method should be called externally by the homescreen.
     * 
     * @param heroTexture the texture chosen by the player in the home screen
     * @see HomeScreen
     */
    public void startGame(Texture heroTexture) 
    {
        // Initialize the scene
        // and UI Overlay : init it here, then call this.gameRenderer.addDrawable(overlay)
        // call intializeAll()


        // Set the appropriate hero's texture
        // Start the game flow

        // Remove the homescreen from the rendering list
        // this.homescreen.removeFromRenderer(this.gameRenderer);
        this.setGameState(GameState.PLAY);
    }

    /**
     * Handles an event from the window.
     * This method may dispatch events to external classes depending on the game state.
     * 
     * @param event an Event to be handled
     * 
     * @see GameState for more on game states
     */
    public void handleEvent(Event event)
    {
        if (this.gameMode == GameState.HOME_SCREEN) {
            // Delegate to the home screen and return
            this.homescreen.handleEvent(event);
            return;
        }

        if(this.gameMode == GameState.DEATH){
            this.deathScreen.handleEvent(event);
            return;
        }

        if (this.gameMode == GameState.PAUSED) {
            this.pauseOverlay.HandleEvent(event, this);
            return;
        }

        if (this.gameMode == GameState.PLAY) {
            // Delegate to the ui bar
            // this.ui.HandleEvent(event, this);
            // Check for interaction events
            // this.critterManager.guideManager.handleEvent(event);
            Guide.get(this).getZone().handleEvent(event);
            Merchant.get(this).getZone().handleEvent(event);
        }

        switch (event.type) {
            case CLOSED:
                this.window.close();
                break;
            case KEY_PRESSED:
                Keyboard.Key kp = event.asKeyEvent().key;
                // System.out.println("Key pressed " + kp);
                switch (kp)
                {
                    case P:
                        this.setGameState(this.gameMode==GameState.PAUSED ? GameState.PLAY : GameState.PAUSED);
                        break;
                    case L:
                        // this.hero.die();
                        // WeaponryManager.get(this).launchWeapon();
                        break;
                    case M:
                        gameSounds.playEffect("success_1");
                        break;
                    case B:
                        Merchant.get(this).enterZone();
                    case D:
                        break;
                    case A:
                        break;
                    case W:
                        hero.jumpOn();
                        break;
                    case I:
                        // critterManager.guideManager.handleEvent(event);
                        // critterManager.guideManger.ha
                        // System.out.println("There are  " + critterManager.activeCritters.size() + "critters");
                        break;
                    case C:
                        Guide.get(this).enterZone();
                        // this.scene.travel(new Random().next() GameScene.SceneType.DUNGEON);
                        // this.scene.travel(SceneType.DUNGEON);
                        break;
                    case V:
                        Chest.get(this).openChest();
                        break;
                    case Z:
                        this.reset();
                        break;
                    case F:
                        System.out.println("Falling? : " + this.hero.isFallingDown()+" (J="+hero.isJumping()+", G="+hero.isOnGround()+")");
                        break;
                    case R: 
                        System.out.print("Position of hero (coords) " + hero.getPosition());
                        System.out.println(" vs. (to pixels) " + hero.getPixelPosition(window));
                        break;
                    case T:
                        if (event.asKeyEvent().shift) scene.setScrollingSpeed(Vector2f.ZERO);
                        else System.out.println("Holds hero? " + testTile.testTick(this,hero));
                        break;
                    default: break;
                }
                break;
            case KEY_RELEASED:
                Keyboard.Key kr = event.asKeyEvent().key;
                switch(kr)
                {
                    case D:
                        break;
                    case A:
                        break;
                    case W:
                        hero.jumpOff();
                        // System.out.print("Position of hero (coords) " + hero.getPosition());
                        // System.out.println(" vs. (to pixels) " + hero.getPixelPosition(window));
                        break;
                    default: break;
                }
                break;
            case MOUSE_BUTTON_PRESSED:
                Vector2i position = event.asMouseButtonEvent().position;
                System.out.println("Mouse position at " + event.asMouseButtonEvent().position);
                // if (event.asMouseButtonEvent().button==Button.RIGHT) testTile.setPosition(window.mapPixelToCoords(position, scene.view));
                hero.setPosition(window.mapPixelToCoords(position, hero.getView()));
                break;
            default: break;
        }
    }

    /**
     * Stops and starts the clock
     * 
     * @param running
     * @return the current play time recorded
     */
    public float setClockState(boolean running) {
        if (running != this.clockRunning) this.clock.restart();
        this.clockRunning = running;
        // if (clockRunning) this.clock.restart();
        return this.playTime;
    }

    
	public boolean clockRunning() {
		return this.clockRunning;
	}

    /**
     * Represents a tick of the game frame.
     */
    public void updateModel() {

        // if (!this.clockRunning()) {
        //     // System.out.println("Not running");
        //     return;
        // }

        // Gravity, scrolling, etc.
        // Spawn what needs to be spawned

        // Playing time
        // if (this.gameMode == GameState.PLAY) {
        //     this.playTime += this.clock.restart().asSeconds();
        // }

        if (this.clockRunning) {
            this.playTime += this.clock.restart().asSeconds();
        }

        if (this.gameMode.hasEnded()) {
            return;
        }

        // Gets updates from the game flow
        this.gameFlow.checkProgress(playTime);
        // Get the critters to be spawned and add them to the scene
        this.critterManager.spawnInGame(playTime); 

        // Scroll the background
        this.scene.scroll();

        // Tick the critters
        this.critterManager.tickCritters(playTime);
        this.ui.updateUI(this.hero);
    }

    public GameState getGameState() {
        return this.gameMode;
    }

    /**
     * Changes the state of this game. 
     * <p>
     * GameState follows what is happening in the application, as well as what is
     * rendered on screen.
     * 
     * @param state the new state of the game
     * 
     */
    public void setGameState(GameState state) {
        this.gameMode = state;
        switch (this.gameMode) {
            case HOME_SCREEN:
                gameSounds.playMusic("marcelo_fernandez_anewheroisrising_intromusic.wav");
                // this.homescreen.addToRenderer(gameRenderer);
                break;
            case PLAY:
                this.setClockState(true);
                gameSounds.stopMusic();
                clock.restart();
                break;
            case PAUSED:
                this.setClockState(false);
                break;
            case DEATH:
                this.setClockState(false);
                // this.reset();
                // this.deathScreen.addToRenderer(gameRenderer);
                break;
            case END:
                this.setClockState(false);
                break;
            default: break;
        }
    }

    /**
     * Starts the end sequence for the game.
     * 
     * @param victorious whether the player has won
     */
    public void end(boolean victorious) {
        if (victorious) {
            System.out.println("You have saved us");
        } else {
            System.out.println("You lost. We're dead");
            this.setGameState(GameState.DEATH);
        }
        // this.setGameState(GameState.END);
    }

    public GameState getState() {
        return this.gameMode;
    }

    /**
     * Obtains the total time played
     */
    public float getPlayTime() {
        return this.playTime;
    }

    public UIOverlay getUI(){
        return ui;
    }

    public boolean getHit(){
        return hit;
    }

    public void setHit(Boolean bool){
        hit = bool;

    }

    public void waitHit(){
        long timer = 100;
        while(timer > 0){
            System.out.println(timer);
            timer = timer - System.nanoTime();
        }
        hit = false;
    }

    public void setInitialized(Boolean b){
        hasBeenInitialized = b;
    }

    public void restart(){
        System.out.println("Restarting");
        this.reset();
        // this.deathScreen.removeFromRenderer(gameRenderer);
        this.setGameState(GameState.PLAY);
    }

    public IntRect getDisplayBounds() {
        return new IntRect(Vector2i.ZERO, this.window.getSize());
    }

    public int getScore(){
        return score;
    }

    public void setScore(int newScore){
        this.score = newScore;
    }

}
