package platformer;

import models.Critter;

/**
 * A Scene element on which other scene elements may step.
 * 
 * 
 */
public interface PlatformChunk extends SceneElement
{
    /**
     * Checks whether the critter is on top of this Critter
     * 
     * @param critter
     * @return
     */
    public boolean checkCritter(Critter critter);
    
    public void setSizeFrom(Platformer platformer);
}
