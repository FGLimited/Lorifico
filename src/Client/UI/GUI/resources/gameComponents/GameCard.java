package Client.UI.GUI.resources.gameComponents;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by andrea on 04/06/17.
 */
public class GameCard extends AbstractImageComponent {
    public static GameCard last;
    private static Map<Integer, GameCard> map = new HashMap<>();
    private Timeline timeline = new Timeline();

    //vars used for animations:
    private double initialScale, initialY;

    private GameCard(int number) {
        initialScale = 0.23;
        loadImage("Client/UI/GUI/resources/images/carteGioco/devcards_f_en_c_" + number + ".png", 0, initialY, 0, initialScale, 90, 0, 0);
        this.last = this;

        setOnMouseEntered((event -> {
            if (timeline.getStatus() == Animation.Status.RUNNING) timeline.stop();
            timeline = new Timeline(
                    new KeyFrame(Duration.seconds(1),
                            new KeyValue(getTranslate().yProperty(), initialY + 30),
                            new KeyValue(getImageView().scaleXProperty(), initialScale + 0.02),
                            new KeyValue(getImageView().scaleYProperty(), initialScale + 0.02),
                            new KeyValue(getImageView().scaleZProperty(), initialScale + 0.02)));
            timeline.setAutoReverse(false);
            timeline.setCycleCount(1);
            timeline.play();
        }));

        setOnMouseExited((event -> {
            if (timeline.getStatus() == Animation.Status.RUNNING) timeline.stop();
            timeline = new Timeline(
                    new KeyFrame(Duration.seconds(0.5),
                            new KeyValue(getTranslate().yProperty(), initialY),
                            new KeyValue(getImageView().scaleXProperty(), initialScale),
                            new KeyValue(getImageView().scaleYProperty(), initialScale),
                            new KeyValue(getImageView().scaleZProperty(), initialScale)));
            timeline.setAutoReverse(false);
            timeline.setCycleCount(1);
            timeline.play();
        }));
    }

    /**
     * Gets card
     *
     * @param number
     * @return
     */
    public static GameCard getCard(int number) {
        if (map.containsKey(number)) {

        } else {
            map.put(number, new GameCard(number));
        }
        return map.get(number);
    }

    /**
     * Moves card to desired tower position
     *
     * @param level
     */
    public void setTowerLevelPosition(int level) {
        initialY = 64.5;
        getTranslate().setX(-115);
        getTranslate().setY(initialY);
        getTranslate().setZ((-267 - 104 * level));
    }
}
