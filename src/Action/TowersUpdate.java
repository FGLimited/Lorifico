package Action;

import Client.UI.UserInterface;
import Client.UI.UserInterfaceFactory;
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
        UserInterface userInterface = UserInterfaceFactory.getInstance();

        //Remove old stuff from previous turn
        userInterface.getGameUI().getTowersController().removeAllCardsFromTowers();
        userInterface.getGameTable().freeAllPositions();//Frees all position from gameTable removing all domestics

        //Add stuff of new turn
        cardPerPosition.forEach(((cardNumber, gamePosition) -> userInterface.getGameUI().getTowersController().showCardOnTowers(gamePosition, cardNumber)));
    }
}
