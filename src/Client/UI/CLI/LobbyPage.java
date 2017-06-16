package Client.UI.CLI;

import Action.BaseAction;
import Action.StartMatch;
import Client.CommunicationManager;
import Client.UI.CLI.cliUtils.CliSout;
import Client.UI.UserInterfaceFactory;
import Model.User.User;
import com.budhash.cliche.Command;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by andrea on 16/06/17.
 */
public class LobbyPage implements Client.UI.Lobby {
    @Override
    public void showPage() {
        ((UserInterfaceImplemCLI) (UserInterfaceFactory.getInstance())).setCliPage(this, false);
    }

    @Override
    public void setMatchAttendees(List<User> userList) {
        String connectedUsers = userList.stream().map(user -> user.getUsername()).collect(Collectors.joining(", "));
        CliSout.log(CliSout.LogLevel.Informazione, "Giocatori in attesa:\n" + connectedUsers);
    }

    @Override
    public void restartTimer() {
        CliSout.log(CliSout.LogLevel.Avvertimento, "La partita iniziera' automaticamente tra 30 secondi");
        CliSout.log(CliSout.LogLevel.Avvertimento, "Per iniziare a giocare subito scrivi 'start'");
    }

    @Command(description = "Inizia la partita")
    public void start() {
        BaseAction action = new StartMatch();
        CommunicationManager.getInstance().sendMessage(action);
    }
}
