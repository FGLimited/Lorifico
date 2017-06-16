package Client.UI.CLI;

import Client.CommunicationManager;
import Client.Networking.CommFactory;
import Client.UI.CLI.cliUtils.CliSout;
import Client.UI.UserInterfaceFactory;
import com.budhash.cliche.Command;
import com.budhash.cliche.Param;
import com.budhash.cliche.Shell;

import java.io.IOException;

/**
 * Created by andrea on 17/05/17.
 */
public class ConnectionPage {
    private Shell shell;

    @Command(description = "Connessione al server")
    public void connect(
            @Param(name = "ip", description = "ip server")
                    String ip,
            @Param(name = "port", description = "porta")
                    String port,
            @Param(name = "type", description = "tipo di comunicazione")
                    String type) {

        type = type.toUpperCase();

        if (!type.equals(CommFactory.LinkType.RMI.toString()) && !type.equals(CommFactory.LinkType.SOCKET.toString())) {
            CliSout.log(CliSout.LogLevel.Errore, "Il tipo di connessione deve essere " + CommFactory.LinkType.RMI.toString() + " o " + CommFactory.LinkType.SOCKET.toString());
        }

        try {
            CommunicationManager.getInstance(CommFactory.LinkType.valueOf(type), ip, Integer.valueOf(port));
        } catch (IOException e) {
            e.getMessage();
        } catch (NumberFormatException e1) {
            CliSout.log(CliSout.LogLevel.Errore, "La porta deve essere un numero");
        }
        CliSout.log(CliSout.LogLevel.Informazione, "Connessione in corso");
        UserInterfaceFactory.getInstance().getLogin().showPage();
    }

    @Command(description = "Connessione al default server")
    public void connect(
            @Param(name = "type", description = "tipo di comunicazione")
                    String type) {

        type = type.toUpperCase();

        if (type.equals(CommFactory.LinkType.RMI.toString())) {
            connect("127.0.0.1", "1099", type);
            return;
        }

        if (type.equals(CommFactory.LinkType.SOCKET.toString())) {
            connect("127.0.0.1", "8080", type);
            return;
        }

        CliSout.log(CliSout.LogLevel.Errore, type + " non riconosciuto,\nIl tipo di connessione deve essere " + CommFactory.LinkType.RMI.toString() + " o " + CommFactory.LinkType.SOCKET.toString());
    }
}
