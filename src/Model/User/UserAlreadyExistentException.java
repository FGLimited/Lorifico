package Model.User;

/**
 * Created by fiore on 10/05/2017.
 */
public class UserAlreadyExistentException extends Exception {

    public UserAlreadyExistentException(String message) {
        super(message);
    }
}
