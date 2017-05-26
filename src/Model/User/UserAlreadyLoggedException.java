package Model.User;

/**
 * Created by fiore on 26/05/2017.
 */
public class UserAlreadyLoggedException extends Exception {

    public UserAlreadyLoggedException(String message) {
        super(message);
    }
}
