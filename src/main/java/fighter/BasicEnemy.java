package fighter;
import org.jsfml.graphics.RectangleShape;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.system.Vector2f;
import ManageAnimation.Animation;
import assets.TextureCatalogue;
import controllers.Game;
import hero.Hero;
import models.Critter;
import platformer.Platformer;
/***
 * A class for a basic enemy with a vision 
 */
public  abstract class BasicEnemy extends Enemy  {
   
    private final Vision vision;
    private final int Mwidth;
    protected boolean switchA=true;
    private Vector2f velocity=Vector2f.ZERO;
    protected Animation EnemyA;
    protected boolean flip;
    private boolean testhit = false;
    protected float animationspeed;
    protected static final TextureCatalogue GRAPHICS = TextureCatalogue.get();

  /***
   * @param forward move the vision froward 
   * @param Mwidth width of the vision 
   * @param animationspeed speed of the animation 
   */
    protected BasicEnemy(boolean forward,int Mwidth,float animationspeed){
        vision=new Vision(forward);
        this.Mwidth=Mwidth;
        this.animationspeed=animationspeed;
    }
  /**
   * It Start When the player enters the vision of an Enemy
   */
    protected abstract void oNTrigger(Hero player);
  /**
   * It update everything about the enemy 
   */
    protected abstract void updateEnemy(Hero playe );
  /**
   * It update the animation 
   */
    protected abstract void updateAnimation();
  
    /** 
     * it flip enemy to the left 
     * @param sizex width of the image 
     * @param sizey height of the image 
     */
    protected void flipLeftEnemy(int sizex,int sizey ) {
        sprite.setOrigin(0,0);
        this.sprite.scale(-sizex / this.getBounds().width, sizey / this.getBounds().height);
    }
    
      /** 
     * it flip enemy to the right
     * @param sizex width of the image 
     * @param sizey height of the image 
     */
    protected void flipRightEnemy(int sizex,int sizey ) {
        sprite.setOrigin(sprite.getLocalBounds().width,0);
        this.sprite.scale(-sizex / this.getBounds().width, sizey / this.getBounds().height);
    }
  
  /**
   * Movement of a BasicEnemy
   * @param player take in a hero to detect movement of the enenmy 
   */
    protected void basicMovement(Hero player){
        move();
        vision.UpdateVision(this, Mwidth);
        if(vision.EnteredVision(player)){
            oNTrigger(player);
        }
    }

  /**
   * Draw the vision of the enemy 
   */
    protected RectangleShape drawVision(){
        return vision.DrawVision();
    }
    /** 
     * set veloctiy of the enemy
     * @param velocity velocity of the enemy   
     */
    private void setVelocity(Vector2f velocity) {
        this.velocity = velocity;
    }

    /** 
     * set veloctiy of the enemy
     * @param x velx of the enemy  
     * @param y vely of the enemy 
     */
    protected void setVelocity(float x, float y) {
        this.setVelocity(new Vector2f(x, y));
    }
    
    /** 
     * it return the velocty of the enemy 
     * @return Vector2f velocty of the enemy 
     */
    protected Vector2f returnVelocity(){
        return velocity;
    }

    /**
     * it move the enemy according to the velocity 
     */
    protected void move() {
        sprite.move(velocity);
    }
    
  /**
   * place enemy on top of the platform 
   */
    @Override
    public void place(Platformer scene, Vector2f availablePosition) {
        float x = availablePosition.x + (scene.getPlatformTileSize().x - this.getBounds().width)/2;
        float y = availablePosition.y - this.getSize().y;
        setPosition(x, y);
    }
   

    
    /**
     * it update the enenmy frame by frame 
     * @param controller need it to access the hero 
     */
    @Override
    public void tick(float time, Game controller) {
        Hero hero = controller.hero;
        this.updateEnemy(hero);
        damagePlayer(controller,this);
    }

    
    /** 
     * it damage the player when enemy collide with the player 
     * @param controller it allows us to acces the player 
     * @param critter it find the collision box fot the player
     */
    protected void damagePlayer(Game controller,Critter critter){
        boolean collides = critter.getPixelBounds(controller).intersection(controller.hero.getPixelBounds(controller))!=null;
        if (collides && !testhit) {
            testhit = true;
            controller.hero.takeDamage();
        }
        if(!collides && testhit){
            testhit = false;
        }
        for(Projectile bullet : controller.hero.getBullets()){
            if(critter.getBounds().intersection(bullet.getBounds()) != null){
                bullet.removeFromRenderer(controller.gameRenderer);
                this.removeFromRenderer(controller.gameRenderer);
            }
        }

    }
    /**
     * To switch to other animation  
     * @param anim the animaton that you want switch to 
     */
    protected void switchAnimation(Animation anim){
        if(switchA){
            EnemyA=anim;
            switchA=false;
        }
    }

}


