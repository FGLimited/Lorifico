package Server.Game.UserObjects;

import Game.UserObjects.*;
import Model.FakeUser;
import Networking.CommLink;
import Networking.FakeLink;
import Server.Game.GameHelper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by fiore on 18/05/2017.
 */
public class GameUserTest {

    private GameUser testUser;

    private final CommLink link = new FakeLink();

    @Before
    public void before() {

        testUser = new GameUser(new FakeUser(link), FamilyColor.Green);

        final PlayerState userState = GameHelper.getInstance().getInitialPS(testUser, 0);
        testUser.updateUserState(userState);

        final Map<DomesticColor, Integer> values = new HashMap<DomesticColor, Integer>() {
            {
                put(DomesticColor.Black, 5);
                put(DomesticColor.Orange, 3);
                put(DomesticColor.White, 2);
                put(DomesticColor.Neutral, 57);
            }
        };

        testUser.setDomesticPenalty(DomesticColor.Black, 1);

        testUser.setDomestics(values);

        testUser.setRoundJump(true);

        testUser.setHasMoved(false);
        testUser.setHasMoved(false);

        testUser.setChurchSupport(true);
    }

    @Test
    public void getUserLink() {
        Assert.assertEquals(link, testUser.getUserLink());
    }

    @Test
    public void getDomestics()  {

        final Map<DomesticColor, Domestic> values = testUser.getDomestics();

        Assert.assertEquals(4, values.get(DomesticColor.Black).getValue().intValue());
        Assert.assertEquals(3, values.get(DomesticColor.Orange).getValue().intValue());
        Assert.assertEquals(2, values.get(DomesticColor.White).getValue().intValue());
        Assert.assertEquals(0, values.get(DomesticColor.Neutral).getValue().intValue());
    }

    @Test
    public void getRoundJump() {
        Assert.assertTrue(testUser.getRoundJump());
    }

    @Test
    public void getUserState() {
        Assert.assertNotSame(testUser.getUserState(), testUser.getUserState());
    }

    @Test
    public void getHasMoved() {
        Assert.assertFalse(testUser.getHasMoved());

        testUser.setHasMoved(true);

        Assert.assertFalse(testUser.getHasMoved());

        testUser.setHasMoved(true);

        Assert.assertTrue(testUser.getHasMoved());
    }

    @Test
    public void getChurchSupport() {
        Assert.assertTrue(testUser.getChurchSupport());
    }
}
