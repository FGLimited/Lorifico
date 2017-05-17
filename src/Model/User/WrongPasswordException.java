package Model.User;

/**
 * Created by fiore on 08/05/2017.
 */
public class WrongPasswordException extends Exception {

    public WrongPasswordException(String message) {
        super(message);
    }
}
