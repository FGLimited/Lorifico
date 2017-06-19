package Action;

import Client.UI.UserInterfaceFactory;
import Model.User.User;
import Server.Game.UserObjects.GameUser;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
        if (isAbort) {
            UserInterfaceFactory.getInstance().displayPopup(DisplayPopup.Level.Normal, "Partita Terminata", "Un utente si e' disconnesso...");
        } else {
            String classifica = podium.stream().collect(Collectors.joining(", "));
            UserInterfaceFactory.getInstance().displayPopup(DisplayPopup.Level.Normal, "Partita Terminata",
                    "La classifica e'\n" + classifica);
        }
    }
}
