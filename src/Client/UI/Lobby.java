package Client.UI;

import Model.User.User;

import java.util.List;

/**
 * Created by Io on 23/05/2017.
 */
public interface Lobby {
    void showPage();//Shows page

    void setMatchAttendees(List<User> userList);//Sets other attendees to this match

    void restartTimer();//Starts or restarts match's countdown
}
