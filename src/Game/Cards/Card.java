package Game.Cards;

import Game.Effects.Effect;
import Game.HasType;
import Server.Game.Usable.Cost;
import Game.UserObjects.PlayerState;
import java.util.List;

/**
 * Created by fiore on 10/05/2017.
 */
public interface Card extends HasType<CardType> {

    /**
     * Get card number
     *
     * @return Card number
     */
    int getNumber();

    /**
     * Get card name
     *
     * @return Card name
     */
    String getName();

    /**
     * Get card description
     *
     * @return Card description
     */
    String getDescription();

    /**
     * Get all effects (permanent and immediate) of this card
     *
     * @return List of all effects; empty if any is present
     */
    List<Effect> getEffects();

    /**
     * Get available costs to pay for this card
     *
     * @return List of available costs; empty if any is present
     */
    List<Cost> getCosts();

    /**
     * Check if a user in his current state can pick this card from its position
     *
     * @param currentState Current player state
     * @return List of affordable costs for this card; empty list if card isn't affordable
     */
    List<Cost> canBuy(PlayerState currentState);
}
