package Server.Game;

import Model.User.User;
import java.util.List;

/**
 * Created by fiore on 10/05/2017.
 */
public interface UserHandler {

    /**
     * Add a new user to this handler
     *
     * @param newUser New user to add
     */
    void addUser(User newUser);

    /**
     * Get all users in this handler
     *
     * @return List of users
     */
    List<User> getAllUsers();
}
