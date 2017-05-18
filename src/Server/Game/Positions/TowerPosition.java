package Server.Game.Positions;

import Game.Cards.Card;
import Game.Cards.CardType;
import Game.Effects.EffectType;
import Game.Positions.Position;
import Game.Positions.PositionType;
import Server.Game.UserObjects.Domestic;
import Game.UserObjects.PlayerState;
import Server.Game.Usable.Cost;
import Game.Effects.Effect;
import Game.Usable.ResourceType;
import org.jetbrains.annotations.Nullable;
import java.util.Collections;
import java.util.List;

/**
 * Created by fiore on 10/05/2017.
 */
public class TowerPosition implements Position<Cost> {

    private final int number;

    private final PositionType type;

    private transient volatile PositionAggregate tower;

    private final Effect immediatePositionEffect;

    private final int positionValue;

    private final CardType cardType;

    private volatile Card currentCard;

    private volatile Domestic occupant;

    /**
     * Initialize a new tower position
     *
     * @param immediateEffect Immediate effect activated by domestic in this position
     * @param value Necessary value of the domestic for this position
     * @param cardType Type of card which will be in this position
     * @param number Position id number
     */
    public TowerPosition(Effect immediateEffect, int value, CardType cardType, int number) {
        this.number = number;
        type = cardType.PositionType;
        immediatePositionEffect = immediateEffect;
        positionValue = value;
        this.cardType = cardType;
    }

    @Override
    public int compareTo(Position other) {
        return number - other.getNumber();
    }

    /**
     * Get position card type
     *
     * @return Position card type
     */
    public Card getCard() {
        return currentCard;
    }

    /**
     * Put given card in this position
     *
     * @param cardToBind Card to put
     * @throws IllegalArgumentException If card type is different from position card type
     */
    public void setCard(Card cardToBind) throws IllegalArgumentException {

        if(currentCard.getType() != cardType)
            throw new IllegalArgumentException("Wrong card type! Can't put " + currentCard.getType().name() + " card in " + cardType.name() + " position.");

        currentCard = cardToBind;
    }

    @Override
    public int getNumber() {
        return number;
    }

    @Override
    public List<Cost> canOccupy(PlayerState currentState) {

        // Apply user permanent effects to his current state
        applyEffets(currentState);

        // If position is occupied no one else can be there
        // or if there isn't a card
        // or if domestic value is too low
        // or if non neutral domestic of same family is already present in the tower
        if(occupant != null
                || currentCard == null
                || currentState.getInUseDomestic().getValue() < positionValue
                || !tower.canOccupy(currentState.getInUseDomestic()))
            return Collections.emptyList();

        final Cost occupiedCost;

        // If tower is already occupied add 3 gold units cost
        if(tower.isOccupied()) {

            // Initialize cost with 3 gold units request
            occupiedCost = new Cost(Collections.singletonMap(ResourceType.Gold, 3));

            // Check if user can afford this
            if(!occupiedCost.canBuy(currentState))
                return Collections.emptyList();

            // If is affordable apply it to current state
            occupiedCost.apply(currentState);
        }
        else
            occupiedCost = null;

        // Check if card in current position is affordable
        List<Cost> affordableCosts = currentCard.canBuy(currentState);

        // If card isn't affordable return false or occupation cost isn't present return card cost only
        if(affordableCosts.isEmpty() || occupiedCost == null)
            return affordableCosts;

        // Else create new cost list
        List<Cost> totalCosts = Collections.emptyList();

        // Add occupation cost to card cost and populate totalCosts list
        affordableCosts.forEach(cardCost -> totalCosts.add(cardCost.sum(occupiedCost)));

        // Return total costs' list
        return totalCosts;
    }

    /**
     * Apply player permanent effects to current state
     *
     * @param currentState Current player state
     */
    private void applyEffets(PlayerState currentState) {

        // Set current position type
        currentState.setCheckingPositionType(type);

        // Apply all permanent effects
        currentState.getEffects(EffectType.Permanent)
                .forEach(effect -> effect.apply(currentState));
    }

    @Override
    public PlayerState occupy(PlayerState currentState, Cost chosenCost) {

        // Apply user permanent effects to his current state
        applyEffets(currentState);

        // Apply position and card cost chosen
        chosenCost.apply(currentState);

        // Update position state
        occupant = currentState.getInUseDomestic();

        occupant.setInPosition(true);

        // Apply position immediate effect
        if(immediatePositionEffect != null)
            immediatePositionEffect.apply(currentState);

        // Add card to user state
        currentState.addCard(currentCard);

        // Remove card from position
        currentCard = null;

        // Return updated player state
        return currentState;
    }

    @Override
    public PlayerState occupy(PlayerState currentState, List<Cost> chosenCosts) {
        return occupy(currentState, chosenCosts.get(0));
    }

    @Override
    public @Nullable Domestic isOccupied() {
        return occupant;
    }

    @Override
    public void free() {

        occupant.setInPosition(false);
        occupant = null;
    }

    @Override
    public void setAggregate(PositionAggregate parent) {
        if(tower == null)
            tower = parent;
    }

    @Override
    public PositionType getType() {
        return type;
    }
}
