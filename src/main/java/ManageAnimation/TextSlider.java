package ManageAnimation;

import org.jsfml.graphics.*;
import org.jsfml.system.Vector2f;

import ui.TextBox;

public class TextSlider extends Transition 
{
    protected TextBox textBox;
    protected boolean vertical;

    protected Vector2f destination;
    protected float speed;

    protected float waitTime = -1; 
    protected float time = 0;
    protected Status status = Status.SLIDE_IN;

    protected View view;

    private enum Status {SLIDE_IN, IDLE, SLIDE_OFF}

    public TextSlider(TextBox text, boolean verticalSlide)
    {
        this.textBox = text;
        this.vertical = verticalSlide;
    }
    
    

    public void createView(RenderWindow window) {
        if (destination==null || textBox==null) return;
        view.reset(new FloatRect(this.destination, this.textBox.getSize()));
        view.setViewport(new FloatRect( Vector2f.componentwiseDiv(this.destination, new Vector2f(window.getSize())),
            Vector2f.componentwiseDiv(textBox.getSize(), new Vector2f(window.getSize()))));
    }

    public void setWaitTime(float waitTime) {
        this.waitTime = waitTime;
    }

    public void setDestination(Vector2f endPoint) {
        this.destination = endPoint;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    protected boolean hasReached() {
        if (vertical) {
            if (this.textBox.getPosition().y + this.textBox.getSize().y >= destination.y && this.speed > 0 ) {
                // Going down and over
                return true;
            }
            if (this.textBox.getPosition().y <= destination.y && this.speed < 0) return true;
        } else {
            if (this.textBox.getPosition().x + this.textBox.getSize().x >= destination.x && this.speed>0) 
                return true;
            if (this.textBox.getPosition().x <= destination.x && this.speed < 0) return true;
        }
        return false;
    }

    protected boolean hasEntered()
    {
        return true;
    }

    

    @Override
    public void draw(RenderTarget arg0, RenderStates arg1) {
        textBox.draw(arg0, arg1);
        
    }

    @Override
    public void applyEffect() {
        // TODO Auto-generated method stub
        

    }

    @Override
    public void apply() {
        if (!active) return;
        if (status==Status.SLIDE_IN) {
            // move the box
            // if (vertical) this.textBox.move(0,speed);
            // else this.textBox.move(speed,0);
            // // check if arrived
            // if (this.hasReached()) status = Status.IDLE;
        }

        if (status == Status.IDLE) {
            if (this.waitTime < 0) status = Status.SLIDE_OFF;
            else {
                time += clock.restart().asSeconds();
                if (time >= waitTime) status = Status.SLIDE_OFF;
            }
        }

        if (status == Status.SLIDE_OFF) {
            
        }

    }

}
