package Client.UI.GUI.resources.gameComponents;

import javafx.animation.*;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

import java.util.Random;

/**
 * Created by andrea on 01/06/17.
 */
public class Dice extends Abstract3dsComponent {
    public static Dice last;
    Timeline timeline;
    Rotate rotateX;
    Rotate rotateY;
    Rotate rotateZ;
    private DiceNumber[] diceNumbers = new DiceNumber[7];


    public Dice(DiceType diceType) {
        load3ds(diceType.getPath(), diceType.getxPos(), diceType.getyPos(), -20, 270, 0, -90, 0.5, 0.5, 0.5);

        //Add rotations used to change dice number:
        rotateX = new Rotate(0, Rotate.X_AXIS);
        rotateY = new Rotate(0, Rotate.Y_AXIS);
        rotateZ = new Rotate(0, Rotate.Z_AXIS);

        getTransforms().addAll(rotateX, rotateY, rotateZ);

        //Array containing available positions
        diceNumbers[0] = null;
        diceNumbers[1] = new DiceNumber(0, -90, 0);
        diceNumbers[2] = new DiceNumber(0, 0, 0);
        diceNumbers[3] = new DiceNumber(-90, 0, 0);
        diceNumbers[4] = new DiceNumber(90, 0, 0);
        diceNumbers[5] = new DiceNumber(180, 0, 0);
        diceNumbers[6] = new DiceNumber(0, 90, 0);

        last = this;

        Random random = new Random();
        setOnMouseClicked(event -> {
            setNumber(random.nextInt(6) + 1);
        });

    }

    /**
     * Applies transformations to show passed number
     *
     * @param arrayKey
     */
    public void setNumber(int arrayKey) {
        if (arrayKey > 6 || arrayKey < 0) return;


        double currentZpos = getTranslateZ();

        // Rise dice from GameTable
        TranslateTransition diceGoesUP =
                new TranslateTransition(Duration.millis(1000), this);
        diceGoesUP.setFromZ(currentZpos);
        diceGoesUP.setToZ(-150);
        diceGoesUP.setCycleCount(1);
        diceGoesUP.setAutoReverse(false);


        //Turn dice
        timeline = new Timeline(
                new KeyFrame(Duration.seconds(1),
                        new KeyValue(rotateX.angleProperty(), diceNumbers[arrayKey].getxRot()),
                        new KeyValue(rotateY.angleProperty(), diceNumbers[arrayKey].getyRot()),
                        new KeyValue(rotateZ.angleProperty(), diceNumbers[arrayKey].getzRot())));
        timeline.setAutoReverse(false);
        timeline.setCycleCount(1);


        //Put dice down
        TranslateTransition diceGoesDOWN =
                new TranslateTransition(Duration.millis(1000), this);
        diceGoesDOWN.setFromZ(-150);
        diceGoesDOWN.setToZ(currentZpos);
        diceGoesDOWN.setCycleCount(1);
        diceGoesDOWN.setAutoReverse(false);

        SequentialTransition sequentialTransition = new SequentialTransition();
        sequentialTransition.getChildren().addAll(diceGoesUP, timeline, diceGoesDOWN);
        sequentialTransition.setCycleCount(1);
        sequentialTransition.setAutoReverse(false);
        sequentialTransition.play();

    }


    /**
     * Data structure used to keep info about dices files path and in game position
     */
    public enum DiceType {
        BLACK(523.5, 637.5, "blackDice"),
        WHITE(623, 637.5, "whiteDice"),
        ORANGE(718, 637.5, "orangeDice");

        private static final String BASE_URL = "/Client/UI/GUI/resources/3D";

        private double xPos;
        private double yPos;
        private String path;

        DiceType(double xPos, double yPos, String path) {
            this.xPos = xPos;
            this.yPos = yPos;
            this.path = path;
        }

        private double getxPos() {
            return xPos;
        }

        private double getyPos() {
            return yPos;
        }

        private String getPath() {
            return BASE_URL + "/" + path + "/" + path + ".3ds";
        }
    }

    /**
     * Data structure used to calculate rotation to show a number
     */
    private class DiceNumber {
        Random random = new Random();
        private double xRot;
        private double yRot;
        private double zRot;

        public DiceNumber(double xRot, double yRot, double zRot) {
            this.xRot = xRot;
            this.yRot = yRot;
            this.zRot = zRot;
        }

        public double getxRot() {
            int randomRotation = (random.nextInt(3) - 1) * 360;//Random rotation
            return xRot + randomRotation;
        }

        public double getyRot() {
            int randomRotation = (random.nextInt(3) - 1) * 360;//Random rotation
            return yRot + randomRotation;
        }

        public double getzRot() {
            int randomRotation = (random.nextInt(3) - 1) * 360;//Random rotation
            return zRot + randomRotation;
        }
    }
}
