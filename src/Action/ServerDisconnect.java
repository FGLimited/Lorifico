package Action;

import Model.User.User;
import Model.UserManager;

/**
 * Created by fiore on 26/05/2017.
 */
public class ServerDisconnect implements BaseAction {
    @Override
    public void doAction(User user) {

        // TODO: abort match if necessary

        UserManager.getInstance().disconnectUser(user.getUsername());
    }
}
