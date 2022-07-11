package ManageAnimation;

import java.util.ArrayList;

import org.jsfml.graphics.IntRect;
import org.jsfml.graphics.Sprite;
import org.jsfml.graphics.Texture;
import org.jsfml.system.Vector2i;

import models.Critter;

public class Animation {
  //Store  textures of the animation 
  IntRect frames [][];
  private float Frame, speed;
  private int r;//to check if user change state of animation or not 
   /***
    * 
    * @param t the spreadsheet 
    * @param Element The SceneCritter that you wanna add animation to 
    * @param row number of the rows on the spreadsheet 
    * @param column number of the columns on the spreadsheet 
    */
  public Animation(Texture t,Critter Element,int row,int column){
  //  frames.add(new IntRect( i * (size.x/count),  size.y/9, size.x/count, size.y/9);
    frames=new IntRect[row][column];
    int y=0;
    this.speed=speed;
    Sprite sprite=Element.getSprite();
    t.setSmooth(true);
    sprite.setTexture(t);
    Vector2i size=t.getSize();
    for (int i = 0; i < row; i++) {
       for(int r=0;r<column;r++){
        frames[i][r]=new IntRect( r * (size.x/column),  y, size.x/column, size.y/row);
       }   
       y+=size.y/row;
    }
    sprite.setTextureRect(frames[0][0]);
  }

  /***
   * 
   * @param Element  the scenecritter 
   * @param speed speed of the animation 
   * @param row the row of the spread sheet that you want lopp through
   */
  public void update(Critter Element,float speed,int row) {
    row--;
    Sprite sprite=Element.getSprite();
    int n =frames[row].length;
    if(r!=row){
      Frame=0;
      r=row;
    }
    Frame += speed;
    if (Frame >= n) {
        Frame -= n;// restart at the first image
    }
    if (n > 0) {
        sprite.setTextureRect(frames[row][(int) (Frame)]);
    }
}

}

