package Client.UI.GUI.resources.gameComponents;

import Client.Datawarehouse;
import Client.UI.TurnObserver;
import Game.UserObjects.Choosable;
import Server.Game.UserObjects.Domestic;
import javafx.application.Platform;
import javafx.scene.Node;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by andrea on 03/06/17.
 */
public class Tower extends Abstract3dsComponent implements TurnObserver {
    private TowerType towerType;
    private Map<Integer, GameCard> cardMap = new HashMap<>();//key is floor level (0 to 3)
    private Map<Domestic, Domestic3D> domesticMap = new HashMap<>();//key is server's domestic

    public Tower(TowerType towerType) {
        this.towerType = towerType;
        load3ds(towerType.getPath(), towerType.getxPos(), towerType.getyPos(), -1, 90, 90, 0, 1, 1, 1);

        //Add lables to each level
        for (TowerLabel.TowerLevel towerLevel : TowerLabel.TowerLevel.values()) {
            getChildren().add(new TowerLabel(this, towerLevel));
        }

        Datawarehouse.getInstance().registerTurnObserver(this);
    }

    /**
     * Adds specified card to this tower at indicated level starting from zero.
     *
     * @param number
     * @param floorLevel
     */
    public void showCard(int number, int floorLevel) {
        //Add card only if it's not already present.
        if (!getChildren().contains(GameCard.getCard(number))) {
            getChildren().add(GameCard.getCard(number));//Add card to map
            cardMap.put(floorLevel, GameCard.getCard(number));//Keep track of added cards
            GameCard.getCard(number).setOnMouseClicked(null);//Remove any callback
            GameCard.getCard(number).setHoverEnabled(false);
        }
        GameCard.getCard(number).setTowerLevelPosition(floorLevel);
    }

    public TowerType getTowerType() {
        return towerType;
    }

    /**
     * Removes passed card from this tower
     *
     * @param cardNumber card to remove
     */
    public void removeCard(int cardNumber) {
        //if we have this card, we'll remove it
        if (getChildren().contains(GameCard.getCard(cardNumber))) {

            GameCard.getCard(cardNumber).setOnMouseClicked(null);//Remove any callback

            //Animates card to floor and removes it from container
            GameCard.getCard(cardNumber).moveCardToFloorThenRemoveFromGroup(this);

            cardMap.values().remove(GameCard.getCard(cardNumber));
        }
    }

    /**
     * Removes every card from this tower
     */
    public void removeAllCards() {
        getChildren().forEach(node -> {
            if (node instanceof GameCard) {
                ((GameCard) (node)).moveCardToFloorThenRemoveFromGroup(this);
                ((GameCard) (node)).setOnMouseClicked(null);//Remove any callback
                cardMap.values().remove(node);
            }
        });
    }

    /**
     * Sets cost per tower position
     *
     * @param choosableList list of costs
     * @param towerLevel      tower level
     * @param positionNumber  real position number
     */
    public void setCostsPerPosition(List<Choosable> choosableList, int towerLevel, int positionNumber) {
        //If we have a cost for positionNumber it means that we can buy it.
        if (!choosableList.isEmpty()) {
            //Attach buy callback
            cardMap.get(towerLevel).setOnMouseClicked(event ->
                    new ChooseCardCostDialog(choosableList, positionNumber, cardMap.get(towerLevel).getCardNumber()));
            //Animate card
            cardMap.get(towerLevel).setHoverEnabled(true);
        } else {
            //if card is shown on towers (someone could have bought it)
            if (cardMap.get(towerLevel) != null) {
                //We can't buy this card
                cardMap.get(towerLevel).setHoverEnabled(false);
                //Remove callback
                cardMap.get(towerLevel).setOnMouseClicked(null);
            }
        }
    }

    /**
     * When turn changes we have to remove all callbacks from cards and disable hover
     *
     * @param username user playing current turn.
     */
    @Override
    public void onTurnChange(String username) {
        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(() -> onTurnChange(username));
            return;
        }

        getChildren().forEach(node -> {
            if (node instanceof GameCard) {
                ((GameCard) (node)).setHoverEnabled(false);
                ((GameCard) (node)).setOnMouseClicked(null);//Remove any callback
            }
        });
    }

    /**
     * Removes a domestic from tower
     *
     * @param domestic
     */
    public void removeDomestic(Domestic domestic) {
        if (domesticMap.containsKey(domestic)) {
            getChildren().remove(domesticMap.get(domestic));//Remove from tower
            domesticMap.remove(domestic);//Remove from map
        }
    }

    /**
     * Adds a domestic to tower
     *
     * @param domestic
     * @param towerLevel
     */
    public void addDomestic(Domestic domestic, int towerLevel) {
        Domestic3D domestic3D = new Domestic3D(domestic);
        domesticMap.put(domestic, domestic3D);//Link Domestic3D to server's domestic
        domestic3D.setPos(86.6, 35.6, -39.6 + (-104.4) * towerLevel);
        getChildren().add(domestic3D);//Add domestic to Tower
    }

    public void removeAllDomestics() {
        for (Iterator<Node> iterator = getChildren().iterator(); iterator.hasNext(); ) {
            Node node = iterator.next();
            if (node instanceof Domestic3D) {
                iterator.remove();
                domesticMap.values().remove(node);
            }
        }
    }

    /**
     * Enum used to store infos about towers
     */
    public enum TowerType {
        GREEN("green", 30, -21),
        YELLOW("yellow", 420.5, -21),
        BLUE("blue", 231.5, -21),
        PURPLE("purple", 626, -21);

        private static final String BASE_URL = "/Client/UI/GUI/resources/3D";
        private String path;
        private double xPos, yPos;


        TowerType(String path, double xPos, double yPos) {
            this.path = path;
            this.xPos = xPos;
            this.yPos = yPos;
        }

        private String getPath() {
            return BASE_URL + "/" + path + "Tower/" + path + "Tower.3ds";
        }

        public String getResourcePath() {
            return BASE_URL + "/" + path + "Tower";
        }

        public double getxPos() {
            return xPos;
        }

        public double getyPos() {
            return yPos;
        }


    }
}
