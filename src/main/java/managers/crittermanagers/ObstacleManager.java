package managers.crittermanagers;

import java.util.Random;

import controllers.Game;
import devtools.*;
import managers.AbstractManager;
import models.Obstacle;
import platformer.Platformer;
import sys.Renderer;

public class ObstacleManager extends AbstractManager<Obstacle> {

    private static ObstacleManager INSTANCE = null; 

    // Random rando = new Random();
    // int randomObstacle = rando.nextInt(6);

    protected ObstacleManager(Platformer platformer, Renderer renderer) {
        super(platformer, renderer);
        // this.active = true;
    }

    protected ObstacleManager(Game game) {
        super(game);
        // this.active = true;
    }

    public static final ObstacleManager get(Game game) {
        if (INSTANCE == null) {
            INSTANCE = new ObstacleManager(game);
        }
        return INSTANCE;
    }


    @Override
    protected Obstacle create() {
        
        int[] types = {0,1,2,3};
        int type = types[random.nextInt(types.length)];
        Obstacle object = new Obstacle(type);
        return object;
    }

    // @Override



    // @Override
    // public void receiveUpdate(ObservableEvent event) {
    //     // TODO Auto-generated method stub

    // }


}
