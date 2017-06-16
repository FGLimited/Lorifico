package Client.UI;

import Action.SetInUseDomestic;
import Game.Effects.Effect;
import Server.Game.UserObjects.Domestic;

import java.util.List;

/**
 * Created by Io on 27/05/2017.
 */
public interface GameUI {
    void showPage();

    TowersController getTowersController();

    DiceController getDiceController();

    FaithRoadController getFaithController();

    RoundOrderController getRoundOrderController();

    DomesticsController getDomesticsChoiceBoxController();

    void addSlaveToSpecialDomestic(Domestic domestic, SetInUseDomestic setInUseDomesticAction);

    void askCouncilFavours(List<Effect> councilFavors, int differentFavors);

    void askFaithRoad();
}
