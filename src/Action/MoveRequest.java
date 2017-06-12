package Action;

import Model.User.User;

/**
 * Created by fiore on 06/06/2017.
 */
public class MoveRequest extends UserSpecific implements BaseAction {

    public MoveRequest(String username) {
        super(username);
    }

    @Override
    public void doAction(User user) {
        // TODO: ask user to choose a domestic and slaves to use and send back SetInUseDomestic message
    }
}
