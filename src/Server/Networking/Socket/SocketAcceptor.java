package Server.Networking.Socket;

import Logging.Logger;
import Networking.Socket.SocketComm;
import Server.Networking.LinkAcceptor;
import Server.Networking.LinkHandler;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by fiore on 10/05/2017.
 */
public class SocketAcceptor implements LinkAcceptor {

    private final int port;

    private final LinkHandler handler;

    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    private volatile ServerSocket listener;

    private volatile boolean listen = false;

    /**
     * Initialize new socket connection acceptor on given port and with specified link handler
     *
     * @param linkHandler Link handler for new connections
     * @param port Port number for socket listening (0-65535)
     * @throws IllegalArgumentException If the port number isn't in range
     */
    public SocketAcceptor(LinkHandler linkHandler, int port) throws IllegalArgumentException {

        // Check port number
        if(port < 0 || port > 65535)
            throw new IllegalArgumentException("Port must be in range 0-65535.");

        this.port = port;
        handler = linkHandler;
    }

    @Override
    public void listen() {
        Logger.log(Logger.LogLevel.Normal, "Socket acceptor start request.");

        listen = true;

        try {
            listener = new ServerSocket(port);
        } catch (IOException ioe) {
            Logger.log(Logger.LogLevel.Error, "Can't start socket server.\n" + ioe.getMessage());
        }

        executor.execute(this::acceptor);
    }

    @Override
    public void stop() {
        Logger.log(Logger.LogLevel.Normal, "Socket acceptor shutdown.");

        listen = false;

        if(listener != null)
            try {
                listener.close();
            } catch (IOException ioe) {

            }

        executor.shutdownNow();
    }

    /**
     * Listening routine to listen for new connections and call linkHandler when a client request is received
     */
    private void acceptor() {
        Logger.log(Logger.LogLevel.Normal, "Socket acceptor started.");
        while (true) {

            try {
                Socket newClientSocket = listener.accept();

                handler.addClientComm(new SocketComm(newClientSocket));

            } catch (Exception e) {
                Logger.log(Logger.LogLevel.Warning, "Generic error in socket handler.\n" + e.getMessage());

                if(!listen)
                    return;
            }

        }
    }
}
