package Server.Game.UserObjects;

import Game.UserObjects.*;
import Model.FakeUser;
import Model.User.User;
import Networking.FakeLink;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by fiore on 18/05/2017.
 */
public class GameUserTest {

    private Game.UserObjects.GameUser testUser;

    @Before
    public void before() {

        testUser = new GameUser(new FakeUser(new FakeLink()), FamilyColor.Green);

        Map<DomesticColor, Integer> values = new HashMap<>();
        values.put(DomesticColor.Black, 5);
        values.put(DomesticColor.Orange, 3);
        values.put(DomesticColor.White, 2);
        values.put(DomesticColor.Neutral, 0);

        testUser.setDomestics(values);

        testUser.setRoundJump(true);
    }

    @Test
    public void selfCheck()  {

        Map<DomesticColor, Domestic> values = testUser.getDomestics();

        Assert.assertEquals(5, values.get(DomesticColor.Black).getValue().intValue());
        Assert.assertEquals(3, values.get(DomesticColor.Orange).getValue().intValue());
        Assert.assertEquals(2, values.get(DomesticColor.White).getValue().intValue());
        Assert.assertEquals(0, values.get(DomesticColor.Neutral).getValue().intValue());


        Assert.assertEquals(true, testUser.getRoundJump());
    }

    @Test
    public void userStateCheck() {

        Assert.assertNotSame(testUser.getUserState(), testUser.getUserState());

    }

}
