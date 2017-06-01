package Action;

import Game.Cards.CardType;
import Model.User.User;
import Server.Game.Positions.TowerPosition;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by fiore on 01/06/2017.
 */
public class TowersUpdate implements BaseAction {

    /**
     * Map&lt;Position_number, Card_number&gt;
     */
    private final Map<Integer, Integer> cardPerPosition = new HashMap<>();

    public TowersUpdate(Map<CardType, List<TowerPosition>> towerPositions) {
        towerPositions.values().forEach(tower ->
                tower.forEach(position ->
                        cardPerPosition.put(position.getNumber(), position.getCard().getNumber())
                )
        );
    }

    @Override
    public void doAction(User user) {
        // TODO: update cards in all towers positions
        // TODO: free all positions (all means all towers, market, council, harvest and production positions)
    }
}
