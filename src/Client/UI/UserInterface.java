package Client.UI;

import Action.DisplayPopup;

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
     * @param level message priority
     * @param title box title
     * @param message box message
     */
    public void displayPopup(DisplayPopup.Level level, String title, String message);

    /**
     * Gets login page controller
     *
     * @return
     */
    public Login getLogin();

    /**
     * Gets ChooseAvatar page controller.
     *
     * @return
     */
    public ChooseAvatar getChooseAvatar();

    /**
     * Gets Lobby page controller
     *
     * @return
     */
    public Lobby getLobby();

    /**
     * Gets game UI controller
     * @return
     */
    public GameUI getGameUI();

    /**
     * Gets gametable in game object
     * @return
     */
    public GameTable getGameTable();

}
