package Client.UI.CLI;

import Client.CommunicationManager;
import Client.Networking.CommFactory;
import Client.UI.UserInterfaceFactory;
import com.budhash.cliche.Command;
import com.budhash.cliche.Param;
import com.budhash.cliche.Shell;
import com.budhash.cliche.ShellDependent;

/**
 * Created by andrea on 17/05/17.
 */
public class ConnectionPage implements ShellDependent {


    @Command(description = "Connessione al server")
    public String connect(
            @Param(name = "ip", description = "ip server")
                    String ip,
            @Param(name = "port", description = "porta")
                    String port,
            @Param(name = "type", description = "tipo di comunicazione")
                    String type) {

        type = type.toUpperCase();

        if (CommunicationManager.getInstance() != null) {
            return "Esiste gia' una connessione verso il server";
        }

        if (!type.equals(CommFactory.LinkType.RMI.toString()) && !type.equals(CommFactory.LinkType.SOCKET.toString())) {
            return "Il tipo di connessione deve essere " + CommFactory.LinkType.RMI.toString() + " o " + CommFactory.LinkType.SOCKET.toString();
        }

        if (CommunicationManager.getInstance(CommFactory.LinkType.valueOf(type), ip, Integer.valueOf(port)) != null) {
            UserInterfaceFactory.getInstance().getLogin().showLoginPage();
        }
        return "Riprova.";
    }

    @Command(description = "Connessione al default server")
    public String connect(
            @Param(name = "type", description = "tipo di comunicazione")
                    String type) {

        type = type.toUpperCase();

        if (type.equals(CommFactory.LinkType.RMI.toString()))
            return connect("127.0.0.1", "1099", type);

        if (type.equals(CommFactory.LinkType.SOCKET.toString()))
            return connect("127.0.0.1", "8080", type);

        return type + " non riconosciuto,\nIl tipo di connessione deve essere " + CommFactory.LinkType.RMI.toString() + " o " + CommFactory.LinkType.SOCKET.toString();
    }

    /**
     * Internally used by cliche to keep references
     *
     * @param theShell
     */
    @Override
    public void cliSetShell(Shell theShell) {
        ((UserInterfaceImplemCLI) UserInterfaceFactory.getInstance()).cliSetShell(theShell);
    }
}
