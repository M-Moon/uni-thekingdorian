
package fighter;
import org.jsfml.system.Vector2f;
import assets.TextureCatalogue;
import controllers.Game;
import models.*;
import platformer.AbstractScene;
import platformer.Platformer;

public class Projectile extends Critter {

  
    private int direction;
    static final TextureCatalogue GRAPHICS = TextureCatalogue.get();
/***
   * @param direction set direction of the bullet 
   * @param position position of the bullet that you are going to plcae 
   */
    public Projectile(int direction, Vector2f position) {
        this.setTexture(GRAPHICS.ProjectileTexture);
        this.sprite.scale(25 / this.getBounds().width, 25 / this.getBounds().height);
        this.direction = direction;
        this.setPosition(position);
    }

    /**
     * it move the bullet to the right if the speed is 1
     * it move the bullet to the left if the speed is 0
     * @param speed the speed of the bullet
     */
    public void move(float speed) {
        switch (direction) {
            case 0:
                this.sprite.move(-speed, 0);
                // move the projectile n amount in said direction
                break;
            case 1:
                this.sprite.move(speed, 0);
                break;
            case 2:
                System.out.println("The projectile is moving down");
                break;
            case 3:
                System.out.println("The projectile is moving left");
                break;
            default:
                break;
        }
    }

    
   
    @Override
    public void placeInScene(AbstractScene scene, Vector2f platformPosition) {
        // TODO Implement his abstract method to place this token on the scen
    }

    
    
    @Override
    public void place(Platformer scene, Vector2f availablePosition) {
        //
    }

    

    public void tick(float time, Game controller) {
        super.tick(time, controller);
    }

    
    /** 
     * @return CritterType
     */
    @Override
    public CritterType getCritterType() {
        return CritterType.PROJECTILE;
    }




        
}
