package Server.Game.Effects.Faith;

import Server.Game.Effects.Effect;
import Game.Effects.EffectType;
import Game.UserObjects.DomesticColor;
import Game.UserObjects.GameUser;
import Game.UserObjects.PlayerState;

/**
 * Created by fiore on 20/05/2017.
 */
public class DomesticPenaltyEffect extends Effect {

    private final int penalty;

    private volatile boolean isApplied = false;

    /**
     * Apply specified value penalty to all non neutral domestics
     *
     * @param penalty Penalty value
     */
    public DomesticPenaltyEffect(int penalty) {
        super(EffectType.Permanent, 0);
        this.penalty = penalty;
    }

    @Override
    public boolean canApply(PlayerState currentMove) {
        return !isApplied;
    }

    @Override
    public void apply(PlayerState currentMove) {

        if (isApplied)
            return;

        final GameUser user = currentMove.getGameUser();

        user.setDomesticPenalty(DomesticColor.Black, penalty);
        user.setDomesticPenalty(DomesticColor.Orange, penalty);
        user.setDomesticPenalty(DomesticColor.White, penalty);

        isApplied = true;
    }

}
