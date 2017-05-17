package Action;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by andrea on 10/05/17.
 */
public class LoginOrRegisterTest {
    private LoginOrRegister loginOrRegister;

    @Before
    public void before() {
        loginOrRegister = new LoginOrRegister("Andrea", "password", true);
    }

    @Test
    public void getUsername() throws Exception {
        assertEquals("Andrea", loginOrRegister.getUsername());
    }

    @Test
    public void getPasswordHash() throws Exception {
        assertEquals("5f4dcc3b5aa765d61d8327deb882cf99", loginOrRegister.getPasswordHash());
    }

    @Test
    public void isNewUser() throws Exception {
        assertEquals(true, loginOrRegister.isNewUser());
    }

    @Test
    public void doAction() throws Exception {

    }

}