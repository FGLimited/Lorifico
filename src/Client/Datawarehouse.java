package Client;

/**
 * Created by andrea on 10/05/17.
 */

import Game.UserObjects.GameUser;
import Logging.Logger;
import Model.User.User;

import java.util.HashMap;
import java.util.Map;

/**
 * Datawarehouse keeps data received from server.
 */
public class Datawarehouse {
    private static Datawarehouse datawarehouse;//singleton

    private User myUser;
    private String myUsername;
    private Map<String, GameUser> gameUserMap = new HashMap<>();

    private Datawarehouse() {
    }

    /**
     * Signeton pattern
     *
     * @return the only instance of Datawarehouse
     */
    public static Datawarehouse getInstance() {
        if (datawarehouse == null) datawarehouse = new Datawarehouse();
        return datawarehouse;
    }

    public User getMyUser() {
        return myUser;
    }

    /**
     * Sets current myUser remote User object
     *
     * @param user
     */
    public void setMyUser(User user) {
        this.myUser = user;
        this.myUsername = user.getUsername();
        Logger.log(Logger.LogLevel.Normal, "MyUser received: [" + user.getUsername() + ", ]");
    }

    /**
     * Returns true if this is current myUser's username
     *
     * @param username
     * @return
     */
    public boolean isMe(String username) {
        return username.equals(myUsername);
    }

    /**
     * Retrieves my username
     *
     * @return
     */
    public String getMyUsername() {
        return myUsername;
    }

    /**
     * Retrieves myGameUser
     *
     * @return
     */
    public GameUser getMyGameUser() {
        return this.getGameUser(getMyUsername());
    }

    /**
     * Retrieves gameUser's of specified myUser
     *
     * @param username
     * @return
     */
    public GameUser getGameUser(String username) {
        return gameUserMap.get(username);
    }

    public void setGameUser(String username, GameUser gameUser) {
        if (gameUserMap.containsKey(username)) {
            gameUserMap.replace(username, gameUser);
        } else {
            gameUserMap.put(username, gameUser);
        }
    }
}
