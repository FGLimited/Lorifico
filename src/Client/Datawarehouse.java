package Client;

/**
 * Created by andrea on 10/05/17.
 */

import Client.UI.PlayerStateObserver;
import Client.UI.TurnObserver;
import Game.UserObjects.FamilyColor;
import Game.UserObjects.GameUser;
import Game.UserObjects.PlayerState;
import Logging.Logger;
import Model.User.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Datawarehouse keeps data received from server.
 */
public class Datawarehouse {
    private static Datawarehouse datawarehouse;//singleton

    private User myUser;
    private String myUsername;
    private String whoseTurn;
    private Map<String, GameUser> gameUserMap = new HashMap<>();
    private Map<String, PlayerState> playerStateMap = new HashMap<>();
    private List<User> matchAttendees = new ArrayList<>();

    //ArrayList of playerstate observers
    private List<PlayerStateObserver> playerStateObserverList = new ArrayList();

    //ArrayList of turn observers
    private List<TurnObserver> turnObserverList = new ArrayList();

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

    /**
     * Registers a new playerState Observer
     *
     * @param playerStateObserver
     */
    public void registerPlayerStateObserver(PlayerStateObserver playerStateObserver) {
        if (!playerStateObserverList.contains(playerStateObserver)) {
            playerStateObserverList.add(playerStateObserver);
        }
    }

    /**
     * Registers a new turn observer.
     *
     * @param turnObserver
     */
    public void registerTurnObserver(TurnObserver turnObserver) {
        if (!turnObserverList.contains(turnObserver)) {
            turnObserverList.add(turnObserver);
        }
    }

    public User getMyUser() {
        return myUser;
    }

    /**
     * Sets current player's User object.
     *
     * @param user
     */
    public void setMyUser(User user) {
        this.myUser = user;
        this.myUsername = user.getUsername();
        Logger.log(Logger.LogLevel.Normal, "MyUser received: [" + user.getUsername() + ", ]");
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
     * Retrieves familyColor of specified user
     *
     * @param username user to retrieve familyc
     * @return familycolor
     */
    public FamilyColor getFamilyColor(String username) {
        return getGameUser(username).getFamilyColor();
    }

    /**
     * Retrieves playerState of specified myUser
     *
     * @param username
     * @return
     */
    public PlayerState getPlayerState(String username) {
        return playerStateMap.get(username);
    }

    /**
     * Updates playerState of specified username
     * @param username
     * @param playerState
     */
    public void setPlayerState(String username, PlayerState playerState) {
        if (playerStateMap.containsKey(username)) {
            playerStateMap.replace(username, playerState);
        } else {
            playerStateMap.put(username, playerState);
        }

        //notify observers
        playerStateObserverList.forEach((playerStateObserver -> playerStateObserver.onPlayerStateUpdate(playerState, username)));
    }

    /**
     * Retrieves gameUser of specified myUser
     *
     * @param username
     * @return
     */
    public GameUser getGameUser(String username) {
        return gameUserMap.get(username);
    }

    /**
     * Updates gameUser of specified username
     *
     * @param username
     * @param gameUser
     */
    public void setGameUser(String username, GameUser gameUser) {
        if (gameUserMap.containsKey(username)) {
            gameUserMap.replace(username, gameUser);
        } else {
            gameUserMap.put(username, gameUser);
        }
    }

    public String getWhoseTurn() {
        return whoseTurn;
    }

    /**
     * Notify objects which are observing turn changes.
     *
     * @param username
     */
    public void setWhoseTurn(String username) {
        this.whoseTurn = username;
        turnObserverList.forEach(turnObserver -> turnObserver.onTurnChange(username));
    }

    public List<User> getMatchAttendees() {
        return matchAttendees;
    }

    public void setMatchAttendees(List<User> matchAttendees) {
        this.matchAttendees = matchAttendees;
    }
}