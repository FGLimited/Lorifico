package Action;

import Client.UI.UserInterfaceFactory;
import Game.Positions.PositionType;
import Model.User.User;
import Server.Game.Usable.Cost;
import Server.Game.UserObjects.Domestic;

import java.util.List;

/**
 * Created by fiore on 25/05/2017.
 */
public class BonusDomesticMove implements BaseAction {

    private final Domestic specialDomestic;

    private final List<PositionType> bonusPositions;

    private final Cost bonusCost;

    /**
     * Bonus domestic move message
     *
     * @param special Special domestic of null type with bonus value
     * @param bonusPositions Bonus positions activated for the special domestic move
     */
    public BonusDomesticMove(Domestic special, List<PositionType> bonusPositions, Cost bonusCost) {
        specialDomestic = special;
        this.bonusPositions = bonusPositions;
        this.bonusCost = bonusCost;
    }

    public BonusDomesticMove(Domestic special, List<PositionType> bonusPositions){
        this(special, bonusPositions, null);
    }

    @Override
    public void doAction(User user) {
        //Creates action to send back to server
        SetInUseDomestic setSpecialDomestic = new SetInUseDomestic(specialDomestic, 0, bonusPositions, bonusCost);

        //Creates a dialog asking for slaves, dialog on submit will send setSpecialDomestic action to server
        UserInterfaceFactory.getInstance().getGameUI().addSlaveToSpecialDomestic(specialDomestic, setSpecialDomestic);
    }
}
