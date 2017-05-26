package Model;

import Model.User.*;
import org.jetbrains.annotations.Nullable;

/**
 * Created by fiore on 10/05/2017.
 */
public interface UserAuthenticator {

    /**
     * Create new user in the user database
     *
     * @param username New username
     * @param passwordHash Password hash (MD5)
     * @return New created user
     * @throws UserAlreadyExistentException If the username is already in user for another user
     */
    User createUser(String username, String passwordHash) throws UserAlreadyExistentException;

    /**
     * Authenticate an existent user
     *
     * @param username Username of the user
     * @param passwordHash Password hash (MD5)
     * @return User instance if authentication is successful
     * @throws UserNotFoundException If user isn't present in the database
     * @throws UserAlreadyLoggedException If user has already logged in
     * @throws WrongPasswordException If user is present but authentication wasn't successful
     */
    @Nullable User authenticateUser(String username, String passwordHash) throws UserNotFoundException, UserAlreadyLoggedException, WrongPasswordException;

    /**
     * Update given user in the database
     *
     * @param updatedUser User instance to be updated
     * @return True if update is successful
     * @throws UserNotFoundException If user isn't present in the database
     */
    boolean updateUser(User updatedUser) throws UserNotFoundException;

    /**
     * Delete given user from the database
     *
     * @param username Username of the user to delete
     * @return True if after the call the user isn't present in the database
     */
    boolean deleteUser(String username);

    /**
     * Disconnect specified user
     *
     * @param username Username for user to disconnect
     */
    void disconnectUser(String username);
}
