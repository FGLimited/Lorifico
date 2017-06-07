package Action;

import Client.UI.UserInterfaceFactory;
import Game.UserObjects.FamilyColor;
import Model.User.User;
import Server.Game.UserObjects.GameUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fiore on 07/06/2017.
 */
public class RoundOrderUpdate implements BaseAction {

    private final List<FamilyColor> roundOrder = new ArrayList<>();

    public RoundOrderUpdate(List<GameUser> roundOrder) {
        roundOrder.forEach(user -> this.roundOrder.add(user.getFamilyColor()));
    }

    @Override
    public void doAction(User user) {
        UserInterfaceFactory.getInstance().getGameUI().getRoundOrderController().setGameOrder(roundOrder);
    }
}