package fighter;
import org.jsfml.graphics.Texture;
import org.jsfml.system.Vector2f;
import Collectibles.weaponCollectible;
import ManageAnimation.Animation;
import hero.Hero;

public class MovingEnemy extends BasicEnemy {

  private float speed;
  private Texture walk;
   /***
   * 
   * @param forward move the vision froward 
   * @param Mwidth width of the vision 
   * @param speed speed of the enemy
   * @param Texture Texture for idle  for enemy
   * @param Texture Texture for walking for enemy
   */
  public MovingEnemy(boolean forward,int Mwidth,float speed,Texture Idle,Texture walk,float animationspeed) {
    super(forward,Mwidth,animationspeed);
    EnemyA =new Animation(Idle,this,1,4);
    sprite.setOrigin(sprite.getLocalBounds().width,0);
    this.sprite.scale(-40 / this.getBounds().width, 80 / this.getBounds().height);
    this.speed=speed;
    this.walk=walk;
    this.healthPoints = weaponCollectible.getWeaponStrength(weaponCollectible.WEAPON_TYPE_BOW);
  }

  @Override
  protected void oNTrigger(Hero player) {
    Vector2f E = getPosition();
    Vector2f P = player.getPosition();
     if (Math.abs(P.x)-Math.abs(E.x)>50){
        if(!flip){
          //right 
          flipLeftEnemy(40, 80);
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
        switchAnimation(new Animation(walk,this,1,6));
    }
    EnemyA.update(this,this.animationspeed,1);
  } 
}