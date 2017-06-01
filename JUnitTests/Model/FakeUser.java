package Model;

import Model.User.User;
import Networking.CommLink;

/**
 * Created by fiore on 01/06/2017.
 */
public class FakeUser extends User {
    public FakeUser(CommLink clientLink) {
        super("FakeUser", 0, 0, 0, clientLink);
    }
}
