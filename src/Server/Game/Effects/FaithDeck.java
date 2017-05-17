package Server.Game.Effects;

import Game.Effects.Effect;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by fiore on 17/05/2017.
 */
public class FaithDeck {

    private final Map<Integer, List<Effect>> faithEffects = new HashMap<>();

    /**
     * Initialize faith cards decks
     */
    public FaithDeck() {

        // TODO: load faith effects from json

        faithEffects.forEach((turn, effects) -> Collections.shuffle(effects));

    }

    /**
     * Get effect for requested game turn
     *
     * @param turnNumber Turn number (2, 4, 6)
     * @return Relative faith effect
     */
    public Effect getFaithEffect(int turnNumber) {
        return turnNumber % 2 == 0 ? faithEffects.get(turnNumber).get(0) : null;
    }

}
