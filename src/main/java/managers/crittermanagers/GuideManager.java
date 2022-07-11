package managers.crittermanagers;

import java.util.ArrayList;
import java.util.List;

import org.jsfml.window.Keyboard.Key;
import org.jsfml.window.event.Event;

import controllers.Game;
import devtools.*;
import devtools.Observer;
import hero.Hero;
import managers.AbstractManager;
import npc.Guide;
public class GuideManager extends AbstractManager<Guide> implements Observer {

    /**
     * Whether the singleton guide is already on screen. There can only be one guide
     * at a time
     */
    protected boolean guideOnScreen;

    private static GuideManager SINGLETON = null;

    protected GuideManager(Game game) {
        super(game);
    }

    public static GuideManager get(Game game) {
        if (SINGLETON == null) {
            SINGLETON = new GuideManager(game);
        }
        return SINGLETON;
    }

    @Override
    protected Guide create() {
        return Guide.get(this.controller);
    }

    public boolean isGuideOnScreen() {
        return guideOnScreen;
    }

    @Override
    public List<Guide> spawn(float time) {
        if (this.guideOnScreen)
            return new ArrayList<>();
        List<Guide> guide = super.spawn(time);
        // if (!guide.isEmpty()) {
        // this.guideOnScreen = true;
        // }
        return guide;
    }
 
    @Override
    public void receiveUpdate(ObservableEvent event) {
        int id = event.getEventID();
        if (id == Guide.EVENT_GUIDE_ON) {
            this.guideOnScreen = true;
        } else if (id == Guide.EVENT_GUIDE_OFF) {
            this.guideOnScreen = false;
        }
    }


    
    
}
