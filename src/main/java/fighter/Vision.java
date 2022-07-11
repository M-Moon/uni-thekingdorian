package fighter;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.FloatRect;
import org.jsfml.graphics.RectangleShape;
import org.jsfml.system.Vector2f;
import hero.Hero;


public class Vision {

  private FloatRect EnemyVision; 
  private boolean forward;
  /**
   * Enemy have a vision for both sides if forward is false
   * Enemy have a vision for  one  the left side if forward is true
   * @param forward to indicate the side of the vision 
   */
  public Vision(boolean forward){
   this.forward=forward;
   EnemyVision=new FloatRect(new Vector2f(0,0),new Vector2f(0,0));
  }  
  /**
   * It display the vision of the Enemy
   */
  public RectangleShape DrawVision(){
    
    RectangleShape rect=new RectangleShape(new Vector2f(EnemyVision.width,EnemyVision.height));
    rect.setPosition(new Vector2f(EnemyVision.left,EnemyVision.top));
    rect.setFillColor(new Color(0, 0, 0, 0));
    rect.setOutlineColor(new Color(0, 255, 0));
    rect.setOutlineThickness(2);
    return rect;
  }
/**
 * It update the vision of the enemy 
 * @param E Take In the Enemy to update the view of the enemy 
 * @param width Width of the Vision Recommend between 1 and 10
 */
  public void UpdateVision(Enemy E,int width){
    int Mwidth=width*2+1;
    width=forward?Mwidth:width;
    float Px=E.getPosition().x-(E.getSize().x*(width));
    Vector2f Size=new Vector2f(Mwidth*E.getSize().x,E.getSize().y);
    Vector2f P=new Vector2f(Px,E.getPosition().y);
    EnemyVision=new FloatRect(P,Size);
  }
   /**
    * Check if Player Enter the Vision
    * @param h instance of a hero  
    * @return it return true if player enters the vision 
    */
  public boolean EnteredVision(Hero h){
    return EnemyVision.intersection(h.getBounds())!=null;
  }
}
