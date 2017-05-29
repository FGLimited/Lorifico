package Action;

import Client.UI.UserInterfaceFactory;
import Model.User.User;

public class DisplayPopup implements BaseAction {
    private Level level;
    private String message;
    private String title;

    /**
     * Used to display popup on GUI
     *
     * @param level
     * @param message message to display
     */
    public DisplayPopup(Level level, String message) {
        this(level, "", message);
    }

    public DisplayPopup(Level level, String title, String message) {
        this.level = level;
        this.message = message;
        this.title = title;
    }

    @Override
    public void doAction(User user) {
        UserInterfaceFactory.getInstance().displayPopup(level, title, message);
    }

    public enum Level {
        Normal,
        Warning,
        Error
    }
}
