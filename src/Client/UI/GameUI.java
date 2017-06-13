package Client.UI;

import Action.SetInUseDomestic;
import Server.Game.UserObjects.Domestic;

/**
 * Created by Io on 27/05/2017.
 */
public interface GameUI {
    void showPage();

    TowersController getTowersController();

    DiceController getDiceController();

    FaithRoadController getFaithController();

    RoundOrderController getRoundOrderController();

    DomesticsController getDomesticsController();

    void addSlaveToSpecialDomestic(Domestic domestic, SetInUseDomestic setInUseDomesticAction);
}
