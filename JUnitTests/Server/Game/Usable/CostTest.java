package Server.Game.Usable;

import Game.Usable.ResourceType;
import Game.UserObjects.PlayerState;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by fiore on 18/05/2017.
 */
public class CostTest {

    private Cost testCost;

    private Cost sumCost;

    private Cost totalCost;

    private PlayerState playerState;

    @Before
    public void before() {

        Map<ResourceType, Integer> resources = new HashMap<>();
        resources.put(ResourceType.Wood, 3);
        resources.put(ResourceType.Rock, 2);

        sumCost = new Cost(resources);

        resources.put(ResourceType.Gold, 4);
        resources.put(ResourceType.MilitaryPoint, 3);

        testCost = new Cost(resources, 5);

        totalCost = testCost.sum(sumCost, true);

        resources.replace(ResourceType.MilitaryPoint, 5);

        playerState = new Server.Game.UserObjects.PlayerState(null);
        playerState.setResources(resources, false);
    }

    @Test
    public void selfCheck() throws Exception {

        Map<ResourceType, Integer> resources = testCost.getResources();

        Assert.assertEquals(3, resources.get(ResourceType.Wood).intValue());
        Assert.assertEquals(2, resources.get(ResourceType.Rock).intValue());
        Assert.assertEquals(4, resources.get(ResourceType.Gold).intValue());
        Assert.assertEquals(3, resources.get(ResourceType.MilitaryPoint).intValue());
        Assert.assertEquals(5, testCost.getRequestedPoints());
    }

    @Test
    public void canBuy() throws Exception {

        Assert.assertTrue(testCost.canBuy(playerState));

        Assert.assertFalse(totalCost.canBuy(playerState));

    }

    @Test
    public void apply() throws Exception {

        final PlayerState state = playerState.clone();

        testCost.apply(state);

        Map<ResourceType, Integer> leftResources = state.getResources();

        Assert.assertEquals(0, leftResources.get(ResourceType.Wood).intValue());
        Assert.assertEquals(0, leftResources.get(ResourceType.Rock).intValue());
        Assert.assertEquals(0, leftResources.get(ResourceType.Gold).intValue());
        Assert.assertEquals(2, leftResources.get(ResourceType.MilitaryPoint).intValue());

    }

    @Test
    public void sum() throws Exception {

        Map<ResourceType, Integer> total = totalCost.getResources();

        Assert.assertEquals(6, total.get(ResourceType.Wood).intValue());
        Assert.assertEquals(4, total.get(ResourceType.Rock).intValue());
        Assert.assertEquals(4, total.get(ResourceType.Gold).intValue());
        Assert.assertEquals(3, total.get(ResourceType.MilitaryPoint).intValue());
        Assert.assertEquals(5, totalCost.getRequestedPoints());

    }

}
