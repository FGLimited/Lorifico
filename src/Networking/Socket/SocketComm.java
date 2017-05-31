package Networking.Socket;

import Action.BaseAction;
import Logging.Logger;
import Networking.CommLink;
import Networking.Gson.GsonUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BiConsumer;
import static Logging.Logger.LogLevel;

/**
 * Created by fiore on 10/05/2017.
 */
public class SocketComm implements CommLink {

    private final Socket socket;

    // Listener thread for incoming messages on socket
    private final ExecutorService postman = Executors.newSingleThreadExecutor();

    // Executor thread for message handling
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    private PrintWriter out;

    private BufferedReader in;

    // Message handling callback for every message received
    private volatile BiConsumer<CommLink, String> onMessage;

    private volatile boolean listen = false;

    public SocketComm(Socket socketLink) {

        socket = socketLink;

        // Initialize output and input stream with corresponding streams from socket
        try {
            out = new PrintWriter(socketLink.getOutputStream(), true);

            in = new BufferedReader(new InputStreamReader(socketLink.getInputStream()));

        } catch (IOException e) {
            Logger.log(Logger.LogLevel.Error, "Can't get output/input stream from socket\n" + e.getMessage());
        }

        // Start listening for incoming messages on socket out
        listen = true;
        postman.execute(this::inReader);
    }

    @Override
    public void setOnMessage(BiConsumer<CommLink, String> onMessageCallback) {
        onMessage = onMessageCallback;
    }

    private void inReader() {

        while (true) {

            try {
                final String message = in.readLine();

                // Every time a non-empty message is read call handler method if has been set
                if(message != null && !message.equals("") && onMessage != null)
                    executor.execute(() -> onMessage.accept(this, message));

            } catch (IOException ioe) {

                // If any exception is thrown check for current status and stop listening if necessary
                if(!listen)
                    return;
            }

        }
    }

    @Override
    public void sendMessage(final BaseAction message) {
        if(message == null)
            return;

        out.println(GsonUtils.toGson(message));
    }

    @Override
    public void shutdown() {
        listen = false;

        // Close socket connection
        try {
            socket.close();
        } catch (IOException ioe) {
            Logger.log(LogLevel.Warning, "Can't close socket properly.\n" + ioe.getMessage());
        }

        // Stop all executor threads for this link
        postman.shutdownNow();
        executor.shutdownNow();
    }
}
