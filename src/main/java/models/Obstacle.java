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
 * An Obstacle represents a static Critter, that can be interacted
 * within the game by another Critter. The interactions can vary 
 * but are activated by coming into the hitbox of the Obstacle.
 */
public class Obstacle extends Critter {

    // Temporary graphics constant
    static final TextureCatalogue GRAPHICS = TextureCatalogue.get();

    private boolean testhit = false;


    /**
     * A constructor that determines the texture of obstacle called and 
     * sets it to a predetermined size depending on the type is specified.
     * @param type The integer will determine what obstacle is called.
     */
    public Obstacle(int type) {


        if(type == 0) {
            this.setTexture(GRAPHICS.TEMP_OBST_SPIKE);
            FloatRect spikeBounds = this.sprite.getLocalBounds();
            this.sprite.setScale(75f/spikeBounds.width,75f/spikeBounds.height);
        }
        if(type == 1) {
            this.setTexture(GRAPHICS.TEMP_OBST_LAVA);
            FloatRect lavaBounds = this.sprite.getLocalBounds();
            this.sprite.setScale(70f/lavaBounds.width,70f/lavaBounds.height);
        }
        if(type == 2) {
            this.setTexture(GRAPHICS.TEMP_OBST_FIRE);
            FloatRect fireBounds = this.sprite.getLocalBounds();
            this.sprite.setScale(100f/fireBounds.width,80f/fireBounds.height);
        }
        if(type == 3) {
            this.setTexture(GRAPHICS.TEMP_OBST_POISON);
            FloatRect poisonBounds = this.sprite.getLocalBounds();
            this.sprite.setScale(100f/poisonBounds.width,80f/poisonBounds.height);
        }
        
    }

    /**
     * Sets the obstacle to a certain tile so it is placed on top of it.
     * @param obst - Obstacle such as a spike, lava, gas cloud etc.
     * @param t - Tile that hero can walk on or obstacle can be placed on.
     */
    public void setObstacleTile(Tile t) {
        this.setPosition(t.getSprite().getGlobalBounds().left + t.getSprite().getGlobalBounds().width/2 - this.getSprite().getGlobalBounds().width/2 
            ,t.getSprite().getGlobalBounds().top - t.getSprite().getGlobalBounds().height - this.getSprite().getGlobalBounds().height/2);
    }

    /**
     * A method that returns true if the Critter h is within the 
     * hitbox (bounds) of the obstacle, and false if not.
     * @param h - A Critter that can interact with the obstacle, which is usually the Hero.
     * @return - Returns a boolean (true or false).
     * @deprecated - uses a deprecated method - To test collision with the hero, call {@link Hero#collidesWith(Critter, Game)} in the tick method
     */
    @Deprecated
    public boolean isObstacleCollided(Critter h) {
        if(this.overlapsWith(h) == true) {
            return true;
        }
        else{
            return false;
        }
    }

    /**
     * Sets the obtstacle to a specified type.
     */
    public void setType(int type) {
        if(type == 0) {
            this.setTexture(GRAPHICS.TEMP_OBST_SPIKE);
            FloatRect spikeBounds = this.sprite.getLocalBounds();
            this.sprite.setScale(75f/spikeBounds.width,75f/spikeBounds.height);
        }
        if(type == 1) {
            this.setTexture(GRAPHICS.TEMP_OBST_LAVA);
            FloatRect lavaBounds = this.sprite.getLocalBounds();
            this.sprite.setScale(70f/lavaBounds.width,70f/lavaBounds.height);
        }
        if(type == 2) {
            this.setTexture(GRAPHICS.TEMP_OBST_FIRE);
            FloatRect fireBounds = this.sprite.getLocalBounds();
            this.sprite.setScale(100f/fireBounds.width,80f/fireBounds.height);
        }
        if(type == 3) {
            this.setTexture(GRAPHICS.TEMP_OBST_POISON);
            FloatRect poisonBounds = this.sprite.getLocalBounds();
            this.sprite.setScale(100f/poisonBounds.width,80f/poisonBounds.height);
        }
    }

    @Override
    public void tick(float time, Game controller) {
        super.tick(time, controller);
        // final Hero hero = controller.hero;
        // boolean collides = this.getPixelBounds(controller).intersection(controller.hero.getPixelBounds(controller))!=null;
        // boolean collides = this.collides(controller.hero, controller);
        // // boolean hit = controller.getHit();
        // if (collides && !testhit) {
        //     // controller.setHit(true);
        //     testhit = true;
        //     controller.hero.takeDamage();
        // }
        // if(!collides && testhit){
        //     testhit = false;
        // }
        // this.handleCollision(controller,()->{
        //     controller.hero.takeDamage();
        // });
        handleCollision(controller, controller.hero::takeDamage);
    }


    @Override
    public CritterType getCritterType() {
        return CritterType.OBSTACLE;
    }

    /**
     * Returns whether the obstacle is overlapping the specified Critter's bounds.
     */
    public boolean overlapsWith(Critter c) {
        return this.sprite.getGlobalBounds().intersection(c.getSprite().getGlobalBounds()) != null;
    }


    @Override
    public void placeInScene(AbstractScene scene, Vector2f platformPosition) {
        // Not used
    }

    @Override
    public void place(Platformer scene, Vector2f availablePosition) {
        float x = availablePosition.x + (scene.getPlatformTileSize().x - this.getBounds().width)/2;
        float y = availablePosition.y - this.getSize().y;
        this.setPosition(x, y);
    }


    
}

