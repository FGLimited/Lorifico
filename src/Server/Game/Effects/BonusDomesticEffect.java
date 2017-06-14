package Server.Game.Effects;

import Action.BonusDomesticMove;
import Game.Effects.EffectType;
import Game.Positions.PositionType;
import Game.Usable.ResourceType;
import Game.UserObjects.DomesticColor;
import Game.UserObjects.PlayerState;
import Server.Game.Usable.Cost;
import Server.Game.UserObjects.Domestic;
import Server.Game.UserObjects.GameUser;
import java.util.List;
import java.util.Map;

/**
 * Created by fiore on 21/05/2017.
 */
public class BonusDomesticEffect extends Effect {

    private final List<PositionType> positionsType;

    private final int value;

    private final Map<ResourceType, Integer> costBonus;

    /**
     * Additional placement with special domestic of given value in specified positions
     * If tower positions costBonus is applied to card costs
     *
     * @param positions Type of position to activate (null for all positions)
     * @param domesticValue Value of special domestic
     * @param costBonus Bonus resources for position/card cost
     */
    public BonusDomesticEffect(List<PositionType> positions, int domesticValue, Map<ResourceType, Integer> costBonus) {
        super(EffectType.Immediate, 0);
        this.positionsType = positions;
        value = domesticValue;
        this.costBonus = costBonus;
    }

    /**
     * Gson constructor
     */
    private BonusDomesticEffect() {
        positionsType = null;
        value = 0;
        costBonus = null;
    }

    @Override
    public boolean canApply(PlayerState currentMove) {
        return true;
    }

    @Override
    public void apply(PlayerState currentMove) {

        GameUser currentUser = (GameUser) currentMove.getGameUser();

        // Set special neutral domestic
        Domestic special = new Domestic(currentMove.getGameUser().getFamilyColor(), DomesticColor.Neutral, value);

        // Set not moved for current user
        currentUser.setHasMoved(false);

        // Send special domestic and bonus positions type
        currentUser.getUserLink()
                .sendMessage(new BonusDomesticMove(special, positionsType, new Cost(costBonus)));
    }
}
