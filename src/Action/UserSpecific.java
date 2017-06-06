package Action;

/**
 * Created by fiore on 01/06/2017.
 */
public abstract class UserSpecific {

    private final String username;

    protected UserSpecific(String username) {
        this.username = username;
    }

    /**
     * Get username of user to pass to BaseAction::doAction method
     *
     * @return Requested user's username
     */
    public String getUsername() {
        return username;
    }

}
