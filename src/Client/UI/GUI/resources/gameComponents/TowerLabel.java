package Client.UI.GUI.resources.gameComponents;

import javafx.scene.effect.ColorAdjust;

/**
 * Created by andrea on 03/06/17.
 */
public class TowerLabel extends AbstractImageComponent {

    public TowerLabel(Tower tower, TowerLevel towerLevel) {
        super.loadImage(towerLevel.getLevelImagePath(tower), towerLevel.getxPos(), towerLevel.getyPos(), towerLevel.getzPos(), 0.1, 90, 0, 0);

        applyEffects();
    }

    private void applyEffects() {
        getImageView().setOnMouseEntered((event) -> {
            ColorAdjust colorAdjust = new ColorAdjust();
            colorAdjust.setContrast(0.1);
            colorAdjust.setHue(-0.05);
            colorAdjust.setBrightness(0.3);
            colorAdjust.setSaturation(0.3);
            getImageView().setEffect(colorAdjust);
        });

        getImageView().setOnMouseExited((event -> getImageView().setEffect(null)));
    }

    /**
     * Struct used to keep data about position of tower's labels
     */
    public enum TowerLevel {
        //aumentando valori: x verso destra, y verso player, z verso giu
        LEVEL0(0, -107, 69.5, -295),
        LEVEL1(1, -107, 69.5, -398),
        LEVEL2(2, -107, 69.5, -502),
        LEVEL3(3, -107, 69.5, -607);

        private int level;
        private double xPos, yPos, zPos;


        TowerLevel(int level, double xPos, double yPos, double zPos) {
            this.level = level;
            this.xPos = xPos;
            this.yPos = yPos;
            this.zPos = zPos;
        }

        public double getxPos() {
            return xPos;
        }

        public double getyPos() {
            return yPos;
        }

        public double getzPos() {
            return zPos;
        }

        private String getLevelImagePath(Tower tower) {
            return tower.getTowerType().getResourcePath() + "/floor_" + level + ".png";
        }


    }
}
