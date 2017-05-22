package Client;

import Action.BaseAction;
import Client.Networking.CommFactory;
import Networking.CommLink;
import Networking.Gson.GsonUtils;

import java.io.IOException;

/**
 * Created by andrea on 10/05/2017.
 */
public class CommunicationManager {

    private static CommunicationManager communicationManager = null;

    private CommLink commLink;//Link with server

    //Initializes Communication with server
    private CommunicationManager(CommFactory.LinkType commType, String ip, int port) throws IOException {
        //Seems not needed at this time of project....
        //System.setProperty("java.security.policy","src/client.policy");
        //System.setSecurityManager(new SecurityManager());

        commLink = (new CommFactory()).getLink(ip, port, commType);
        if (commLink == null)
            throw new IOException("Impossibile connettersi al server");
        commLink.setOnMessage((link, message) -> handleMessageIn(message));

    }

    public static CommunicationManager getInstance() {
        return communicationManager;
    }

    public static CommunicationManager getInstance(CommFactory.LinkType commType, String ip, int port) throws IOException {
        if (communicationManager == null) communicationManager = new CommunicationManager(commType, ip, port);
        return communicationManager;
    }

    /**
     * Handles messages received from server
     *
     * @param message the serialized message
     */
    private void handleMessageIn(String message) {
        BaseAction action = GsonUtils.fromGson(message);
        if (Datawarehouse.getInstance() != null) {
            action.doAction(Datawarehouse.getInstance().getUser());
        } else {
            action.doAction(null);
        }
    }

    /**
     * Sends an Action to server
     *
     * @param baseAction action to send
     */
    public void sendMessage(BaseAction baseAction) {
        commLink.sendMessage(GsonUtils.toGson(baseAction));
    }
}
