package Server.Game.Effects.Faith;

import Action.MarketDeny;
import Server.Game.Effects.Effect;
import Game.Effects.EffectType;
import Game.UserObjects.PlayerState;

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

        // Send market deny message to client
        currentMove.getGameUser().getUserLink().sendMessage(new MarketDeny());

        isApplied = true;
    }

}
