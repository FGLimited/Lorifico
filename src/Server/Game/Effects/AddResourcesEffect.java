package Server.Game.Effects;

import Game.Effects.EffectType;
import Game.Positions.PositionType;
import Game.Usable.ResourceType;
import Server.Game.Usable.UsableHelper;
import Game.UserObjects.PlayerState;
import java.util.Map;

/**
 * Created by fiore on 10/05/2017.
 */
public class AddResourcesEffect extends Effect {

    private final PositionType position;

    private final Map<ResourceType, Integer> resources;

    /**
     * Initialize new add resources effect
     *
     * @param resources Resources to add
     * @param activationValue Necessary domestic value for activation
     * @param positionType Activation position type
     */
    public AddResourcesEffect(Map<ResourceType, Integer> resources, int activationValue, PositionType positionType) {
        super(EffectType.Activable, activationValue);
        position = positionType;
        this.resources = resources;
    }

    /**
     * Gson constructor
     */
    private AddResourcesEffect() {
        position = null;
        resources = null;
    }

    @Override
    public boolean canApply(PlayerState currentMove) {
        return currentMove.getInUseDomestic().getValue() >= activationValue && currentMove.getCheckingPositionType() == position;
    }

    @Override
    public void apply(PlayerState currentMove) {
        // Get current resources
        Map<ResourceType, Integer> currentResources = currentMove.getResources();

        // Add resources from effect
        UsableHelper.editResources(resources, currentResources, true);

        // Update resources on current state
        currentMove.setResources(currentResources, true);
    }

    @Override
    public String getDescription() {

        if(resources == null || resources.isEmpty())
            return "Ricevi nulla.";

        StringBuilder description = new StringBuilder("Ricevi ");

        for (Map.Entry<ResourceType, Integer> entry : resources.entrySet()) {

            description
                    .append(entry.getKey().toCostString(entry.getValue()))
                    .append(", ");
        }

        description
                .delete(description.length() - 2, description.length())
                .append(".");

        return description.toString();
    }
}
