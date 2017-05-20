package Server.Game.Effects;

import Game.Effects.Effect;
import Game.Effects.EffectType;
import Game.Positions.PositionType;
import Game.UserObjects.PlayerState;
import Server.Game.UserObjects.Domestic;

/**
 * Created by fiore on 19/05/2017.
 */
public class PositionBonusEffect implements Effect {

    private final EffectType type = EffectType.Permanent;

    private final PositionType position;

    private final int value;

    /**
     * Initialize a bonus/penalty effect for harvest/production position
     *
     * @param positionType Position to activate this effect
     * @param bonusValue Bonus/penalty to apply to domestic value
     */
    public PositionBonusEffect(PositionType positionType, int bonusValue) {
        position = positionType;
        value = bonusValue;
    }

    @Override
    public EffectType getType() {
        return type;
    }

    @Override
    public boolean canApply(PlayerState currentMove) {

        return currentMove.getCheckingPositionType() == position;
    }

    @Override
    public void apply(PlayerState currentMove) {

        // Get in use domestic
        final Domestic inUse = currentMove.getInUseDomestic();

        // Apply bonus/penalty value
        inUse.setValue(inUse.getValue() + value);
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
