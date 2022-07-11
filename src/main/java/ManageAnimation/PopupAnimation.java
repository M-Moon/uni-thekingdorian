package ManageAnimation;

import org.jsfml.graphics.*;
import org.jsfml.system.*;

import devtools.ObservableEvent;
import ui.PopupFrame;
import ui.TextBox;

/**
 * A simple transition whose effect is to wait a given amount of time
 * 
 * 
 */
public class PopupAnimation extends Transition {

    protected float waitLimit;
    protected float waitTime = 0f;

    protected TextBox box;
    protected PopupFrame frame;

    public PopupAnimation(PopupFrame frame, float time) {
        this.frame = frame;
        this.waitLimit = time;
    }

    @Override
    public void draw(RenderTarget arg0, RenderStates arg1) {
        // box.draw(arg0, arg1);
        this.frame.draw(arg0, arg1);
    }

    @Override
    public void apply() {
        if (!this.active) return;
        applyEffect();
    }

    public void setPosition(Vector2f pos) {
        this.box.setPosition(pos);
    }

    @Override
    public void applyEffect() {
        waitTime += clock.restart().asSeconds();
        if (this.waitTime >= this.waitLimit) {
            this.active = false;
            this.notifyObservers(new ObservableEvent(this, EVENT_TRANSITION_COMPLETED));
        }
    }
    
}
