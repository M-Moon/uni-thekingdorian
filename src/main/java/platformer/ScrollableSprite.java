package platformer;

import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;

import org.jsfml.graphics.ConstTexture;
import org.jsfml.graphics.IntRect;
import org.jsfml.graphics.Sprite;
import org.jsfml.graphics.Texture;
 
/**
 * A class creates a scrolling background
 * 
 * 
 * @author Lydie Toure
 */
public class ScrollableSprite extends Sprite implements Scrollable
{
    /**
     * The dimensions of the frame
     */
    private Vector2i size;

    public ScrollableSprite() {
        super();
    }

    public ScrollableSprite(Texture texture) {
        this();
        this.setTexture(texture);
    }


    public ScrollableSprite(Texture image, Vector2i size)
    {
        this(image);
        this.setSize(size);
    }


    @Override
    public void scroll(float dx, float dy)
    {
        // get the new portion of texture to draw
        IntRect portionToDraw = new IntRect(this.getTextureRect().left + (int)dx, this.getTextureRect().top + (int)dy,
            this.getTextureRect().width, this.getTextureRect().height);
        this.setTextureRect(portionToDraw);
    }

    @Override
    public void scroll(Vector2f displacement)
    {
        this.scroll(displacement.x, displacement.y);
    }

    public void setSize(Vector2i size) {
        this.size = size;
        // System.out.println("[setSize|in] - SIZE="+ new Vector2f(this.getGlobalBounds().width,this.getGlobalBounds().height));
        if (this.getTexture() == null) return;
        
        this.setTextureRect(new IntRect(Vector2i.ZERO,this.getTexture().getSize()));
        float oldWidth = this.getGlobalBounds().width;
        float oldHeight = this.getGlobalBounds().height;
        this.scale(this.size.x/oldWidth, this.size.y/oldHeight);    
    }

    public final void setSize(int x, int y) {
        this.setSize(new Vector2i(x,y));
    }


    @Override
    public void setTexture(ConstTexture texture, boolean resetRect)
    {
        if (resetRect) {
            super.setTexture(texture, true);
            return;
        }

        Texture t = new Texture(texture);
        t.setRepeated(true);
        super.setTexture(t,false);

        // Resets the right size
        if (this.size != null) {
            this.setSize(this.size);
        }
    }

}
