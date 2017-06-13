package Model;

import Logging.Logger;
import Model.User.*;
import Server.Networking.SQL.Database;
import org.jetbrains.annotations.Nullable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fiore on 10/05/2017.
 */
public class UserManager implements UserAuthenticator {

    private static volatile UserManager userManager = null;

    public static UserManager getInstance() {
        return userManager;
    }

    /**
     * Initialize global user manager with specified db instance
     *
     * @param dbContext Db context for current user manager
     */
    public static void init(Database dbContext) {
        userManager = new UserManager(dbContext);
    }

    private final Database database;

    private final List<String> authenticatedUsers = new ArrayList<>();

    /**
     * Initialize user manager on given database instance
     *
     * @param dbContext Database instance to use as user database
     */
    private UserManager(Database dbContext) {
        database = dbContext;
    }

    @Override
    public User createUser(String username, String passwordHash) throws UserAlreadyExistentException {

        // Check if user is present in the database
        if(isPresent(username))
            throw new UserAlreadyExistentException("User " + username + " already present in the database.");

        try {
            List<User> userSet = database.call("user_create",
                    new Object[] {username, passwordHash},
                    User.class);

            // If response set is empty return
            if(userSet == null || userSet.isEmpty())
                return null;

            return userSet.get(0);

        } catch (SQLException se) {
            Logger.log(Logger.LogLevel.Warning, "Error during user creation on the database.\n" + se.getMessage());

            return null;
        }
    }

    @Override
    public @Nullable User authenticateUser(String username, String passwordHash) throws UserNotFoundException, UserAlreadyLoggedException, WrongPasswordException {

        if(!isPresent(username))
            throw new UserNotFoundException("User " + username + " isn't present in the database.");

        if(authenticatedUsers.contains(username))
            throw new UserAlreadyLoggedException("User" + username + " has already logged on the server.");

        try {
            List<User> loggedUser = database.call("user_login", new Object[] { username, passwordHash }, User.class);

            // If response set is empty return
            if(loggedUser == null || loggedUser.isEmpty())
                throw new WrongPasswordException("Wrong password for user " + username + ".");

            // Add authenticated user to user list
            authenticatedUsers.add(loggedUser.get(0).getUsername());

            return loggedUser.get(0);

        } catch (SQLException se) {
            Logger.log(Logger.LogLevel.Warning, "Error while authenticating user " + username + ".\n" + se.getMessage());

            return null;
        }
    }

    @Override
    public boolean updateUser(User updateUser) throws UserNotFoundException {

        if(!isPresent(updateUser.getUsername()))
            throw new UserNotFoundException("User " + updateUser.getUsername() + " isn't present in the database.");

        try {
            List<User> updatedUser = database.call("user_update",
                    new Object[] { updateUser.getUsername(),
                            updateUser.getAvatar(),
                            updateUser.getWins(),
                            updateUser.getLosts(),
                            updateUser.getGameTime()},
                    User.class);

            // user_update procedure returns 1 if user has been updated, 0 if user not present
            if(updatedUser != null && !updatedUser.isEmpty())
                return updatedUser.size() == 1;

        } catch (SQLException se) {
            Logger.log(Logger.LogLevel.Warning, "Can't update user " + updateUser.getUsername() + " in the database.\n" + se.getMessage());
        }

        return false;
    }

    @Override
    public boolean deleteUser(String username) {

        try {
            database.call("user_delete", new Object[] { username }, true);

        } catch (SQLException se) {
            Logger.log(Logger.LogLevel.Warning, "Can't delete user " + username + " from the database.\n" + se.getMessage());
        }

        return !isPresent(username);
    }

    @Override
    public void disconnectUser(String username) {
        if(!authenticatedUsers.contains(username))
            return;

        authenticatedUsers.remove(username);
        Logger.log(Logger.LogLevel.Normal, "User " + username + " disconnected from server.");
    }

    /**
     * Check if requested user is already present in user database
     *
     * @param username Username to check
     * @return True if present
     */
    private boolean isPresent(String username) {

        try {

            List<User> users = database.call("user_present", new Object[] { username }, User.class);

            if(users == null || !users.isEmpty())
                return true;

        } catch (SQLException se) {
            Logger.log(Logger.LogLevel.Warning, "Can't check user presence in the database.\n" + se.getMessage());
        }

        return false;
    }
}
