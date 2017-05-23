package Server;

import Server.Game.Lobby;
import Logging.Logger;
import Model.UserManager;
import Server.Networking.ConnectionHandler;
import Server.Networking.LogInHandler;
import Server.Networking.RMI.RMIAcceptor;
import Server.Networking.SQL.DBContext;
import Server.Networking.Socket.SocketAcceptor;
import java.rmi.RemoteException;

/**
 * Created by andrea on 10/05/2017.
 */
public class Main {
    public static void main(String args[]) {

        final String mySqlConnString = "jdbc:mysql://flow3rhouse.duckdns.org/LorenzoDB?user=Lollo&password=Lorenzo@";

        // Create database instance for users
        DBContext db = new DBContext(mySqlConnString);

        // Create user manager instance
        UserManager userManager = new UserManager(db);

        // Initialize login handler
        LogInHandler loginHandler = new LogInHandler(new Lobby(), userManager);

        // Initialize connection handler with multiple connection providers
        ConnectionHandler connHandler = new ConnectionHandler();

        try {
            connHandler.addAcceptor(new SocketAcceptor(loginHandler, 8080));
            connHandler.addAcceptor(new RMIAcceptor(loginHandler));
        } catch (RemoteException re) {
            Logger.log(Logger.LogLevel.Error, "Can't initialize rmi acceptor.\n" + re.getMessage());
            return;
        }
        catch (IllegalArgumentException iae) {
            Logger.log(Logger.LogLevel.Error, "Can't initialize socket acceptor.\n" + iae.getMessage());
            return;
        }

        // Start connection handlers
        connHandler.startAll();

    }
}
