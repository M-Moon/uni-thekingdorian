package managers;

/**
 * An object pool to reuse tokens that have left the screen
 */
public interface ReusablePool<T>
{
    /**
     * Replaces a Reusable object in the pool for further use
     * 
     * @param r the Reusable object that is no longer needed
     */
    public void release(T r);

    public T acquire();  // returns an object of this type
}