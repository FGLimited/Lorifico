package Server.Game.Effects.Faith;

import Server.Game.Effects.Effect;
import Game.Effects.EffectType;
import Game.UserObjects.PlayerState;
import Networking.CommLink;

/**
 * Created by fiore on 20/05/2017.
 */
public class MarketDenyEffect extends Effect {

    public volatile boolean isApplied = false;

    public MarketDenyEffect() {
        super(EffectType.Permanent, 0);
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

}
