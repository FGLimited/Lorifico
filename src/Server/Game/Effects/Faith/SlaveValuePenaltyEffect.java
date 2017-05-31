package Server.Game.Effects.Faith;

import Server.Game.Effects.Effect;
import Game.Effects.EffectType;
import Game.UserObjects.PlayerState;

/**
 * Created by fiore on 20/05/2017.
 */
public class SlaveValuePenaltyEffect extends Effect {

    private final int slavePerValue;

    private volatile boolean isApplied = false;

    /**
     * Initialize penalty effect on domestic value increment using slaves
     *
     * @param slavePerValue Slaves necessary to increment domestic value by one
     */
    public SlaveValuePenaltyEffect(int slavePerValue) {
        super(EffectType.Permanent, 0);
        this.slavePerValue = slavePerValue;
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

}
