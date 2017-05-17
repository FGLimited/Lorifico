package Action;

import Client.UI.UserInterfaceFactory;
import Model.User.User;

public class DisplayPopup implements BaseAction {
    private Level level;
    private String message;

    public DisplayPopup(Level level, String message) {
        this.level = level;
        this.message = message;
    }

    @Override
    public void doAction(User user) {
        //TODO: add error levels
        UserInterfaceFactory.getInstance().displayPopup(message);
    }

    public enum Level {
        Normal,
        Warning,
        Error
    }
}
