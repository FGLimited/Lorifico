package Client;

/**
 * Created by andrea on 10/05/17.
 */

import Game.UserObjects.GameUser;
import Logging.Logger;
import Model.User.User;

/**
 * Datawarehouse keeps data received from server.
 */
public class Datawarehouse {
    private static Datawarehouse datawarehouse;//singleton

    private User user;
    private GameUser gameUser;

    //.......

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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        //observer pattern?
        this.user = user;
        Logger.log(Logger.LogLevel.Normal, "User received: [" + user.getUsername() + ", ]");
    }

    public GameUser getGameUser() {
        return gameUser;
    }

    public void setGameUser(GameUser gameUser) {
        //observer pattern?
        this.gameUser = gameUser;
    }
}
