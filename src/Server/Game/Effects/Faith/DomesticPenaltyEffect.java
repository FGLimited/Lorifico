package Server.Game.Effects.Faith;

import Game.Effects.Effect;
import Game.Effects.EffectType;
import Game.UserObjects.DomesticColor;
import Game.UserObjects.PlayerState;
import Server.Game.UserObjects.Domestic;

/**
 * Created by fiore on 20/05/2017.
 */
public class DomesticPenaltyEffect implements Effect {

    private final EffectType type = EffectType.Permanent;

    private final int penalty;

    /**
     * Apply specified value penalty to all non neutral domestics
     *
     * @param penalty Penalty value
     */
    public DomesticPenaltyEffect(int penalty) {
        this.penalty = penalty;
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

        final Domestic inUse = currentMove.getInUseDomestic();

        if(inUse.getType() != DomesticColor.Neutral)
            inUse.setValue(inUse.getValue() - penalty);

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
