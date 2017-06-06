package Action;

import Model.User.User;

/**
 * Created by fiore on 06/06/2017.
 */
public class FaithPenaltyApplied implements BaseAction {

    private final int faithCard;

    public FaithPenaltyApplied(int cardNumber) {
        faithCard = cardNumber;
    }

    @Override
    public void doAction(User user) {
        // TODO: put penalty cube on specified faith card
    }
}
