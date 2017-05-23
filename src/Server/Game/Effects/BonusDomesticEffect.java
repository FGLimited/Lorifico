package Server.Game.Effects;

import Game.Effects.Effect;
import Game.Effects.EffectType;
import Game.Positions.PositionType;
import Game.Usable.ResourceType;
import Game.UserObjects.DomesticColor;
import Game.UserObjects.PlayerState;
import Server.Game.UserObjects.Domestic;
import java.util.Map;

/**
 * Created by fiore on 21/05/2017.
 */
public class BonusDomesticEffect implements Effect {

    private final EffectType type = EffectType.Immediate;

    private final PositionType positionType;

    private final int value;

    private final Map<ResourceType, Integer> costBonus;

    /**
     * Additional placement with special domestic of given value in specified positions
     * If tower positions costBonus is applied to card costs
     *
     * @param position Type of position to activate
     * @param domesticValue Value of special domestic
     * @param costBonus Bonus resources for position/card cost
     */
    public BonusDomesticEffect(PositionType position, int domesticValue, Map<ResourceType, Integer> costBonus) {
        this.positionType = position;
        value = domesticValue;
        this.costBonus = costBonus;
    }

    @Override
    public EffectType getType() {
        return type;
    }

    @Override
    public boolean canApply(PlayerState currentMove) {
        return true;
    }

    @Override
    public void apply(PlayerState currentMove) {

        // Set special neutral domestic
        Domestic special = new Domestic(currentMove.getGameUser().getFamilyColor(), DomesticColor.Neutral, value);

        // Set not moved for current user
        currentMove.getGameUser().setHasMoved(false);

        // TODO: set special as in use domestic

        // TODO: ask user to chose a card/effect from specified tower/action with given value (null positionType means any card from any tower)

    }

    @Override
    public int getActivationValue() {
        return 0;
    }

    @Override
    public int getCardNumber() {
        return 0;
    }

    @Override
    public void setCardNumber(int cardNumber) {

    }
}