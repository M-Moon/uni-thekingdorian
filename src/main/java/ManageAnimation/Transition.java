package ManageAnimation;

import java.util.ArrayList;
import java.util.List;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.FloatRect;
import org.jsfml.graphics.RectangleShape;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.graphics.Sprite;
import org.jsfml.graphics.Texture;
import org.jsfml.system.Clock;
import org.jsfml.system.Vector2f;

import devtools.*;
import sys.Renderable;
import sys.Renderer;

public abstract class Transition implements Drawable, Observable 
{
    public static final int EVENT_TRANSITION_COMPLETED = 301;

    protected Clock clock = new Clock();

    // private int alpha = 0;
    // private int speed;
    // protected RectangleShape rectangle;
    // private TransitionEffect effect;
    protected boolean active = false;
    protected boolean fadingOut = true;

    protected List<Observer> observers = new ArrayList<>();

    // /**
    //  * 
    //  * @param rect the area in which the fade must apply
    //  * @param speed the speed at which the fade must apply
    //  */
    // protected Transition(FloatRect rect, int speed) {
    //     this.rectangle = new RectangleShape(new Vector2f(rect.width, rect.height));
    //     this.rectangle.setPosition(rect.left, rect.top);
    //     this.rectangle.setFillColor(Color.TRANSPARENT);
    //     this.speed = speed;
    //     // this.effect = effect;
    // }

    public void start() {
        this.active = true;
        this.clock.restart();
    }

    public abstract void apply();

    // /**
    //  * @param out whether the screen must disappear first
    //  */
    // public void apply(boolean out) {
    //     if (out) {
    //         fadeOutFirst();
    //     } else {
    //         fadeInFirst();
    //     }
    // }

    // private void fadeOutFirst() {
    //     if (!this.active)
    //         return;
    //     rectangle.setFillColor(new Color(0, 0, 0, alpha));
    //     // transitionCircle.setFillColor(new Color(0,0,0, transitionAlpha));

    //     if (clock.getElapsedTime().asSeconds() > 0.1f && alpha < 255f && fadingOut) {
    //         alpha += speed;
    //         clock.restart();

    //         if (alpha >= 255) {
    //             alpha = 255;
    //             fadingOut = false;
    //             // System.out.println("Faded out " + alpha);
    //             this.applyEffect();
    //         }
    //     }

    //     else if (clock.getElapsedTime().asSeconds() > 0.1f && alpha > 0f && !fadingOut) {
    //         alpha -= 5;
    //         clock.restart();

    //         if (alpha <= 0) {
    //             alpha = 0;
    //             this.active = false;
    //             this.notifyObservers(new ObservableEvent(this, EVENT_TRANSITION_COMPLETED));
    //         }
    //     }
    //     rectangle.setFillColor(new Color(0, 0, 0, alpha));
    // }

    // private void fadeInFirst() {
    //     if (!this.active)
    //         return;
    //     rectangle.setFillColor(new Color(0, 0, 0, alpha));
    //     // transitionCircle.setFillColor(new Color(0,0,0, transitionAlpha));

        
    //     if (clock.getElapsedTime().asSeconds() > 0.1f && alpha > 0f && fadingOut) {
    //         alpha -= 5;
    //         clock.restart();

    //         if (alpha <= 0) {
    //             fadingOut = false;
    //             System.out.println("Faded out " + alpha);
    //             this.applyEffect();
    //         }
    //     }

    //     else if (clock.getElapsedTime().asSeconds() > 0.1f && alpha < 255f && !fadingOut) {
    //         alpha += speed;
    //         clock.restart();

    //         if (alpha >= 255) {
    //             alpha = 0;
    //             this.active = false;
    //             this.notifyObservers(new ObservableEvent(this, EVENT_TRANSITION_COMPLETED));
    //         }
    //     }
    //     rectangle.setFillColor(new Color(0, 0, 0, alpha));
    // }

    // @Override
    // public void draw(RenderTarget arg0, RenderStates arg1) {
    //     rectangle.draw(arg0, arg1);
    // }

    public abstract void applyEffect();

    @Override
    public void addObserver(Observer observer) {
        this.observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        this.observers.remove(observer);
    }

    @Override
    public void notifyObservers(ObservableEvent event) {
        this.observers.forEach(o -> o.receiveUpdate(event));
    }
}

