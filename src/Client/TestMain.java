package Client;

import Client.UI.GUI.GameUIController;
import Client.UI.GUI.resources.gameComponents.MyCameraGroup;
import Client.UI.UserInterface;
import Client.UI.UserInterfaceFactory;
import Game.UserObjects.FamilyColor;
import com.budhash.cliche.Command;
import com.budhash.cliche.Shell;
import com.budhash.cliche.ShellDependent;
import com.budhash.cliche.ShellFactory;

import java.io.IOException;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by andrea on 10/05/2017.
 *
 * This main is used for testing purpose only
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
        public String showTower() {
            GameUIController gameUIController = (GameUIController) UserInterfaceFactory.getInstance().getGameUI();
            gameUIController.getCameraGroup().setView(MyCameraGroup.CameraPosition.TOWERS);
            return "OK";
        }

        @Command
        public String showGameTable() {
            GameUIController gameUIController = (GameUIController) UserInterfaceFactory.getInstance().getGameUI();
            gameUIController.getCameraGroup().setView(MyCameraGroup.CameraPosition.GAMETABLE);
            return "OK";
        }

        @Command
        public String setDice(int one, int two, int three) {
            userInterface.getGameUI().getDiceController().setNumbers(one, two, three);
            return "OK";
        }

        @Command
        public String setFaithPosition(String familyColor, int value) {
            String laGenteCheProgrammaInCSharpSiMeritaDiStareMaleAvreiPotutoUsareUnUpperCaseInveceNo =
                    familyColor.substring(0, 1).toUpperCase() + familyColor.substring(1);
            FamilyColor enumFamilyColor = FamilyColor.valueOf(laGenteCheProgrammaInCSharpSiMeritaDiStareMaleAvreiPotutoUsareUnUpperCaseInveceNo);

            userInterface.getGameUI().getFaithController().moveToPosition(enumFamilyColor, value);
            return "OK";
        }

        @Command
        public String setFaithCards(int first, int second, int third) {
            userInterface.getGameUI().getFaithController().showFaithCards(first, second, third);
            return "OK";
        }

        @Command
        public String showCard(int position, int cardid) {
            userInterface.getGameUI().getTowersController().showCardOnTowers(position, cardid);
            return "OK";
        }

        @Command
        public String removeAllCards() {
            userInterface.getGameUI().getTowersController().removeAllCardsFromTowers();
            return "OK";
        }

        @Command
        public String removeCard(int cardNumber) {
            userInterface.getGameUI().getTowersController().removeCardFromTower(cardNumber);
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