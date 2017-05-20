package Server.Game.Effects.Faith;

import Game.Effects.Effect;
import Game.Effects.EffectType;
import Game.UserObjects.PlayerState;
import Networking.CommLink;

/**
 * Created by fiore on 20/05/2017.
 */
public class MarketDenyEffect implements Effect {

    public EffectType type = EffectType.Permanent;

    public volatile boolean isApplied = false;

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

        final CommLink userLink = currentMove.getGameUser().getUserLink();

        // TODO: send market deny message to avoid domestic positioning request in market positions

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
