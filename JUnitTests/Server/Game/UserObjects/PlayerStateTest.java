package Server.Game.UserObjects;

import Game.Cards.CardType;
import Game.Positions.PositionType;
import Game.Usable.ResourceType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by fiore on 18/05/2017.
 */
public class PlayerStateTest {

    private PlayerState playerState;

    @Before
    public void before() {

        playerState = new Server.Game.UserObjects.PlayerState(null);

        Map<ResourceType, Integer> resources = new HashMap<>();
        resources.put(ResourceType.Wood, 3);
        resources.put(ResourceType.Rock, 4);
        resources.put(ResourceType.Slave, 5);

        playerState.setPenalty(ResourceType.Slave, 2);

        playerState.setResources(resources, true);

        playerState.setCostBonus(CardType.Personality, ResourceType.Gold, 2);

        playerState.setCheckingPositionType(PositionType.TerritoryTower);

    }

    @Test
    public void setResources() {

        Map<ResourceType, Integer> resources = playerState.getResources();

        Assert.assertEquals(3, resources.get(ResourceType.Wood).intValue());
        Assert.assertEquals(4, resources.get(ResourceType.Rock).intValue());
        Assert.assertEquals(3, resources.get(ResourceType.Slave).intValue());
        Assert.assertEquals(0, resources.get(ResourceType.Gold).intValue());

    }

    @Test
    public void getBonus() {
        Assert.assertEquals(2, playerState.getCostBonus(CardType.Personality).get(ResourceType.Gold).intValue());
        Assert.assertEquals(0, playerState.getCostBonus(CardType.Personality).get(ResourceType.Wood).intValue());
        Assert.assertEquals(0, playerState.getCostBonus(CardType.Territory).get(ResourceType.Gold).intValue());
    }

    @Test
    public void checkingPosition() {
        Assert.assertEquals(PositionType.TerritoryTower, playerState.getCheckingPositionType());
    }
}
