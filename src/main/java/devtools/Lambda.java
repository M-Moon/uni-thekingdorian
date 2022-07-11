package devtools;

/**
 * An interface for a lambda function which takes to argument and does not return anything
 * 
 * 
 */
@FunctionalInterface
public interface Lambda {
    
    public void run();

    public default Lambda andThen(Lambda after) {
        return () -> {
            this.run();
            after.run();
        };
    }

    public default Lambda compose(Lambda before) {
        return () -> {
            before.run();
            this.run();
        };
    }
}
