package Client.UI.GUI.resources.gameComponents;

/**
 * Created by andrea on 03/06/17.
 */
public class Tower extends Abstract3dsComponent {
    private TowerType towerType;

    public Tower(TowerType towerType) {
        this.towerType = towerType;
        load3ds(towerType.getPath(), towerType.getxPos(), towerType.getyPos(), -1, 90, 90, 0, 1, 1, 1);

        //Add lables to each level
        for (TowerLabel.TowerLevel towerLevel : TowerLabel.TowerLevel.values()) {
            getChildren().add(new TowerLabel(this, towerLevel));
        }
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
            getChildren().add(GameCard.getCard(number));
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
            //Animates card to floor and removes it from container
            GameCard.getCard(cardNumber).moveCardToFloorThenRemoveFromGroup(this);
        }
    }

    /**
     * Removes every card from this tower
     */
    public void removeAllCards() {
        getChildren().forEach(node -> {
            if (node instanceof GameCard)
                ((GameCard) (node)).moveCardToFloorThenRemoveFromGroup(this);
        });
    }

    /**
     * Enum used to store infos about towers
     */
    public enum TowerType {
        GREEN("green", 30, -21),
        BLUE("blue", 231.5, -21),
        YELLOW("yellow", 420.5, -21),
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
