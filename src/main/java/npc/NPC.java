package npc;

import models.Critter;

import org.jsfml.graphics.FloatRect;
import org.jsfml.graphics.Sprite;
import org.jsfml.system.Vector2f;
import platformer.AbstractScene;
import platformer.Platformer;
import ui.PanelOverlay;

/**
 * A representation of a non-playing character, which have a safe zone in which
 * the player can "rest" for a while. The zone may contain a dialog box with dialog options
 * to communicate with the NPC.
 * 
 * @see Zone
 * 
 */
public class NPC extends Critter {

    public static final int EVENT_IN_ZONE = 901;
    public static final int EVENT_OUT_ZONE = - EVENT_IN_ZONE;

    protected Sprite area;

    protected Zone zone;

    @Override
    public void placeInScene(AbstractScene scene, Vector2f platformPosition) {
        this.setPosition(platformPosition.x, platformPosition.y-this.getBounds().height);
    }

    @Override
    public void place(Platformer platformer, Vector2f platformPosition) {
        float x = platformPosition.x + (platformer.getPlatformTileSize().x - this.getBounds().width)/2;
        float y = platformPosition.y - this.getSize().y;
        this.setPosition(x, y);
    }

    public FloatRect getRectangleArea() {
        return this.area.getGlobalBounds();
    }

    public Zone getZone(){
        return this.zone;
    }

    @Override
    public int getTileCountRequirements() {
        return 3;
    }


}