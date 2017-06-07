package Action;

import Client.Datawarehouse;
import Logging.Logger;
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
        Datawarehouse.getInstance().setMyUser(userRemote);
        Logger.log(Logger.LogLevel.Normal, "Remote user received");
    }
}
