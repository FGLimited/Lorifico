package Action;

import Client.Datawarehouse;
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
        Datawarehouse.getInstance().setWhoseTurn(getUsername());
    }
}
