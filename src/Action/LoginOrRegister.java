package Action;

import Model.User.User;
import org.jetbrains.annotations.Nullable;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by andrea on 10/05/2017.
 */
public class LoginOrRegister implements BaseAction {
    private String username;
    private String passwordHash;
    private boolean isNewUser = false;

    /**
     * Used to login or register a new user on server
     *
     * @param username
     * @param password
     * @param isNewUser
     */
    public LoginOrRegister(String username, String password, boolean isNewUser) {
        this.username = username;

        this.passwordHash = getMD5(password);

        this.isNewUser = isNewUser;
    }

    private @Nullable String getMD5(String password) {

        byte[] bytes;

        try {
            bytes = MessageDigest.getInstance("MD5").digest(password.getBytes());
        } catch (NoSuchAlgorithmException nsae) {

            return null;
        }

        StringBuilder hexString = new StringBuilder();

        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if(hex.length() == 1)
                hexString.append('0');

            hexString.append(hex);
        }

        return hexString.toString();
    }

    public String getUsername() {
        return username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public boolean isNewUser() {
        return isNewUser;
    }

    @Override
    public void doAction(User user) {
        //Registration Login should have happened here....... but u wanted to use LoginHandler
    }
}