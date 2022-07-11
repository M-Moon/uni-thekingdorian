package platformer;

import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;


import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.IntRect;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.graphics.Sprite;
import org.jsfml.graphics.Texture;
import org.jsfml.graphics.Transform;
import org.jsfml.graphics.Transformable;

/**
 * A class which creates a scrolling background
 * 
 * @author Lydie Toure
 */
public class Scroller implements Drawable, Scrollable, Transformable
{
    private final Sprite background;
    private Texture backgroundTexture;

    private Vector2i size; // area to be scrolled in

    public Scroller()
    {
        this.background = new Sprite();
    }

    public Scroller(Texture texture) {
        this();
        this.setTexture(texture);
    }

    /**
     * Creates a Scroller with the given parameters
     * 
     * @param image the texture of the Sprite
     * @param size the size of the Scroller, if different from that of the image
     */
    public Scroller(Texture texture, Vector2i size) {
        this(texture);
        this.setSize(size);
    }



    @Override
    public void scroll(float dx, float dy) {
        // New portion of texture to draw
        IntRect portionToDraw = new IntRect(background.getTextureRect().left + (int) dx,
                background.getTextureRect().top + (int) dy, background.getTextureRect().width,
                background.getTextureRect().height);
        background.setTextureRect(portionToDraw);
    }

    @Override
    public void scroll(Vector2f displacement) {
        this.scroll(displacement.x, displacement.y);
    }

    @Override
    public void draw(RenderTarget target, RenderStates states) {
        background.draw(target, states);
    }

    public void setTexture(Texture t) {
        this.backgroundTexture = t;
        this.backgroundTexture.setRepeated(true);
        this.background.setTexture(t);

        // Resizes the sprite
        if (this.size != null) {
            this.setSize(this.size);
        }
    }

    /**
     * Sets a new size for this Scroller by rescaling it.
     * 
     * @param size
     */
    public void setSize(Vector2i size) {
        this.size = size;
        if (this.backgroundTexture == null) {
            return;
        }
        this.background.setTextureRect(new IntRect(Vector2i.ZERO,this.background.getTexture().getSize()));
        float oldWidth = this.background.getGlobalBounds().width;
        float oldHeight = this.background.getGlobalBounds().height;
        this.scale(size.x/oldWidth, size.y/oldHeight);
        // System.out.println("Scroller size = "+ background.getGlobalBounds());
        // System.out.println("Texture Rect "+ this.background.getTextureRect());
    }

    @Override
    public Transform getInverseTransform() {
        return this.background.getInverseTransform();
    }

    @Override
    public Vector2f getOrigin() {
        return this.background.getOrigin();
    }

    @Override
    public Vector2f getPosition() {
        return this.background.getOrigin();
    }

    @Override
    public float getRotation() {
        return this.background.getRotation();
    }

    @Override
    public Vector2f getScale() {
        return this.background.getScale();
    }

    @Override
    public Transform getTransform() {
        return this.background.getTransform();
    }

    @Override
    public void move(Vector2f arg0) {
        this.background.move(arg0);
    }

    @Override
    public void move(float arg0, float arg1) {
        this.background.move(arg0, arg1);
    }

    @Override
    public void rotate(float arg0) {
        this.background.rotate(arg0);
    }

    @Override
    public void scale(Vector2f arg0) {
        this.background.scale(arg0);
    }

    @Override
    public void scale(float arg0, float arg1) {
        this.background.scale(arg0, arg1);
    }

    @Override
    public void setOrigin(Vector2f arg0) {
        this.background.setOrigin(arg0);
    }

    @Override
    public void setOrigin(float arg0, float arg1) {
        this.background.setOrigin(arg0, arg1);
    }

    @Override
    public void setPosition(Vector2f arg0) {
        this.background.setPosition(arg0);
    }

    @Override
    public void setPosition(float arg0, float arg1) {
        this.background.setPosition(arg0, arg1);

    }

    @Override
    public void setRotation(float arg0) {
        this.background.setRotation(arg0);
    }

    @Override
    public void setScale(Vector2f arg0) {
        this.background.setScale(arg0);
    }

    @Override
    public void setScale(float arg0, float arg1) {
        this.background.setScale(arg0, arg1);
    }

}
