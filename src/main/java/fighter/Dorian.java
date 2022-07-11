package fighter;
import org.jsfml.graphics.Texture;
import org.jsfml.system.Vector2f;
import Collectibles.weaponCollectible;
import ManageAnimation.Animation;
import hero.Hero;

 /***
   * A Class that creates the final boss enemy Dorian and attibutes can be specified at initialisation.
   * This Class extends a MovingEnemy as he has animations (both idle and running).
   * 
   * @param forward move the vision froward 
   * @param Mwidth width of the vision 
   * @param speed speed of the enemy
   * @param Texture Texture for idle  for enemy
   * @param Texture Texture for walking for enemy
   */
public class Dorian extends MovingEnemy {

    private float speed;
    private Texture walk;

    /**
     * A constructor which creates a Dorian enemy with the following parameters.
     * @param forward - a boolean whether te player just moves foward.
     * @param Mwidth - The width of the vision of the enemy.
     * @param speed - The speed at which it moves at.
     * @param Idle - The Idle texture when the enemy is not moving.
     * @param walk - The walking texture when the enemy is moving.
     * @param animationspeed - The speed at which the animation is played.
     */
    public Dorian(boolean forward, int Mwidth, float speed, Texture Idle, Texture walk, float animationspeed) {
        super(forward, Mwidth, speed, Idle, walk, animationspeed);
        EnemyA =new Animation(Idle,this,1,8);
        sprite.setOrigin(sprite.getLocalBounds().width,0);
        this.sprite.scale(200 / this.getBounds().width, 200 / this.getBounds().height);
        this.speed=speed;
        this.walk=walk;
    }

    // public boolean dorianDead(Dorian d) {

    //     if(!d.isBeingRendered() && bossLevelIsActive) {
    //         return true;
    //     }
    //     else {
    //         return false;
    //     }

    // }

    @Override
  protected void oNTrigger(Hero player) {
    Vector2f E = getPosition();
    Vector2f P = player.getPosition();
     if (Math.abs(P.x)-Math.abs(E.x)>50){
        if(!flip){
          //right 
          flipLeftEnemy(200, 200);
        }
        flip=true;
      setVelocity(speed, 0);
    }
    else if (Math.abs(E.x)-Math.abs(P.x)>50) {
      setVelocity(-speed, 0);
      if(flip){
        //left
        flipRightEnemy(40, 80);
      }
      flip=false;
    }
    
  }

  @Override
  protected void updateEnemy(Hero player) {
    basicMovement(player);
    updateAnimation();    
  }

  

  @Override
  protected void updateAnimation() {
    Vector2f v=returnVelocity();
    if(v.x!=0){
        switchAnimation(new Animation(GRAPHICS.DorianRunTexture,this,1,8));
    }
    EnemyA.update(this,this.animationspeed,1);
  } 
}

