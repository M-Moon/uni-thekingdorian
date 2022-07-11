package ui;

import org.jsfml.graphics.Drawable;
import org.jsfml.window.event.Event;

/**
 * Interface to indicate than an element is an UI Overlay
 */
public interface Overlay extends Drawable {

    public void handleEvent(Event event);
    
}
