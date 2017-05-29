package Client;

import Client.UI.UserInterface;
import Client.UI.UserInterfaceFactory;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by andrea on 10/05/2017.
 */
public class TestMain {
    public static void main(String[] args) {
        UserInterface userInterface = UserInterfaceFactory.getInstance(UserInterfaceFactory.UserInterfaceType.JAVAFX);
        final ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(2);
        executor.schedule(new Runnable() {
            @Override
            public void run() {
                userInterface.getGameUI().showPage();
            }
        }, 2, TimeUnit.SECONDS);

        userInterface.init(args);

    }
}