package Action;

import Client.Datawarehouse;
import Client.UI.UserInterfaceFactory;
import Game.UserObjects.FamilyColor;
import Model.User.User;

/**
 * Created by fiore on 06/06/2017.
 */
public class FaithPenaltyApplied extends UserSpecific implements BaseAction {

    private final int faithCard;

    public FaithPenaltyApplied(String username, int cardNumber) {
        super(username);
        faithCard = cardNumber;
    }

    @Override
    public void doAction(User user) {
        int cardOrdinal = (faithCard - 1) / 7 + 1;
        FamilyColor familyColor = Datawarehouse.getInstance().getFamilyColor(getUsername());
        UserInterfaceFactory.getInstance().getGameUI().getFaithController().showFaithCube(familyColor, cardOrdinal);
    }
}
