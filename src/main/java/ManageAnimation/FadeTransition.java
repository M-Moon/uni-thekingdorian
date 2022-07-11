package ManageAnimation;


import org.jsfml.graphics.*;
import org.jsfml.system.*;
import devtools.*;

public abstract class FadeTransition extends Transition {

    private int alpha = 0;
    private int speed;
    protected RectangleShape rectangle;
    protected boolean fade = true;

    /**
     * 
     * @param rect  the area in which the fade must apply
     * @param speed the speed at which the fade must apply
     */
    protected FadeTransition(FloatRect rect, int speed) {
        this.rectangle = new RectangleShape(new Vector2f(rect.width, rect.height));
        this.rectangle.setPosition(rect.left, rect.top);
        this.rectangle.setFillColor(Color.TRANSPARENT);
        this.speed = speed;
        // this.effect = effect;
    }

    public void apply() {
        if (!this.active)
            return;
        rectangle.setFillColor(new Color(0, 0, 0, alpha));
        // transitionCircle.setFillColor(new Color(0,0,0, transitionAlpha));

        if (clock.getElapsedTime().asSeconds() > 0.1f && alpha < 255f && fade) {
            alpha += speed;
            clock.restart();

            if (alpha >= 255) {
                alpha = 255;
                fade = false;
                // System.out.println("Faded out " + alpha);
                this.applyEffect();
            }
        }

        else if (clock.getElapsedTime().asSeconds() > 0.1f && alpha > 0f && !fade) {
            alpha -= 5;
            clock.restart();

            if (alpha <= 0) {
                alpha = 0;
                this.active = false;
                this.notifyObservers(new ObservableEvent(this, EVENT_TRANSITION_COMPLETED));
            }
        }
        rectangle.setFillColor(new Color(0, 0, 0, alpha));
    }

    @Override
    public void draw(RenderTarget arg0, RenderStates arg1) {
        this.rectangle.draw(arg0, arg1);

    }

}
