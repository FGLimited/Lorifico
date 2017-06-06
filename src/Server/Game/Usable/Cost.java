package Server.Game.Usable;

import Game.Usable.ResourceType;
import Game.UserObjects.Choosable;
import Game.UserObjects.PlayerState;
import java.util.*;

/**
 * Created by fiore on 09/05/2017.
 */
public class Cost implements Choosable {

    private final Map<ResourceType, Integer> requestedResources;

    private final int militaryRequested;

    private volatile int cardNumber;

    /**
     * Initialize new cost instance with given cost specific
     *
     * @param resources Requested resources
     * @param militaryRequested Requested military points
     */
    public Cost(Map<ResourceType, Integer> resources, Integer militaryRequested) {

        requestedResources = resources == null ? new HashMap<>() : UsableHelper.cloneMap(resources);

        if(militaryRequested != null)
            this.militaryRequested = militaryRequested > requestedResources.get(ResourceType.MilitaryPoint) ? militaryRequested : resources.get(ResourceType.MilitaryPoint);
        else
            this.militaryRequested = 0;

        cardNumber = 0;
    }

    public Cost(Map<ResourceType, Integer> resources) {
        this(resources, null);
    }

    /**
     * Get requested resources
     *
     * @return List of requested resources
     */
    public Map<ResourceType, Integer> getResources() {
        return requestedResources;
    }

    /**
     * Get requested military points
     *
     * @return Requested military points
     */
    public int getRequestedPoints() {
        return militaryRequested;
    }

    /**
     * Get associated card number
     *
     * @return Card number
     */
    public int getCardNumber() {
        return cardNumber;
    }

    /**
     * Set associated card umber
     *
     * @param cardNumber Associated card number
     */
    public void setCardNumber(int cardNumber) {
        this.cardNumber = cardNumber;
    }

    /**
     * Check if user has requested resources in his current state
     * (state isn't modified)
     *
     * @param currentState Current user state
     * @return True if all requested resources and points are present, false else
     */
    public boolean canBuy(PlayerState currentState) {

        return canBuyResources(currentState) && currentState.getResources().get(ResourceType.MilitaryPoint) >= militaryRequested;
    }

    /**
     * Check requested resources availability in given user state
     *
     * @param currentState User state
     * @return True if required resources are available, false else
     */
    private boolean canBuyResources(PlayerState currentState) {
        final Map<ResourceType, Integer> userResources = currentState.getResources();

        for (ResourceType type : requestedResources.keySet()) {
            if(requestedResources.get(type) > userResources.get(type))
                return false;
        }

        return true;
    }

    /**
     * Apply resources and points cost to user state
     * (state is modified without checks, negative or wrong value can occur if canBuy returned false)
     *
     * @param currentState Current user state
     */
    public void apply(PlayerState currentState) {

        // Get current state resources
        final Map<ResourceType, Integer> currentResources = currentState.getResources();

        // Apply costs to current resources
        UsableHelper.editResources(requestedResources, currentResources, false);

        // Update current state with new resources list
        currentState.setResources(currentResources, false);
    }

    /**
     * Sum this cost to the given one
     *
     * @param toSum Cost to sum to current instance
     * @param subtractSum True if toSum is added, false if is subtracted
     * @return Total resulting cost object
     */
    public Cost sum(Cost toSum, boolean subtractSum) {

        // Clone current instance
        final Cost totalCost = new Cost(requestedResources, Math.max(toSum.militaryRequested, militaryRequested));

        // Sum resources from other instance
        UsableHelper.editResources(toSum.requestedResources, totalCost.requestedResources, subtractSum);

        // Set associated card number if present
        totalCost.setCardNumber(cardNumber != 0 ? cardNumber : toSum.cardNumber);

        // Return total instance
        return totalCost;
    }

}
