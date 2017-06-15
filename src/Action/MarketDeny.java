package Action;

import Client.UI.UserInterfaceFactory;
import Model.User.User;

/**
 * Created by fiore on 14/06/2017.
 */
public class MarketDeny implements BaseAction {
    @Override
    public void doAction(User user) {
        UserInterfaceFactory.getInstance().getGameTable().marketDeny();
        UserInterfaceFactory.getInstance().displayPopup(DisplayPopup.Level.Normal, "Mercato", "D'ora in poi non potrai piu' utilizzare il mercato");
    }
}
