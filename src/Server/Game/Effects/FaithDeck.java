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
     * Get faith effects for this game
     *
     * @return Three faith effects
     */
    public Map<Integer, Effect> getFaithEffect() {
        Map<Integer, Effect> gameFaithEffects = new HashMap<>();

        gameFaithEffects.put(1, null);
        gameFaithEffects.put(2, faithEffects.get(1).get(0));
        gameFaithEffects.put(3, null);
        gameFaithEffects.put(4, faithEffects.get(2).get(0));
        gameFaithEffects.put(5, null);
        gameFaithEffects.put(6, faithEffects.get(3).get(0));

        return gameFaithEffects;
    }

}
