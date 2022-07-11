package hero;

import org.jsfml.graphics.IntRect;
import org.jsfml.system.Vector2f;

/**
 * An interface for objects that implement and obey Newton's second law of motion.
 * A mobile object is therefore able to bounce and reflect, decelerate and accelerate according
 * to some implementation of the law of force and acceleratio
 */
public interface Mobile
{
    public Vector2f getAcceleration();
    public Vector2f getVelocity();
    public float getSpeed();

    public IntRect getAreaBounds();

    public void obeyMotionLaws();
    public void moveWithin(Vector2f displacement, IntRect area);
}