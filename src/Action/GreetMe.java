package Action;

import Model.User.User;

/**
 * Created by andrea on 10/05/2017.
 */

/**
 * Action sent from client to server, asks for a greet
 */
public class GreetMe implements BaseAction {
    @Override
    public void doAction(User user) {
        BaseAction displayPopup = new DisplayPopup(DisplayPopup.Level.Normal, "Ciao, ti chiami " + user.getUsername());
        user.getLink().sendMessage(displayPopup);
    }
}
