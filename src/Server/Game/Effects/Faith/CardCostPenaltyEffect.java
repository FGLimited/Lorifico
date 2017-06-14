package Server.Game.Effects.Faith;

import Game.Cards.CardType;
import Server.Game.Effects.Effect;
import Game.Effects.EffectType;
import Game.Usable.ResourceType;
import Game.UserObjects.PlayerState;
import Server.Game.Usable.Cost;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by fiore on 20/05/2017.
 */
public class CardCostPenaltyEffect extends Effect {

    private final CardType cardType;

    private final List<ResourceType> resourcesType;

    private final int removedPoints;

    private volatile boolean isApplied = false;

    /**
     * Remove specified amount of victory points for each of the specified resources present in
     * each card cost of given card type
     *
     * @param resources Resources to check in card cost
     * @param pointsToRemove Points to remove for each resource found
     * @param cardType Type of cards to check
     */
    public CardCostPenaltyEffect(List<ResourceType> resources, int pointsToRemove, CardType cardType) {
        super(EffectType.Permanent, 0);
        this.cardType = cardType;
        resourcesType = resources;
        removedPoints = pointsToRemove;
    }

    @Override
    public boolean canApply(PlayerState currentMove) {
        return !isApplied;
    }

    @Override
    public void apply(PlayerState currentMove) {

        // Get all costs from given card type
        final List<Cost> cardCosts = new ArrayList<>();
        cardCosts.clear();
        currentMove.getCards(cardType).forEach(card -> cardCosts.addAll(card.getCosts()));

        final AtomicInteger pointsToRemove = new AtomicInteger();
        pointsToRemove.set(0);

        // Check each cost to find requested resources and add resource value to removed points counter
        cardCosts.forEach(cost -> {

            // Current cost resources
            final Map<ResourceType, Integer> current = cost.getResources();

            // If requested resource is present increment pointsToRemove
            resourcesType.forEach(resource -> {
                if(current.containsKey(resource))
                    pointsToRemove.set(current.get(resource) * removedPoints);
            });

        });

        // Get current user resources
        Map<ResourceType, Integer> currentResources = currentMove.getResources();

        // Remove victory points
        currentResources.replace(ResourceType.VictoryPoint, currentResources.get(ResourceType.VictoryPoint) - pointsToRemove.get());

        // Update user resources
        currentMove.setResources(currentResources, false);

        isApplied = true;

    }
}
