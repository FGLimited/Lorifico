package Action;

import Client.Datawarehouse;
import Model.User.User;

/**
 * Created by Io on 23/05/2017.
 */
public class UpdateUserObject implements BaseAction {
    private User userRemote;

    public UpdateUserObject(User userRemote) {
        this.userRemote = userRemote;
    }

    @Override
    public void doAction(User user) {
        Datawarehouse.getInstance().setUser(userRemote);
    }
}
