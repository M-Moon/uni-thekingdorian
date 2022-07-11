package fighter;

import java.util.LinkedList;
import org.jsfml.system.Vector2f;
import Collectibles.weaponCollectible;
import ManageAnimation.Animation;
import controllers.Game;
import hero.Hero;


public class ShootEnemy extends BasicEnemy {
  LinkedList<Projectile> Bullets=new LinkedList<>();
  private long last=0;
  private final float Firerate;
  private boolean start=true;
  private boolean Addbullet;
  private final float bulletspeed;
  /***
   * 
   * @param forward move the vision to the left of the enemy 
   * @param Mwidth width of the vision 
   * @param Firerate for example 4 -- enemy shoot every 4 second 
   * @param bulletspeed it define the speed of the bullet 
   */
  public ShootEnemy(boolean forward,int Mwidth,float Firerate,float bulletspeed,float animationspeed) {
    super(forward,Mwidth,animationspeed);
    this.setTexture(GRAPHICS.TEMP_BG_02);
    this.Firerate=Firerate*1000;
    this.bulletspeed=bulletspeed;
    EnemyA=new Animation(GRAPHICS.ShootingEnemyIdleTexture,this,1,4);
    sprite.setOrigin(sprite.getLocalBounds().width,0);
    this.sprite.scale(-40 / this.getBounds().width, 70 / this.getBounds().height);
    this.healthPoints = weaponCollectible.getWeaponStrength(weaponCollectible.WEAPON_TYPE_SWORD);
  }

  @Override
  public void oNTrigger(Hero player) {
  Addbullet=true;
   if(start){
    last=System.currentTimeMillis();
    start=false;
   }
   
}
 
  @Override
  public void updateEnemy(Hero player ) {
        Addbullet=false;
        basicMovement(player);
        updateAnimation();     
  }
  
  
  /** 
   * it add the bullet to the screen according to the firerate
   * @param direction direction of the bullet 
   */
  private void allowedFirerate(int direction,Game game){
    if(System.currentTimeMillis() - last >= Firerate){
        last=System.currentTimeMillis();
        addBullet(direction,game);
    }
}

 
 /** 
  * it add the bullet to the screen 
  * @param direction direction of the bullet 
  * @param game to use this to add to the game  
  */
  private void addBullet(int direction,Game game) {
    float sizex=getSize().x;
    float x;
    Vector2f p=getPosition();
    if(direction==0)
        x=p.x-sizex;
      else
        x=p.x+sizex; 
    Projectile bullet=new Projectile(direction,new Vector2f(x,p.y+25));
    game.scene.addToPlatformer(bullet);
    bullet.addToRenderer(game.gameRenderer);
    Bullets.add(bullet);
    }


  /** 
   *  it moves the bullet frame by frame 
   */
  private void bulletsMovement(Game game){
    for(Projectile bullet:Bullets){
          bullet.move(bulletspeed);
          this.damagePlayer(game, bullet);
    }
  }

  @Override
  protected void updateAnimation() {
    EnemyA.update(this,this.animationspeed,1);
  }
  @Override
  public void tick(float time, Game controller) {
    super.tick(time, controller);
    bulletsMovement(controller);
    Hero hero = controller.hero;
    this.updateEnemy(hero);  
    if(Addbullet)
      allowedFirerate(0,controller);
  }
  }