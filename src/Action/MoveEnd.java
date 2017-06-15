package Action;

import Client.UI.GUI.GameUIController;
import Client.UI.GUI.resources.gameComponents.MyCameraGroup;
import Client.UI.GameUI;
import Client.UI.UserInterfaceFactory;
import Model.User.User;

/**
 * Created by fiore on 06/06/2017.
 */
public class MoveEnd implements BaseAction {

    private final boolean isTimeout;

    public MoveEnd(boolean isTimeout) {
        this.isTimeout = isTimeout;
    }

    @Override
    public void doAction(User user) {
        if (isTimeout) {
            UserInterfaceFactory.getInstance().displayPopup(DisplayPopup.Level.Warning, "Tempo scaduto", "Hai finito il tempo a disposizione!");
        }

        //If we are in GUI, reset camera view.
        GameUI gameUIController = UserInterfaceFactory.getInstance().getGameUI();
        if (gameUIController instanceof GameUIController) {
            ((GameUIController) gameUIController).getCameraGroup().setView(MyCameraGroup.CameraPosition.MAINVIEW);
        }
    }
}
