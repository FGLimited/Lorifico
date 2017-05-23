package Server.Game.Effects.Faith;

import Game.Effects.Effect;
import Game.Effects.EffectType;
import Game.UserObjects.DomesticColor;
import Game.UserObjects.GameUser;
import Game.UserObjects.PlayerState;

/**
 * Created by fiore on 20/05/2017.
 */
public class DomesticPenaltyEffect implements Effect {

    private final EffectType type = EffectType.Permanent;

    private final int penalty;

    private volatile boolean isApplied = false;

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
