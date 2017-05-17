package Server.Game.Effects;

import Game.Effects.Effect;
import Game.Effects.EffectType;
import Game.UserObjects.PlayerState;

/**
 * Created by fiore on 17/05/2017.
 */
public class JumpFirstRound implements Effect {

    private final EffectType type = EffectType.Permanent;

    private volatile boolean applied = false;

    @Override
    public EffectType getType() {
        return type;
    }

    @Override
    public boolean canApply(PlayerState currentMove) {
        return !applied;
    }

    @Override
    public void apply(PlayerState currentMove) {

        currentMove.getGameUser().setRoundJump(true);

        applied = true;

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
