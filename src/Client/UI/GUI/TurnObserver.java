package Client.UI.GUI;

/**
 * Created by andrea on 12/06/17.
 */
public interface TurnObserver {
    /**
     * Called when a new turn starts
     *
     * @param username user playing current turn.
     */
    void onTurnChange(String username);
}
