package Server.Game;

import Action.BaseAction;
import Model.User.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by fiore on 10/05/2017.
 */
public abstract class UserHandler {

    protected List<User> users = Collections.synchronizedList(new ArrayList<>());

    /**
     * Add a new user to this handler
     *
     * @param newUser New user to add
     */
    public abstract void addUser(User newUser);

    /**
     * Get all users in this handler
     *
     * @return List of users
     */
    public List<User> getAllUsers() {
        return users;
    }

    /**
     * Send specified action to all users in this handler
     *
     * @param message Message to spread
     */
    public void sendAll(final BaseAction message) {
        users.forEach(user -> user.getLink().sendMessage(message));
    }
}
