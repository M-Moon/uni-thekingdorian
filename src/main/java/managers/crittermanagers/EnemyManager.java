package managers.crittermanagers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import assets.TextureCatalogue;
import controllers.Game;
import devtools.*;
import fighter.BasicEnemy;
import fighter.Dorian;
import fighter.Enemy;
import fighter.MovingEnemy;
import fighter.ShootEnemy;
import managers.AbstractManager;
import platformer.DefaultScene;
import platformer.GameScene;
import platformer.Platformer;
import platformer.Tile;
import sys.Renderer;

public class EnemyManager extends AbstractManager<Enemy> implements Observer {
  private static EnemyManager INSTANCE = null;
  private static final TextureCatalogue GRAPHICS = TextureCatalogue.get();
  protected EnemyManager(Game game) {
    super(game);
    // this.active=true;
  }


  protected EnemyManager(Platformer platformer, Renderer renderer) {
    super(platformer, renderer);
    // this.active = true;
}

  @Override
  public void receiveUpdate(ObservableEvent event) {
    // TODO Auto-generated method stu

  }

  @Override
  protected Enemy create() {

    ArrayList<Enemy>  Enemy = new ArrayList<>(); 
    Enemy.add(new MovingEnemy(false,7,3,GRAPHICS.MovingEnemyIdleTexture,GRAPHICS.MovingEnemyWalkTexture,0.12f));
    Enemy.add(new MovingEnemy(true,6,5,GRAPHICS.RuningEnemyIdleTexture,GRAPHICS.RuningEnemyWalkTexture,0.4f));
    Enemy.add( new ShootEnemy(true ,6,1.5f,2.2f,0.06f));
    Collections.shuffle(Enemy);
    int randomIndex = new Random().nextInt(3); 
   return Enemy.get(randomIndex);

  } 

  public static final EnemyManager get(Game game) {
    if (INSTANCE == null) {
        INSTANCE = new EnemyManager(game);
    }
    return INSTANCE;
  }



  
}
