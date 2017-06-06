package Client.UI.GUI.resources.gameComponents;

import Logging.Logger;
import javafx.scene.Group;

/**
 * Created by andrea on 04/06/17.
 */
public class TowersBlock extends Group implements Client.UI.TowersController {
    private Tower[] towers = new Tower[4];

    /**
     * A Tower block is a container of 4 single towers
     */
    public TowersBlock() {
        //Load towers
        for (Tower.TowerType towerType : Tower.TowerType.values()) {
            towers[towerType.ordinal()] = new Tower(towerType);
            getChildren().add(towers[towerType.ordinal()]);
        }
    }

    /**
     * Shows passed card in passed gamePosition (Towers positions  are from 0 to 16)
     *
     * @param gamePosition
     * @param cardNumber
     */
    @Override
    public void showCardOnTowers(int gamePosition, int cardNumber) {
        if (gamePosition > 16) {
            Logger.log(Logger.LogLevel.Error, "Position " + gamePosition + " is not on towers");
            return;
        }
        int towerLevel = (gamePosition - 1) % 4;//Floor of the tower (0,1,2,3)
        int towerKey = (gamePosition - 1) / 4;//Number of the tower (0,1,2,3)
        towers[towerKey].showCard(cardNumber, towerLevel);
    }

    /**
     * Hides passed card from towers (animates it to down)
     *
     * @param cardNumber
     */
    @Override
    public void hideCardFromTower(int cardNumber) {

    }

}
