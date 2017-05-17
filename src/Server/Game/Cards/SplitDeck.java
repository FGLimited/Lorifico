package Server.Game.Cards;

import Game.Cards.*;
import Game.Cards.Card;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by fiore on 16/05/2017.
 */
public class SplitDeck {

    private final Map<Integer, Map<CardType, List<Card>>> cardPerTurn = new HashMap<>();

    public SplitDeck() {

        // TODO: load all cards from json file

    }

    /**
     * Shuffle cards
     */
    public void shuffle() {
        cardPerTurn.forEach((turnNumber, cards) ->
                cards.forEach((type, list) ->
                        Collections.shuffle(list)));
    }

    public Map<CardType, List<Card>> getCardPerTurn(Integer turnNumber) {

        Map<CardType, List<Card>> currentTurn = new HashMap<>();

        // Get requested cards from global map
        cardPerTurn.get(turnNumber).forEach((type, list) ->
                currentTurn.put(type, list.subList(turnNumber % 2 == 0 ? 4 : 0, turnNumber % 2 == 0 ? 7 : 3))
        );

        return currentTurn;
    }

}
