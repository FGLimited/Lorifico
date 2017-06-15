package Client;

import Action.BaseAction;
import Action.UserSpecific;
import Client.Networking.CommFactory;
import Logging.Logger;
import Model.User.User;
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

        while (commLink == null) {
            commLink = (new CommFactory()).getLink(ip, port, commType);
        }

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
        Logger.log(Logger.LogLevel.Normal, message);
        BaseAction action = GsonUtils.fromGson(message);

        //If action is userSpecific we try to execute it in right context
        if (action instanceof UserSpecific) {
            UserSpecific userSpecificAction = (UserSpecific) action;

            //If we have gameUser of this username we'll create on on-the-fly user and execute action using it.
            if (Datawarehouse.getInstance().getGameUser(userSpecificAction.getUsername()) != null) {
                User user = new User(userSpecificAction.getUsername(), 0, 0, 0);
                action.doAction(user);
                return;
            }
        }

        //If action is not user specific we'll try to execute it using current user
        if (Datawarehouse.getInstance().getMyUser() != null) {
            action.doAction(Datawarehouse.getInstance().getMyUser());
            return;
        }

        //if current user is not specified
        action.doAction(null);
    }

    /**
     * Sends an Action to server
     *
     * @param baseAction action to send
     */
    public void sendMessage(BaseAction baseAction) {
        commLink.sendMessage(baseAction);
    }
}