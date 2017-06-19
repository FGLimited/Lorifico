package Server;

import Server.Game.Lobby;
import Logging.Logger;
import Model.UserManager;
import Server.Networking.ConnectionHandler;
import Server.Networking.LogInHandler;
import Server.Networking.RMI.RMIAcceptor;
import Server.Networking.SQL.DBContext;
import Server.Networking.Socket.SocketAcceptor;
import java.io.IOException;
import java.rmi.RemoteException;
import java.sql.SQLException;

/**
 * Created by andrea on 10/05/2017.
 */
public class Main {
    public static void main(String args[]) throws IOException {

        Logger.setLogLevel(Logger.LogLevel.Normal);

        final String mySqlConnString = "jdbc:mysql://flow3rhouse.duckdns.org/LorenzoDB?user=Lollo&password=Lorenzo@";

        // Create database instance for users
        final DBContext db = new DBContext(mySqlConnString);
        try {
            db.connect();

        } catch (SQLException sqle) {
            Logger.log(Logger.LogLevel.Error, "Can't connect to the database.\n" + sqle.getMessage());

            return;
        }

        // Initialize user manager
        UserManager.init(db);

        // Initialize login handler
        LogInHandler loginHandler = new LogInHandler();

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

        // Set shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread(() -> Lobby.getInstance().dismissAll()));

        // Start connection handlers
        connHandler.startAll();

    }
}
