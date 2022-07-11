package models;

import java.util.Comparator;
import java.util.Random;
import java.util.function.Predicate;

import org.jsfml.graphics.*;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;
import org.jsfml.window.VideoMode;
import org.jsfml.window.event.Event;

import platformer.*;
import sys.Renderable;
import sys.Renderer;
import assets.TextureCatalogue;
import controllers.Game;
import hero.Hero;
import models.Critter;
import ui.UIOverlay;
import views.*;




/**
 * Wall Class that creates a spike wall at the left of the screen for the player to run from.
 */
public class Wall extends Sprite {

    // Temporary graphics constant
    static final TextureCatalogue GRAPHICS = TextureCatalogue.get();

    /**
     * A Constructor that creates the wall to be placed into the game scroller.
     */
    public Wall() {
        this.setTexture(GRAPHICS.SPIKE_WALL);
        FloatRect Bounds = this.getLocalBounds();
        this.setScale(10f/Bounds.width,900f/Bounds.height);  
        this.setPosition(0, 450);

    }


    
}

