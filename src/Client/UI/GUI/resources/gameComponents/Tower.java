package Client.UI.GUI.resources.gameComponents;

/**
 * Created by andrea on 03/06/17.
 */
public class Tower extends Abstract3dsComponent {
    public static Tower last;
    private TowerType towerType;

    public Tower(TowerType towerType) {
        this.towerType = towerType;
        load3ds(towerType.getPath(), towerType.getxPos(), towerType.getyPos(), -1, 90, 90, 0, 1, 1, 1);

        getChildren().add(new TowerLabel(this, TowerLabel.TowerLevel.LEVEL0));
        getChildren().add(new TowerLabel(this, TowerLabel.TowerLevel.LEVEL1));
        getChildren().add(new TowerLabel(this, TowerLabel.TowerLevel.LEVEL2));
        getChildren().add(new TowerLabel(this, TowerLabel.TowerLevel.LEVEL3));

        last = this;//debug purpose
    }

    public TowerType getTowerType() {
        return towerType;
    }

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
