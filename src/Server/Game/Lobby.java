package Server.Game;

import Model.User.User;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Created by fiore on 10/05/2017.
 */
public class Lobby implements UserHandler {

    private final List<User> users = Collections.synchronizedList(new ArrayList<>());

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
            Match newMatch = new Match(30000, 120000);
            newMatch.addUser(newUser);
            matches.add(newMatch);
        }
    }

    @Override
    public List<User> getAllUsers() {
        return users;
    }

    public List<Match> getAllMatches() {
        return matches;
    }
}
