package Action;

import Model.User.User;
import Server.Game.UserObjects.Domestic;

/**
 * Created by fiore on 01/06/2017.
 */
public class UpdatePosition implements BaseAction {

    private final int positionNumber;

    private final Domestic occupant;

    public UpdatePosition(int positionNumber, Domestic occupant) {
        this.positionNumber = positionNumber;
        this.occupant = occupant;
    }

    @Override
    public void doAction(User user) {

        // TODO: update position state moving specified domestic in place
        //       remove domestic from selection area to avoid multiple usage

    }
}
