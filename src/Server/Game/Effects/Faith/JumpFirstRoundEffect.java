package Server.Game.Effects.Faith;

import Server.Game.Effects.Effect;
import Game.Effects.EffectType;
import Game.UserObjects.PlayerState;
import Server.Game.UserObjects.GameUser;

/**
 * Created by fiore on 17/05/2017.
 */
public class JumpFirstRoundEffect extends Effect {

    private volatile boolean applied = false;

    public JumpFirstRoundEffect() {
        super(EffectType.Permanent, 0);
    }

    @Override
    public boolean canApply(PlayerState currentMove) {
        return !applied;
    }

    @Override
    public void apply(PlayerState currentMove) {

        if(applied)
            return;

        GameUser currentUser = (GameUser) currentMove.getGameUser();
        currentUser.setRoundJump(true);

        applied = true;
    }

}
