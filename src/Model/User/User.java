package Model.User;

import Action.BaseAction;
import Networking.Gson.GsonUtils;
import Logging.Logger;
import Networking.CommLink;
import Server.Game.Match;
import Server.Game.UserObjects.GameUser;
import com.google.gson.JsonSyntaxException;

/**
 * Created by andrea on 10/05/2017.
 */
public class User {

    private final String Username;

    private volatile int WinCount;

    private volatile int LostCount;

    private volatile int GameTime;

    private volatile CommLink link;

    private volatile GameUser gameUser;

    private volatile Match match;

    public User(String username, int winCount, int lostCount, int gameTime, CommLink clientLink) {
        this.Username = username;
        this.WinCount = winCount;
        this.LostCount = lostCount;
        this.GameTime = gameTime;

        setCommLink(clientLink);
    }

    public User(String username, int winCount, int lostCount, int gameTime) {
        this(username, winCount, lostCount, gameTime, null);
    }

    private User() {
        Username = "";
        link = null;
    }

    /**
     * Executes stuff received from comm link
     *
     * @param message Message received
     */
    private void messageHandler(final String message) {
        try {

            BaseAction action = GsonUtils.fromGson(message);//Deserialized action
            action.doAction(this);

        } catch (JsonSyntaxException e) {
            Logger.log(Logger.LogLevel.Error, "Not a JSON message: " + message + "\n." + e.getMessage());

        }
    }

    public String getUsername() {
        return Username;
    }

    public int getWins() { return WinCount; }

    public int getLosts() { return LostCount; }

    public int getGameTime() { return GameTime; }

    public CommLink getLink() { return link; }

    public void incrementWins() { WinCount++; }

    public void incrementLosts() { LostCount++; }

    public void incrementGameTime(int toAdd) { GameTime += toAdd; }

    /**
     * Set comm link for this user and updates its callback
     *
     * @param newLink Comm link relative to this user
     */
    public void setCommLink(CommLink newLink) {
        link = newLink;

        // When a message is received on comm link the user relative callback is called
        link.setOnMessage((link, message) -> messageHandler(message));
    }

    /**
     * Set game user on match initialization
     *
     * @param newGameUser Initialized game user
     */
    public void setGameUser(GameUser newGameUser) {
        gameUser = newGameUser;
    }

    /**
     * Get associated game user
     *
     * @return Game user
     */
    public GameUser getGameUser() {
        return gameUser;
    }

    /**
     * Set game match when entering a new match
     *
     * @param newMatch New match instance
     */
    public void setMatch(Match newMatch) {
        match = newMatch;
    }

    /**
     * Get current game match
     *
     * @return Game match
     */
    public Match getMatch() {
        return match;
    }
}