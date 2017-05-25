package Action;

import Model.User.User;
import Server.Game.UserObjects.Domestic;

/**
 * Created by fiore on 25/05/2017.
 */
public class BonusDomesticMove implements BaseAction {

    private final Domestic specialDomestic;

    // TODO: add list of bonus positions


    public BonusDomesticMove(Domestic special/* TODO: initalize bonus positions list */) {
        specialDomestic = special;
    }

    @Override
    public void doAction(User user) {

        /*
        // TODO: ask user to add slaves if necessary, than send update to the server

        BaseAction setSpecialDomestic = new SetInUseDomestic(specialDomestic, slaves);
        */

        // TODO: ask user to move on one of the bonus positions

        // TODO: send move request via Action.Move object as usual

    }
}
