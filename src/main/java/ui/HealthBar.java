package ui;

import org.jsfml.graphics.Texture;
import org.jsfml.graphics.*;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;

import assets.TextureCatalogue;

import javax.swing.text.Position;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.*;
import java.util.concurrent.TimeUnit;

/**
 * Method that creates a healthbar of 3 hearts.
 * Has methods that lower the amount of hearts and increase.
 * These methods also update the sprite which displays half, full and empty hearts.
 *
 */
public class HealthBar implements Drawable{
    private static TextureCatalogue TEXTURES = TextureCatalogue.get();
    private Texture FullHeart = TEXTURES.HEART_FULL;
    private Texture HalfHeart = TEXTURES.HEART_HALF;
    private Texture EmptyHeart = TEXTURES.HEART_EMPTY;
    private int health = 6;
    private Sprite[] Hearts = new Sprite[3];

    /**
     * Constructor for healthbar. Takes a scale and location to draw it on.
     *
     *
     * @param Position Vector2f position of the top left corner of the left most heart
     * @param scale float scale that all the hearts will be scaled to
     */
    public HealthBar(Vector2f Position, float scale) {
        Hearts[0] = new Sprite(FullHeart, new IntRect(0, 0, 160, 160));
        Hearts[0].setScale(scale, scale);
        Hearts[0].setPosition(Position);
        float spacing = (Hearts[0].getGlobalBounds().width*1.16f);
        Hearts[1] = new Sprite(FullHeart, new IntRect(0, 0, 160, 160));
        Hearts[1].setScale(scale, scale);
        Hearts[1].setPosition(Position.x + spacing, Position.y);
        Hearts[2] = new Sprite(FullHeart, new IntRect(0, 0, 160, 160));
        Hearts[2].setScale(scale, scale);
        Hearts[2].setPosition(Position.x + spacing*2, Position.y);

    }

    /**
     * Lose health method.
     * 6 is max health.
     *
     * @param amount integer amount for hearts to lose 1 = half heart, 2 = full heart.
     */

    public void loseHealth(int amount){
        if(health>0){
            health -= amount;
        }
        if(health < 0){
            health = 0;
        }
        updateHealth();
    }

    /**
     * gain health method.
     * 6 is max health.
     *
     * @param amount integer amount for hearts to increase 1 = half heart, 2 = full heart.
     */
    public void gainHealth(int amount){

        if(health<6){
            health += amount;
            if(health>6){
                health = 6;
            }
        }

        updateHealth();
    }

    /**
     * method that resets health and heart sprites to full.
     *
     *
     */
    public void reset(){
        health = 6;
        updateHealth();
    }


    public float getHealth(){
        return health;
    }

    /*public void draw(RenderWindow window){
        for (Sprite Heart: Hearts) {
            window.draw(Heart);

        }
    }*/

    /**
     * Method that updates the health sprite
     * to match the health.
     *
     *
     */
    private void updateHealth(){
        int i = health;
        for (Sprite Heart: Hearts) {
            if(i >= 0){
                Heart.setTexture(EmptyHeart);
            }
            if(i >= 1){
                Heart.setTexture(HalfHeart);
            }
            if(i >= 2){
                Heart.setTexture(FullHeart);
            }
            i = i - 2;
        }
    }


    @Override
    public void draw(RenderTarget renderTarget, RenderStates renderStates) {
        for (Sprite Heart: Hearts) {
            Heart.draw(renderTarget, renderStates);
        }
    }
}
