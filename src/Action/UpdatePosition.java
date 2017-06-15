package Action;

import Client.UI.UserInterfaceFactory;
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

        if(occupant.getType() == null)
            return;

        if (positionNumber > 16) {
            UserInterfaceFactory.getInstance().getGameTable().addDomestic(occupant, positionNumber);
        } else {
            UserInterfaceFactory.getInstance().getGameUI().getTowersController().addDomestic(occupant, positionNumber);
        }

    }
}
