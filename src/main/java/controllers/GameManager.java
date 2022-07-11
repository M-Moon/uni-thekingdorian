package controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.jsfml.graphics.IntRect;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;

import devtools.*;
import fighter.Enemy;
import managers.collectiblemanagers.*;
import managers.crittermanagers.*;
import models.Critter;
import platformer.GameScene;
import platformer.Tile;

public class GameManager implements Observer {
    private static GameManager INSTANCE = null;

    // Events
    public static final int EVENT_RELEASED_CRITTER = Critter.EVENT_CRITTER_RELEASED;
    public static final int EVENT_ACQUIRED_CRITTER = Critter.EVENT_ACQUIRED_CRITTER;
    public static final int EVENT_TILE_REQUEST = 103;
    public static final int EVENT_HERO_LANDING = 102;


    // Managers
    /** The ID for the spawning constraints of the tiles */
    public static final String MANAGER_TILE = "6_TILE";
    /** The ID for the spawning constraints of the coins */
    public static final String MANAGER_COINS = "2_COINS";
    /** The ID for the spawning constraints of the collectibles */
    public static final String MANAGER_POTIONS = "2_POTIONS";
    /** The ID for the spawning constraints of the enemies */
    public static final String MANAGER_ENEMY = "3_ENEMY";
    /** The ID for the spawning constraints of the obstacles */
    public static final String MANAGER_OBSTACLE = "1_OBSTACLE";
    /** The ID for the spawning constraints of the crossroads guide */
    public static final String MANAGER_GUIDE = "4_CROSSROADS GUIDE";
    /** The ID for the spawning constraints of the merchant */
    public static final String MANAGER_MERCHANTS = "5_MERCHANTS";
    /** The ID for the spawning constraints of the weapons */
    public static final String MANAGER_WEAPONS = "5_WEAPONS";
    

    public final Game controller;
    public final List<Critter> activeCritters;

    // Time management
    private float lastTime = 0f;

    // Individual managers
    final TileManager tileManager;
    final EnemyManager enemyManager;
    final ObstacleManager obstacleManager;
    final CoinManager coinManager;
    final PotionsManager potionManager;
    final WeaponryManager weaponManager;
    final GuideManager guideManager;
    final MerchantManager merchantManager;
    final KeyManager keyManager;

    private boolean heroLanded = false;

    protected GameManager(Game controller) {
        this.controller = controller;
        this.activeCritters = new ArrayList<>();

        // Start monitoring the scene
        tileManager = TileManager.get(controller);
        this.controller.scene.addObserver(this);
        this.controller.scene.setSceneBuilder(tileManager);
        // this.controller.scene.addObserver(this);

        // Spawn coins
        coinManager = CoinManager.get(this.controller);

        // Spawn obstacles (second to be activated with potions)
        potionManager = PotionsManager.get(this.controller);
        obstacleManager = ObstacleManager.get(this.controller);

        // Spawn weapons along with enemies
        weaponManager = WeaponryManager.get(this.controller);


        // Crossroads guide
        guideManager = GuideManager.get(this.controller);
        merchantManager = MerchantManager.get(this.controller);
        
        enemyManager=EnemyManager.get(this.controller);
        keyManager = KeyManager.get(this.controller);
    }

    public static final GameManager get(Game game) {
        if (INSTANCE == null) {
            INSTANCE = new GameManager(game);
        }
        return INSTANCE;
    }

    /**
     * Spawns new critters during a tick of the game. 
     * <p>
     * This method typically calls each of its managers (in a given order)
     * to spawn a specific type of Critter, and returns the list of it all.
     * 
     * @param time the total number of seconds since the game started ( playtime )
     * @return a list of newly spawned critter
     */
    public List<Critter> spawn(float time) {
        List<Critter> toSpawn = new ArrayList<>();
        if (!controller.clockRunning()) return toSpawn;
        // Ask every spawner to spawn
        toSpawn.addAll(keyManager.spawn(time));
        toSpawn.addAll(tileManager.spawn(time));
        toSpawn.addAll(coinManager.spawn(time));
        toSpawn.addAll(potionManager.spawn(time));
        toSpawn.addAll(weaponManager.spawn(time));
        toSpawn.addAll(obstacleManager.spawn(time));
        toSpawn.addAll(guideManager.spawn(time));
        toSpawn.addAll(enemyManager.spawn(time));
        toSpawn.addAll(merchantManager.spawn(time)); 
        Collections.shuffle(toSpawn);             // Shuffle them so they don't come in the same order
        return toSpawn;
    }

    public void addActive(Critter critter) {
        this.activeCritters.add(critter);
    }

    /**
     * Spawns all critters and adds them to the scene and the renderer for this Game.
     * <p> 
     * This method internally calls {@link #spawn(float)}, so DO NOT CALL IT AGAIN
     * during the same tick. 
     * @param time
     */
    public void spawnInGame(float time) {
        this.spawn(time).forEach((Critter critter) -> {
            // Adds the critter to the platformer
            this.controller.scene.addToPlatformer(critter, this.controller.window);
            // Adds the critter to the renderer
            critter.addToRenderer(controller.gameRenderer);
            // Adds it to the active critters
            this.activeCritters.add(critter);
        });
    }

    /**
     * Sets the spawning constraints for a specific type of critter to be spawned
     * 
     * @param managerName the type of critter to be spawned
     * @param minMaxCount the minimum and maximum number of elements to be spawned at once
     * @param minMaxTime the minimum and maximum nb of seconds between each spawn
     * 
     * @see static fields of this GameManager
     */
    public void setManagerParameter(String managerName, Vector2i minMaxCount, Vector2f minMaxTime) {
        if (managerName.equalsIgnoreCase(MANAGER_TILE)) {
            this.tileManager.setSpawnCountLimit(minMaxCount.x, minMaxCount.y);
            this.tileManager.setTimeBetweenSpawnsLimits(minMaxTime.x, minMaxTime.y);
        }
        else if (managerName.equalsIgnoreCase(MANAGER_COINS)) {
            this.coinManager.setSpawnCountLimit(minMaxCount.x, minMaxCount.y);
            this.coinManager.setTimeBetweenSpawnsLimits(minMaxTime.x, minMaxTime.y);
        }
        else if (managerName.equalsIgnoreCase(MANAGER_OBSTACLE)) {
            this.obstacleManager.setSpawnCountLimit(minMaxCount.x, minMaxCount.y);
            this.obstacleManager.setTimeBetweenSpawnsLimits(minMaxTime.x, minMaxTime.y);
        }
        else if (managerName.equalsIgnoreCase(MANAGER_GUIDE)) {
            this.guideManager.setSpawnCountLimit(minMaxCount.x, minMaxCount.y);
            this.guideManager.setTimeBetweenSpawnsLimits(minMaxTime.x, minMaxTime.y);
        }
        else if (managerName.equalsIgnoreCase(MANAGER_ENEMY)) {
            this.enemyManager.setSpawnCountLimit(minMaxCount.x, minMaxCount.y);
            this.enemyManager.setTimeBetweenSpawnsLimits(minMaxTime.x, minMaxTime.y);
        }
        else if (managerName.equalsIgnoreCase(MANAGER_WEAPONS)) {
            this.weaponManager.setSpawnCountLimit(minMaxCount.x, minMaxCount.y);
            this.weaponManager.setTimeBetweenSpawnsLimits(minMaxTime.x, minMaxTime.y);
        }
        else if (managerName.equalsIgnoreCase(MANAGER_POTIONS)) {
            this.potionManager.setSpawnCountLimit(minMaxCount.x, minMaxCount.y);
            this.potionManager.setTimeBetweenSpawnsLimits(minMaxTime.x, minMaxTime.y);
        }
    }

    @Override
    public void receiveUpdate(ObservableEvent event) {
        int eventID = event.getEventID();
        Object source = event.getSource();

        if (eventID == GameScene.EVENT_SCENE_CHANGE) {
            // reset the platformer
        }

        // A Critter was acquired by the platformer, i.e., it became active
        // So we add it to the list of active critters so it can be ticked
        if (eventID == EVENT_ACQUIRED_CRITTER && source instanceof Critter) {
            this.activeCritters.add((Critter)event.getSource());
        }

        if (event.getEventID() == EVENT_HERO_LANDING) {
            this.heroLanded = true;
        }

        else if (event.getEventID() == EVENT_TILE_REQUEST) {
            this.tileManager.request(event);
        }

        // else if (eventID == GameScene.EVENT_FLOOR_SET && source instanceof Tile.Floor) {
        //     this.addActive((Tile.Floor)source);
        // }

    }

    /**
     * Reveals whether the hero has touched any tile during the tick
     * 
     * @return true only if the hero is on a tile during a tick
     */
    public boolean getLanding() {
        return this.heroLanded;
    }

    /**
     * 
     * @param playtime time since start of game
     */
    public void tickCritters(float playtime)
    {
        // Do not tick if the clock is not running
        if (!this.controller.clockRunning() || controller.getGameState().hasEnded()) {
            return;
        }

        final  float time = playtime - lastTime;

        try
        {
            for (Iterator<Critter> iterator = activeCritters.iterator(); iterator.hasNext();)
            {
                Critter crit = iterator.next();
                crit.tick(time, this.controller);
            }
        } catch (ConcurrentModificationException e)
        {
            activeCritters.clear();
            e.printStackTrace();
        }


        this.controller.hero.tick(time, controller);
        // if (controller.hero.getPixelBounds(controller).left < 0) {
        //     controller.hero.takeDamage();
        // }
        this.processBoundsLimits();
        
        // Remove the critters once they leave the screen
        this.activeCritters.removeIf(Predicate.not(Critter::isBeingRendered));
        // System.out.println(heroLanded);
        // Reset the landing switch
        this.heroLanded = false;
        // Update the time
        this.lastTime = playtime;
    } 

    /**
     * 
     */
    protected void processBoundsLimits() {
        final IntRect player = controller.hero.getPixelBounds(controller);
        final IntRect display = controller.getDisplayBounds();
        if (player.left < 0) {
            controller.hero.takeDamage(1,true);
        }
        if (!controller.scene.hasFloor(0) && player.top+player.height > display.height) {
            controller.hero.takeDamage(1,true);
            controller.scene.createTemporaryFloor(0,5);
        } 

    }

    /**
     * Resets the game critters. Empties the manager
     * <p>
     */
    public void empty() {
        // Remove from the renderer
        this.activeCritters.forEach(critter -> critter.removeFromRenderer(this.controller.gameRenderer));
        this.activeCritters.removeIf(Predicate.not(Critter::isBeingRendered));
    }

    public void resetManager() {
        this.empty();
        this.lastTime = 0;
        tileManager.reset();
        enemyManager.reset();
        coinManager.reset();
        obstacleManager.reset();
        weaponManager.reset();
        guideManager.reset();
        keyManager.reset();
    }

    public List<Critter> getActive() {
        return this.activeCritters;
    }

    public List<Enemy> getActiveEnemies(int lv) {
        return this.activeCritters.stream().filter(c -> c instanceof Enemy && c.getPlatformLevel()==lv)
            .map(Enemy.class::cast).collect(Collectors.toList());
    } 

}
