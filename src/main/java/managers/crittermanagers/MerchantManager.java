package managers.crittermanagers;

import java.util.ArrayList;
import java.util.List;

import controllers.Game;
import devtools.ObservableEvent;
import devtools.Observer;
import managers.AbstractManager;
import npc.Merchant;

public class MerchantManager extends AbstractManager<Merchant> implements Observer {

    /**
     * Whether the singleton merchant is already on screen. There can only be one guide
     * at a time
     */
    protected boolean storeOpen = false;

    private static MerchantManager SINGLETON = null;

    protected MerchantManager(Game game) {
        super(game);
    }

    public static MerchantManager get(Game game) {
        if (SINGLETON == null) {
            SINGLETON = new MerchantManager(game);
        }
        return SINGLETON;
    }

    @Override
    protected Merchant create() {
        return Merchant.get(controller);
    }


    @Override
    public List<Merchant> spawn(float time) {
        if (this.storeOpen)
            return new ArrayList<>();
        List<Merchant> guide = super.spawn(time);
        // if (!guide.isEmpty()) {
        // this.guideOnScreen = true;
        // }
        return guide;
    }
 
    @Override
    public void receiveUpdate(ObservableEvent event) {
        int id = event.getEventID();
        if (id == Merchant.EVENT_MERCHANT_ON) {
            this.storeOpen = true;
        } else if (id == Merchant.EVENT_MERCHANT_OFF){
            this.storeOpen = false;
        }
    }

	

}
