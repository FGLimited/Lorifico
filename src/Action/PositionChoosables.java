package Action;

import Client.UI.UserInterfaceFactory;
import Game.UserObjects.Choosable;
import Model.User.User;

import java.util.List;
import java.util.Map;

/**
 * Created by fiore on 06/06/2017.
 */
public class PositionChoosables implements BaseAction {

    private final Map<Integer, List<Choosable>> choosablePerPos;

    /**
     * Initialize positions cost/effect message to send to the client
     *
     * @param choosablePerPos Cost/Effect available for each position
     */
    public PositionChoosables(Map<Integer, List<Choosable>> choosablePerPos) {
        this.choosablePerPos = choosablePerPos;
    }

    @Override
    public void doAction(User user) {
        // (empty cost list means position can't be activated)
        // (empty effect list means no effect can be activated but domestic can be placed)

        UserInterfaceFactory.getInstance().getGameUI().getTowersController().setCostsPerPosition(choosablePerPos);
        UserInterfaceFactory.getInstance().getGameTable().setCostsPerPosition(choosablePerPos);
    }
}
