package managers;

import java.util.List;

import models.Critter;

public interface Spawner<C> 
{
    /**
     * Obtains a given number of spawned critters, according to this Spawner's parameters.
     * 
     * The parameters are the time between spawns and the maximum number of items which can be spawned at once
     * @param time the time elapsed since the start of the game (in seconds)
     * @return a list of critters to spawn
     */
    public List<C> spawn(float time);

    public float getMaxTimeBetweenSpawns();
    public float getMinTimeBetweenSpawns();
    public void setTimeBetweenSpawnsLimits(float min, float max);

    /**
     * Obtains the minimum number of objects which can be spawned at once
     * 
     * @return
     */
    public int getMinSpawnCount();
    public int getMaxSpawnCount();
    public void setSpawnCountLimit(int min, int max);

}
