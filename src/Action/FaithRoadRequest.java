package Action;

import Model.User.User;

/**
 * Created by fiore on 06/06/2017.
 */
public class FaithRoadRequest implements BaseAction {
    @Override
    public void doAction(User user) {
        // TODO: ask user if wants to support the church or not
        boolean userChoice = false;

        BaseAction faithChoice = new FaithRoadChoice(userChoice);

        // TODO: send faithChoice back to the server
    }
}
