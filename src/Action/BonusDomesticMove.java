package Action;

import Game.Positions.PositionType;
import Model.User.User;
import Server.Game.UserObjects.Domestic;

import java.util.List;

/**
 * Created by fiore on 25/05/2017.
 */
public class BonusDomesticMove implements BaseAction {

    private final Domestic specialDomestic;

    private final List<PositionType> bonusPositions;

    /**
     * Bonus domestic move message
     *
     * @param special Special domestic of null type with bonus value
     * @param bonusPositions Bonus positions activated for the special domestic move
     */
    public BonusDomesticMove(Domestic special, List<PositionType> bonusPositions) {
        specialDomestic = special;
        this.bonusPositions = bonusPositions;
    }

    @Override
    public void doAction(User user) {

        // TODO: ask user to add slaves if necessary, than send update to the server
        int slaves = 0;

        BaseAction setSpecialDomestic = new SetInUseDomestic(specialDomestic, slaves, bonusPositions);

        // TODO: send in use domestic to server
        // (a position update will be sent back as in normal domestic selection mode)

    }
}
