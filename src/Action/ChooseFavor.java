package Action;

import Client.UI.UserInterfaceFactory;
import Game.Effects.Effect;
import Model.User.User;
import Server.Game.GameHelper;

import java.util.List;

/**
 * Created by fiore on 25/05/2017.
 */
public class ChooseFavor implements BaseAction {

    private final List<Effect> councilFavors = GameHelper.getInstance().getCouncilFavors();

    private final int differentFavors;

    /**
     * Request to the user to choose council favors
     *
     * @param favors Number of favors to choose
     */
    public ChooseFavor(int favors) {
        differentFavors = favors;
    }

    @Override
    public void doAction(User user) {
        UserInterfaceFactory.getInstance().displayPopup(DisplayPopup.Level.Normal, "Consiglio", "Scegli i favori");
        // TODO: prompt the user to chose correct number of favors
    }
}
