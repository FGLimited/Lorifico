package Server.Game;

import Model.User.User;
import Networking.FakeLink;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import java.util.Arrays;
import java.util.Collection;

/**
 * Created by fiore on 18/05/2017.
 */
@RunWith(value = Parameterized.class)
public class UserHandlerTest {

    private UserHandler userHandler;

    private User testUser = new User("TestUser", 0, 0, 0, new FakeLink());

    public UserHandlerTest(UserHandler handler) {
        userHandler = handler;
    }

    @Before
    public void before() {
        userHandler.addUser(testUser);
    }

    @Test
    public void addUser() throws Exception {
        Assert.assertSame(testUser, userHandler.getAllUsers().get(0));
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(
                new Object[] { new Lobby() },
                new Object[] { new Match(30, 30000) }
        );
    }

}
