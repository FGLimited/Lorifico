package Client;

import Client.UI.UserInterface;
import Client.UI.UserInterfaceFactory;
import com.budhash.cliche.Command;
import com.budhash.cliche.Shell;
import com.budhash.cliche.ShellDependent;
import com.budhash.cliche.ShellFactory;

import java.io.IOException;
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
        }, 1, TimeUnit.SECONDS);


        new Thread(() -> {
            try {
                ShellFactory.createConsoleShell("Debuggenzo", "?list per conoscere i comandi", new DebugLorenzo())
                        .commandLoop();
            } catch (IOException e) {
                System.err.println("Unable to start CLI Interface");
                e.printStackTrace();
            }
        }).start();

        userInterface.init(args);

    }

    public static class DebugLorenzo implements ShellDependent {
        private Shell shell;

        private UserInterface userInterface = UserInterfaceFactory.getInstance();

        @Command
        public String showGUI() {
            userInterface.getGameUI().showPage();
            return "OK";
        }

        public void cliSetShell(Shell theShell) {
            this.shell = theShell;
        }

        public Shell getShell() {
            return shell;
        }

    }
}