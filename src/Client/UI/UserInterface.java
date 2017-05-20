package Client.UI;

/**
 * Created by Io on 10/05/2017.
 */
public interface UserInterface {
    /**
     * Used to start gui/cli
     */
    public void init(String args[]);

    /**
     * Displays a popup in GUI or a text message in CLI
     *
     * @param message
     */
    public void displayPopup(String message);

    /**
     * Gets login page controller
     *
     * @return
     */
    public Login getLogin();

    /**
     * Gets dashboard controller
     * @return
     */
    public Dashboard getDashboard();

    /**
     * Gets gametable controller
     * @return
     */
    public GameTable getGameTable();

}
