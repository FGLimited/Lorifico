package Action;

import Game.Effects.Effect;
import Model.User.User;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by fiore on 01/06/2017.
 */
public class FaithCardsUpdate implements BaseAction {

    /**
     * Map&lt;Age_number, Faith_card_number&gt;
     */
    private final Map<Integer, Integer> cardPerAge = new HashMap<>();

    public FaithCardsUpdate(Map<Integer, Effect> faithDeckResult) {
        cardPerAge.put(1, faithDeckResult.get(2).getCardNumber());
        cardPerAge.put(2, faithDeckResult.get(4).getCardNumber());
        cardPerAge.put(3, faithDeckResult.get(6).getCardNumber());
    }

    @Override
    public void doAction(User user) {
        // TODO: set all three faith cards in position
    }
}
