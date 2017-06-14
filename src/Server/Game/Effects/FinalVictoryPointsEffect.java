package Server.Game.Effects;

import Game.Effects.EffectType;
import Game.Usable.ResourceType;
import Game.UserObjects.PlayerState;
import java.util.Map;

/**
 * Created by fiore on 22/05/2017.
 */
public class FinalVictoryPointsEffect extends Effect {

    private final int quantity;

    /**
     * Give specified victory points quantity at the end of the game
     *
     * @param quantity Victory points to add
     */
    public FinalVictoryPointsEffect(int quantity) {
        super(EffectType.Final, 0);
        this.quantity = quantity;
    }

    /**
     * Gson constructor
     */
    private FinalVictoryPointsEffect() {
        quantity = 0;
    }

    @Override
    public boolean canApply(PlayerState currentMove) {
        return true;
    }

    @Override
    public void apply(PlayerState currentMove) {

        Map<ResourceType, Integer> currentResources = currentMove.getResources();

        currentResources.replace(ResourceType.VictoryPoint, currentResources.get(ResourceType.VictoryPoint) + quantity);

        currentMove.setResources(currentResources, true);
    }

}
