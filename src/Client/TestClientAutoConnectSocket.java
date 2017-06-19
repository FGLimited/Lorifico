package Client;

import Action.BaseAction;
import Action.LoginOrRegister;
import Client.Networking.CommFactory;
import Client.UI.UserInterface;
import Client.UI.UserInterfaceFactory;

import java.io.IOException;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by andrea on 10/05/2017.
 * <p>
 * This main is used for testing purpose only
 */
public class TestClientAutoConnectSocket {


    public static void main(String[] args) {
        UserInterface userInterface = UserInterfaceFactory.getInstance(UserInterfaceFactory.UserInterfaceType.JAVAFX);
        try {
            CommunicationManager.getInstance(CommFactory.LinkType.SOCKET, "127.0.0.1", 8080);
        } catch (IOException e) {
            e.printStackTrace();
        }

        final ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(2);
        executor.schedule(new Runnable() {
            @Override
            public void run() {
                BaseAction baseAction = new LoginOrRegister(args[0], "prova", false);
                CommunicationManager.getInstance().sendMessage(baseAction);
            }
        }, 500, TimeUnit.MILLISECONDS);

        userInterface.init(args);

    }
}