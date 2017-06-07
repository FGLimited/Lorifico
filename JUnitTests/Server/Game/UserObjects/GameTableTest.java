package Server.Game.UserObjects;

import Game.Cards.CardType;
import Game.Effects.Effect;
import Game.Positions.PositionType;
import Game.UserObjects.Choosable;
import Game.UserObjects.DomesticColor;
import Game.UserObjects.FamilyColor;
import Model.FakeUser;
import Networking.FakeLink;
import Server.Game.Cards.Card;
import Server.Game.Cards.SplitDeck;
import Server.Game.Effects.Faith.FaithDeck;
import Server.Game.GameHelper;
import Server.Game.Positions.TowerPosition;
import Server.Game.Usable.Cost;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.*;

/**
 * Created by fiore on 07/06/2017.
 */
public class GameTableTest {

    private GameTable testTable;

    private Effect faithEffect;

    private GameUser testUser;

    private final List<Card> firstTowerCards = Collections.synchronizedList(new ArrayList<>());

    @Before
    public void setUp() throws Exception {
        faithEffect = new FaithDeck().getFaithEffect().get(2);

        testUser = new GameUser(new FakeUser(new FakeLink()), FamilyColor.Green);
        testUser.setDomestics(new HashMap<DomesticColor, Integer>() {
            {
                put(DomesticColor.Orange, 5);
                put(DomesticColor.Black, 4);
                put(DomesticColor.White, 6);
                put(DomesticColor.Neutral, 0);
            }
        });
        final PlayerState testState = GameHelper.getInstance().getInitialPS(testUser, 0);
        testState.setInUseDomestic(testUser.getDomestics().get(DomesticColor.Black));
        testUser.updateUserState(testState);

        final SplitDeck deck = new SplitDeck();
        deck.shuffle();
        firstTowerCards.addAll(deck.getCardPerTurn(1).get(CardType.Territory));

        testTable = GameTable.load(4);

        testTable.changeTurn(deck.getCardPerTurn(1), faithEffect);
    }

    @Test
    public void getPositions() throws Exception {
        final Map<Integer, List<Choosable>> costs = testTable
                .getPositions(testUser, Collections.singletonList(PositionType.TerritoryTower));

        final Cost firstCost = ((Cost)costs.get(1).get(0));

        Assert.assertEquals(4, costs.size());
        Assert.assertTrue(firstCost.canBuy(testUser.getUserState()));
        Assert.assertTrue(firstTowerCards.stream()
                .anyMatch(card -> card.getNumber() == firstCost.getCardNumber()));
    }

    @Test
    public void getFaithEffect() throws Exception {
        Assert.assertEquals(faithEffect, testTable.getFaithEffect());
    }

    @Test
    public void getDiceValue() throws Exception {
        final Map<DomesticColor, Integer> values = testTable.getDiceValue();

        Assert.assertEquals(4, values.size());
        values.forEach((type, value) ->
                Assert.assertTrue(type == DomesticColor.Neutral
                        || (value > 0 && value <= 6)));
    }

    @Test
    public void changeRound() throws Exception {
        final List<GameUser> currentOrder = new ArrayList<GameUser>() {
            {
                add(new GameUser(new FakeUser(new FakeLink()), FamilyColor.Blue));
                add(new GameUser(new FakeUser(new FakeLink()), FamilyColor.Red));
                add(testUser);
            }
        };

        Assert.assertEquals(FamilyColor.Blue, currentOrder.get(0).getFamilyColor());
        Assert.assertEquals(FamilyColor.Red, currentOrder.get(1).getFamilyColor());
        Assert.assertEquals(FamilyColor.Green, currentOrder.get(2).getFamilyColor());

        final PlayerState currentState = testUser.getUserState();
        currentState.setInUseDomestic(testUser.getDomestics().get(DomesticColor.Orange));
        testUser.updateUserState(currentState);

        testTable.occupy(testUser, 50, Collections.singletonList(new Cost(null)));

        final List<GameUser> nextOrder = testTable.changeRound(currentOrder);

        Assert.assertEquals(FamilyColor.Green, nextOrder.get(0).getFamilyColor());
        Assert.assertEquals(FamilyColor.Blue, nextOrder.get(1).getFamilyColor());
        Assert.assertEquals(FamilyColor.Red, nextOrder.get(2).getFamilyColor());

    }

    @Test
    public void occupy() throws Exception {

        final TowerPosition towerPos = (TowerPosition) testTable
                .occupy(testUser, 2, Collections.singletonList(new Cost(null)));

        Assert.assertEquals(2, towerPos.getNumber());
        Assert.assertEquals(PositionType.TerritoryTower, towerPos.getType());
    }

}