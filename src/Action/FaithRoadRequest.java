package Action;

import Client.UI.UserInterfaceFactory;
import Model.User.User;

/**
 * Created by fiore on 06/06/2017.
 */
public class FaithRoadRequest implements BaseAction {
    @Override
    public void doAction(User user) {
        UserInterfaceFactory.getInstance().getGameUI().askFaithRoad();
    }
}
