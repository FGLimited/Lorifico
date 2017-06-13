package Action;

import Server.Game.UserObjects.GameUser;
import Model.User.User;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fiore on 13/06/2017.
 */
public class EndMatch implements BaseAction {

    private final String username;

    private final boolean isAbort;

    private final List<String> podium = new ArrayList<>();

    /**
     * Initialize end match message if a player leave the game before the end
     *
     * @param username Player who left the game
     */
    public EndMatch(String username) {
        this.username = username;
        isAbort = true;
    }

    /**
     * Initialize end match message at the end of the match
     *
     * @param podium Player's classification at the end of the match
     */
    public EndMatch(List<GameUser> podium) {
        username = "";
        isAbort = false;
        podium.forEach(user -> this.podium.add(user.toString()));
    }

    @Override
    public void doAction(User user) {
        // TODO: if isAbort
        //          notify that a user left the match before the end
        //       else
        //          show podium to the user

        // TODO: close all and shutdown connection
        // if you want to go into the lobby again reconnect
    }
}
