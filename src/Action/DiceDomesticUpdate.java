package Action;

import Client.Datawarehouse;
import Client.UI.UserInterfaceFactory;
import Game.UserObjects.DomesticColor;
import Model.User.User;
import Server.Game.UserObjects.Domestic;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by fiore on 01/06/2017.
 */
public class DiceDomesticUpdate extends UserSpecific implements BaseAction {

    private final Map<DomesticColor, Integer> diceValues;

    private final Map<DomesticColor, Integer> domesticValues = new HashMap<>();

    public DiceDomesticUpdate(String username, Map<DomesticColor, Integer> dice, Map<DomesticColor, Domestic> domestics) {
        super(username);
        this.diceValues = dice;
        domestics.values().forEach(domestic -> domesticValues.put(domestic.getType(), domestic.getValue()));
    }

    @Override
    public void doAction(User user) {

        if (Datawarehouse.getInstance().getMyUsername().equals(getUsername())) {
            //Update dices only once (when my username is received)
            UserInterfaceFactory.getInstance().getGameUI().getDiceController().setNumbers(diceValues.get(DomesticColor.Black),
                    diceValues.get(DomesticColor.White), diceValues.get(DomesticColor.Orange));

            //Updates user's domestics
            UserInterfaceFactory.getInstance().getGameUI().getDomesticsController().updateDomesticsValues(domesticValues);

            //Removes all domestics from GameTable
            UserInterfaceFactory.getInstance().getGameUI().getTowersController().removeAllDomestics();
        }
    }
}
