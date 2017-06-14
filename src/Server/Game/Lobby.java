package Server.Game;

import Model.User.User;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Created by fiore on 10/05/2017.
 */
public class Lobby extends UserHandler {

    private static Lobby instance = new Lobby();

    public static Lobby getInstance() {
        return instance;
    }

    private Lobby() {

    }

    private final List<Match> matches = Collections.synchronizedList(new ArrayList<>());

    @Override
    public synchronized void addUser(User newUser) {

        // Add new user to global list
        users.add(newUser);

        // Find the first match where a user can be added or create a new one
        Optional<Match> firstFreeMatch = matches.parallelStream().filter(match -> !match.isStarted()).findFirst();

        // If match is present add user else create a new match, add user and add match to matches' list
        if(firstFreeMatch.isPresent())
            firstFreeMatch.get().addUser(newUser);
        else {
            Match newMatch = new Match(30000, 480000);
            newMatch.addUser(newUser);
            matches.add(newMatch);
        }
    }

    /**
     * Remove specified match from list
     *
     * @param toClear Match to remove
     */
    public void clearMatch(Match toClear) {
        matches.remove(toClear);
    }

    /**
     * Abort all matches
     */
    public void dismissAll() {

        // Create fake user
        final User server = new User("Server shutdown", 0, 0, 0);

        // Abort all matches
        matches.parallelStream().forEach(match -> match.abort(server));

        matches.clear();
    }

}
