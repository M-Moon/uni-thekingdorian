package npc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jsfml.graphics.*;
import org.jsfml.system.*;
import org.jsfml.window.Keyboard;
import org.jsfml.window.event.Event;

import controllers.Game;
import devtools.Lambda;
import devtools.ObservableEvent;
import managers.crittermanagers.GuideManager;
import models.Critter;
//import mytests.aftertests.platformer.SceneTrials;
import platformer.GameScene;
import platformer.GameScene.SceneType;
import sys.Renderer;
import ui.Button;
import ui.PanelOverlay; 

/**
 * The crossroads guide is an NPC which prompts a scene change.
 * The player is meant to interact with them to travel to another scene ({@link GameScene#getSceneType()})
 * 
 * @author Lydie Toure
 * @see GameScene
 */
public class Guide extends NPC
{
    private static Guide SINGLETON = null;
    private enum Status {IDLE, ENTERED}

    private Zone[] crossroads = new Zone[2]; // ground / not grounds
    
    /**
     * Event ID signaling that the guide is on screen
     */
    public static final int EVENT_GUIDE_ON = 702;
    public static final int EVENT_GUIDE_OFF = -702;

    public final Game controller;

    // protected

    protected Guide(Game game)
    {
        this.controller = game;
        this.addObserver(GuideManager.get(controller));
        this.setTexture(controller.GRAPHICS.NPC_GUIDE_ZONE);
        // System.out.println("Zone size: " + this.getBounds());
        // this.scale(0.5f,0.5f);
        this.setPlatformLevel(0);
        // this.zone = new Crossroads();
        
        // Build the area

        // Create the crossroads
        this.createCrossroads();
        this.zone = crossroads[0];
    }
    /**
     * Obtain the unique guide available
     * @param game
     * @return
     */
    public static Guide get(Game game) {
        if (SINGLETON == null) {
            SINGLETON = new Guide(game);
        }
        return SINGLETON;
    }

    @Override
    public CritterType getCritterType() {
        return CritterType.GUIDE;
    }

    public Game getController() {
        return controller;
    }

    private void createCrossroads() {
        String basic = "You have entered the Guide's Cross roads, one of the few safe zones in the worlds. The Guide offers to take you to one of the other realms. ";
        String end = "Or do you prefer to stay where you are?";

        final Lambda cancelAction = this::exitZone;
        final String title = "Crossroads";
        
        // 
        Zone fromGround = new Zone(controller,this,3) {};
        fromGround.setIcon(controller.GRAPHICS.NPC_GUIDE);
        fromGround.setZoneTitle(title);
        fromGround.onCancel(cancelAction);
        fromGround.addOption("Sky", ()->controller.scene.travel(SceneType.SKY));
        fromGround.addOption("Dungeon", ()->controller.scene.travel(SceneType.DUNGEON));
        fromGround.setDialog(basic + "Will you travel to the more energy-demanding Sky realm or explore the dungeons of the Underground realm? ");
        this.crossroads[0] = fromGround;

        Zone elsewhere = new Zone(controller,this,3) {};
        elsewhere.setIcon(controller.GRAPHICS.NPC_GUIDE);
        elsewhere.setZoneTitle(title);
        elsewhere.onCancel(cancelAction);
        elsewhere.addOption("Ground", ()->controller.scene.travel(SceneType.GROUND));
        elsewhere.setDialog(basic + "Do you wish to return to the Ground Scene (an maybe the end game) ? ");
        this.crossroads[1] = elsewhere;

        // GROUND
        // Crossroads ground = new Crossroads(3);
        // ground.addOption("Sky", ()->controller.scene.travel(SceneType.SKY));
        // ground.addOption("Dungeon", ()->controller.scene.travel(SceneType.DUNGEON));
        // ground.setDialog(basic + "Will you travel to the more energy-demanding Sky realm or explore the dungeons of the Underground realm? ");
        // this.crossroads[0] = ground;

        // // SKY
        // Crossroads sky = new Crossroads(2);
        // sky.addOption("Ground", ()->controller.scene.travel(SceneType.GROUND));
        // sky.setDialog(basic + "Do you wish to return to the Ground Scene (an maybe the end game) ? ");
        // this.crossroads[1] = sky;
    }


    @Override
    public void tick(float lastTime, Game game) {
        super.tick(lastTime, controller);

        // Verify if we are entering a zone:
        if (this.collides(game.hero, game)) {
            if (Keyboard.isKeyPressed(Keyboard.Key.I)) {
                this.enterZone();
                game.setClockState(false);
            }
        }
        // this.handleCollision(game, ()->{
        //     if (Keyboard.isKeyPressed(Keyboard.Key.I)) {
        //         this.enterZone();
        //         game.setClockState(false);
        //     }
        // });
    }

    @Override
    public void setVisibility(boolean visible) {
        // Not visible anymore
        // TODO : GuideManager observes this
        if (this.isVisibleYet && !visible) {
            notifyObservers(new ObservableEvent(this,EVENT_GUIDE_OFF));
        } else if (!this.isVisibleYet && visible) {
            notifyObservers(new ObservableEvent(this,EVENT_GUIDE_ON));
            IntRect pixels = this.getPixelBounds(controller);
        }
        super.setVisibility(visible);
    }

    /**
     * Makes the player enter the zone (scene.zoom) of this Guide
     * 
     */
    public void enterZone() {
        // System.out.println("Entering the zone");
        // System.out.println("Type int : " + controller.scene.getSceneType().ordinal());
        this.zone = this.crossroads[controller.scene.getSceneType()==SceneType.GROUND ? 0 : 1];
        this.zone.addToRenderer(controller.gameRenderer);
        this.controller.setClockState(false);
    }

    @Override
    public FloatRect getRectangleArea() {
        final GameScene scene = controller.scene;
        FloatRect bounds = this.getBounds();
        // FloatRect rect = new FloatRect(bounds.left, bounds.top-50f, bounds.width, bounds.height + controller.scene.getPlatformTileSize().y);
        float top = this.platformLevel == scene.getNumberFloors()-1? scene.getSceneSize().y-scene.computePlatformerHeight() : scene.getFloorPosition(this.platformLevel+1);
        float left = bounds.left+bounds.width - 3*scene.getPlatformTileSize().x;
        final FloatRect rect = new FloatRect(left, top, 3f*scene.getPlatformTileSize().x, scene.getPlatformLevelSize().y + scene.getPlatformTileSize().y);

        return rect;
    }

    public void exitZone() {
        // start the fade transition and zoom out
        // System.out.println("Well, bye I guess");
        this.zone.removeFromRenderer(controller.gameRenderer);
        this.controller.setClockState(true);
    }

    public class Crossroads extends Zone 
    {
        Guide guide = Guide.this;


        public Crossroads(int options) {
            super(Guide.this.controller, Guide.this, options);

            // this.setBackground(controller.GRAPHICS.HOMESCREEN_BACKGROUND);
            this.setIcon(controller.GRAPHICS.NPC_GUIDE);
            this.setZoneTitle("Crossroads");
            // String text = "You have entered the Guide's Cross roads, one of the few safe zones in the worlds. "+
            //     "The Guide offers to take you to one of the other realms. Will you travel to the more aerial Sky realm?";
            // this.setDialog(text);
            this.onCancel(Guide.this::exitZone);
        }
        
    }



}
