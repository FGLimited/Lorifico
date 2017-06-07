package Action;

import Client.UI.UserInterfaceFactory;
import Game.Effects.Effect;
import Model.User.User;

import java.util.Map;

/**
 * Created by fiore on 01/06/2017.
 */
public class FaithCardsUpdate implements BaseAction {

    private int firstCard;

    private int secondCard;

    private int thirdCard;

    public FaithCardsUpdate(Map<Integer, Effect> faithDeckResult) {
        firstCard = faithDeckResult.get(2).getCardNumber();
        secondCard = faithDeckResult.get(4).getCardNumber();
        thirdCard = faithDeckResult.get(6).getCardNumber();
    }

    @Override
    public void doAction(User user) {
        UserInterfaceFactory.getInstance().getGameUI().getFaithController().showFaithCards(firstCard, secondCard, thirdCard);
    }
}
