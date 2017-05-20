package Server.Game.Effects.Faith;

import Game.Effects.Effect;
import Game.Effects.EffectType;
import Game.UserObjects.PlayerState;

/**
 * Created by fiore on 20/05/2017.
 */
public class SlaveValuePenaltyEffect implements Effect {

    private final EffectType type = EffectType.Permanent;

    private final int slavePerValue;

    private volatile boolean isApplied = false;

    /**
     * Initialize penalty effect on domestic value increment using slaves
     *
     * @param slavePerValue Slaves necessary to increment domestic value by one
     */
    public SlaveValuePenaltyEffect(int slavePerValue) {
        this.slavePerValue = slavePerValue;
    }

    @Override
    public EffectType getType() {
        return type;
    }

    @Override
    public boolean canApply(PlayerState currentMove) {
        return !isApplied;
    }

    @Override
    public void apply(PlayerState currentMove) {

        if(isApplied)
            return;

        currentMove.setSlavePerDomesticValue(slavePerValue);

        isApplied = true;
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
