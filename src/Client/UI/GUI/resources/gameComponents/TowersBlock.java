package Client.UI.GUI.resources.gameComponents;

import Client.Datawarehouse;
import Client.UI.PlayerStateObserver;
import Game.Cards.CardType;
import Game.UserObjects.Choosable;
import Game.UserObjects.PlayerState;
import Logging.Logger;
import javafx.application.Platform;
import javafx.scene.Group;

import java.util.List;
import java.util.Map;

/**
 * Created by andrea on 04/06/17.
 */
public class TowersBlock extends Group implements Client.UI.TowersController, PlayerStateObserver {
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

        Datawarehouse.getInstance().registerPlayerStateObserver(this);
    }

    @Override
    public void setCostsPerPosition(Map<Integer, List<Choosable>> choosablePerPos) {
        //Send each choosable to respective tower
        choosablePerPos.forEach((gamePosition, choosables) -> {
            //Towers are positions from 1 to 16
            if (gamePosition <= 16) {
                //identify tower
                int towerLevel = (gamePosition - 1) % 4;//Floor of the tower (0,1,2,3)
                int towerKey = (gamePosition - 1) / 4;//Number of the tower (0,1,2,3)

                List<Choosable> choosableList = choosablePerPos.get(gamePosition);

                //Send tower its cost per specified position
                towers[towerKey].setCostsPerPosition(choosableList, towerLevel, gamePosition);
            }
        });
    }

    /**
     * Shows passed card in passed gamePosition (Towers positions  are from 0 to 16)
     *
     * @param gamePosition
     * @param cardNumber
     */
    @Override
    public void showCardOnTowers(int gamePosition, int cardNumber) {
        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(() -> showCardOnTowers(gamePosition, cardNumber));
            return;
        }

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
    public void removeCardFromTower(int cardNumber) {
        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(() -> removeCardFromTower(cardNumber));
            return;
        }

        //Say each tower to remove specified card, if they don't have it they'll just do nothing. DIVIDE ET IMPERA!
        for (Tower.TowerType towerType : Tower.TowerType.values()) {
            towers[towerType.ordinal()].removeCard(cardNumber);
        }
    }

    /**
     * Removes all cards from every tower.
     */
    @Override
    public void removeAllCardsFromTowers() {
        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(() -> removeAllCardsFromTowers());
            return;
        }

        //Say each tower to remove every card they have. DIVIDE ET IMPERA!
        for (Tower.TowerType towerType : Tower.TowerType.values()) {
            towers[towerType.ordinal()].removeAllCards();
        }
    }

    /**
     * If a card which is on towers is seen in a user's punchboard, we have to remove it
     *
     * @param playerState
     * @param username
     */
    @Override
    public void onPlayerStateUpdate(PlayerState playerState, String username) {
        //Remove every card which is in user's punchboard from towers
        playerState.getCards(CardType.Building).forEach(card -> removeCardFromTower(card.getNumber()));
        playerState.getCards(CardType.Challenge).forEach(card -> removeCardFromTower(card.getNumber()));
        playerState.getCards(CardType.Personality).forEach(card -> removeCardFromTower(card.getNumber()));
        playerState.getCards(CardType.Territory).forEach(card -> removeCardFromTower(card.getNumber()));
    }
}