package Action;

import Client.Datawarehouse;
import Client.UI.GUI.UserInterfaceImplemJFX;
import Client.UI.UserInterfaceFactory;
import Game.UserObjects.GameUser;
import Logging.Logger;
import Model.User.User;

/**
 * Created by fiore on 01/06/2017.
 */
public class GameUserUpdate extends UserSpecific implements BaseAction {

    private final GameUser updatedUser;

    public GameUserUpdate(String username, GameUser updatedUser) {
        super(username);
        this.updatedUser = updatedUser;
    }

    @Override
    public void doAction(User user) {
        Datawarehouse.getInstance().setGameUser(getUsername(), updatedUser);
        Logger.log(Logger.LogLevel.Normal, "Remote GameUser received for " + getUsername());

        //If user chose GUI we have to wait for it to show up
        if (UserInterfaceFactory.getInstance() instanceof UserInterfaceImplemJFX) {

            //synchronize on UI element and wait for random part to be loaded (faithRoad)
            synchronized (UserInterfaceFactory.getInstance().getGameUI()) {
                try {
                    Logger.log(Logger.LogLevel.Normal, "Waiting for GUI to show");

                    //If 3d was not loaded, start it.
                    if (UserInterfaceFactory.getInstance().getGameUI().getFaithController() == null)
                        UserInterfaceFactory.getInstance().getGameUI().showPage();//asks 3d gui to load

                    //Wait for user interface to complete loading
                    while (UserInterfaceFactory.getInstance().getGameUI().getFaithController() == null) {
                        UserInterfaceFactory.getInstance().getGameUI().wait();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        } else {
            //If we are on CLI... simply start it
            UserInterfaceFactory.getInstance().getGameUI().showPage();
        }
    }
}
