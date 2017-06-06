package Client.UI;

/**
 * Created by Io on 06/06/2017.
 */
public interface TowersController {
    /**
     * Shows passed card in passed gamePosition (Towers positions  are from 0 to 16)
     *
     * @param gamePosition
     * @param cardNumber
     */
    void showCardOnTowers(int gamePosition, int cardNumber);

    /**
     * Hides passed card from towers (animates it to down)
     *
     * @param cardNumber
     */
    void hideCardFromTower(int cardNumber);
}
