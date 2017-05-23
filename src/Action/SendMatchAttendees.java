package Action;

import Client.UI.UserInterfaceFactory;
import Model.User.User;

import java.util.List;

/**
 * Created by Io on 23/05/2017.
 */
public class SendMatchAttendees implements BaseAction {
    private List<User> userList;

    public SendMatchAttendees(List<User> userList) {
        this.userList = userList;
    }

    @Override
    public void doAction(User user) {
        UserInterfaceFactory.getInstance().getLobby().setMatchAttendees(userList);
        if (userList.size() > 1) {
            UserInterfaceFactory.getInstance().getLobby().restartTimer();
        }
    }
}
