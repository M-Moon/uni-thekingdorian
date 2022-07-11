package managers;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;

import controllers.Game;
import devtools.ObservableEvent;
import devtools.Observer;
import models.Critter;
import platformer.Platformer;
import sys.Renderer;

public abstract class AbstractManager<T extends Critter> implements Spawner<T>, ReusablePool<T> 
{
    protected Game controller;
    public final Platformer platformer;
    public final Renderer renderer;

    /* ------ Spawner ---------------- */
    protected float minDeltaTime = 0;
    protected float maxDeltaTime = 7;
    protected int minSpawnCount = 1;
    protected int maxSpawnCount = 4;

    protected float lastTimeSpawned = 0;
    protected static Random random = new Random();

    /* ------ Pool ---------------- */
    protected List<T> poolElements = new ArrayList<>();

    /* ----- Observer ------------- */
    protected final Observer itemObserver;        // releases non rendered tiles
    /**
     * Whether the specific type is spawned
     */
    protected boolean active = false;              

    protected AbstractManager(Platformer platformer, Renderer renderer) {
        this.platformer = platformer;
        this.renderer = renderer;
        this.itemObserver = (ObservableEvent event) -> {
            T t1 = (T) event.getSource();
            if (! t1.isBeingRendered()) {
                release(t1);
            }	
        };
    }

    protected AbstractManager(Game game) {
        this(game.scene, game.gameRenderer);
        this.controller = game;
    }

    public void setConstraints(Vector2i count, Vector2f time) {
        this.setConstraints(count.x, count.y, time.x, time.y);
    }

    public void setConstraints(int minCount, int maxCount, float minTime, float maxTime) {
        this.authorize(true);
        this.setSpawnCountLimit(minCount, maxCount);
        this.setTimeBetweenSpawnsLimits(minTime, maxTime);
    }

    public void authorize(boolean canSpawn) {
        this.active = canSpawn;
        if (active) System.out.println(this + " is activated.");
    }

    public boolean isActive(){
        return active;
    }

    public void reset() {
        this.lastTimeSpawned = 0;
        this.authorize(false);
    }

    @Override
    public void release(T element) {
        this.poolElements.add(element);
    }

    @Override
    public T acquire() {
        T thing;
        if (this.poolElements.isEmpty()) {
            thing = this.create();
            thing.addObserver(this.itemObserver);
        } else {
            thing = this.poolElements.get(0);
            this.poolElements.remove(0);
        }
        this.parametrizeElement(thing);
        
        return thing;
    }


    /**
     * Obtains a specific instance of the object T.
     * It is the developper's choice whether they simply instantiate the acquire element,
     * or if they also set specific aspects of it in this method.
     * <p> For example, for the tile manager, the relevant constructor may be used
     * <pre>
     * public Tile create() {
     *  return new Tile(this.controller);
     * } </pre>
     * 
     * @return the newly created instance of a critter.
     */
    protected abstract T create() ;

    protected void parametrizeElement(T element) {
        element.setPlatformLevel(random.nextInt(this.platformer.getNumberFloors()));
    }

    @Override
    public List<T> spawn(float time) {
        // Ddecide if we spawn anything
        boolean decision = this.decideToSpawn(time);

        // Decide on the number of tiles to spawn
        int nb = decision ? random.nextInt(maxSpawnCount-minSpawnCount)+minSpawnCount : 0;
        List<T> elements = new ArrayList<>(nb);
        for (int i = 0; i < nb; i++) elements.add(this.acquire());
        // IntStream.of(nb).forEach(i -> elements.add(this.acquire()));

        return elements;
    }

    /**
     * 
     */
    protected boolean decideToSpawn(float time) {
        if (!active) return false;
        // Use the time elapsed since the last time (time-lastPawnedTime)
        // and if it's less than deltaTimeMin, don't spawn (i.e., return false)
        // if it;s more than deltaTimeMax, spawn
        // otherwise, your choice
        float timeElapsed = time - this.lastTimeSpawned;
        if (timeElapsed <= minDeltaTime) {
            return false;
        }
        if (timeElapsed >= maxDeltaTime) {
            this.lastTimeSpawned = time;        // this is now the new time
            return true;
        }
        // 50-50 chance
        boolean spawn = random.nextInt()%2 == 0;
        this.lastTimeSpawned = spawn ? time : lastTimeSpawned;
        return spawn;
    }

    @Override
    public float getMaxTimeBetweenSpawns() {
        return this.maxDeltaTime;
    }
    @Override
    public float getMinTimeBetweenSpawns() {
        return this.minDeltaTime;
    }
    @Override
    public void setTimeBetweenSpawnsLimits(float min, float max) {
        this.maxDeltaTime = max;
        this.minDeltaTime = min;
    }
    @Override
    public int getMinSpawnCount() {
        return this.minSpawnCount;
    }
    @Override
    public int getMaxSpawnCount() {
        return this.maxSpawnCount;
    }
    @Override
    public void setSpawnCountLimit(int min, int max) {
        this.maxSpawnCount = max;
        this.minSpawnCount = min;
    }


}
