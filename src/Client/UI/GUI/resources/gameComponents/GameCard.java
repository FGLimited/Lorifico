package Client.UI.GUI.resources.gameComponents;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.util.Duration;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by andrea on 04/06/17.
 */
public class GameCard extends AbstractImageComponent {
    public static GameCard last;
    private static Map<Integer, GameCard> map = new HashMap<>();
    boolean isHoverEnabled = false;
    private Timeline timeline = new Timeline();
    //vars used for animations:
    private double initialScale, initialY;

    private GameCard(int number) {
        initialScale = 0.23;
        loadImage("Client/UI/GUI/resources/images/carteGioco/devcards_f_en_c_" + number + ".png", 0, initialY, 0, initialScale, 90, 0, 0);
        this.last = this;

        setOnMouseEntered((event -> {
            if (!isHoverEnabled) return;
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
            if (!isHoverEnabled) return;
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
        setHoverEnabled(false);//Stop animations to prevent mess with timelines
        initialY = 64.5;

        getTranslate().xProperty().setValue(-115);
        getTranslate().yProperty().setValue(initialY + 50);
        getTranslate().zProperty().setValue(-863);

        if (timeline.getStatus() == Animation.Status.RUNNING) timeline.stop();//If we are already animating, stop it.

        timeline = new Timeline(
                new KeyFrame(Duration.seconds(5),
                        new KeyValue(getTranslate().yProperty(), initialY),
                        new KeyValue(getTranslate().zProperty(), -267 - 104 * level)));
        timeline.setAutoReverse(false);
        timeline.setCycleCount(1);
        timeline.play();
        timeline.setOnFinished(event -> setHoverEnabled(true));
    }

    /**
     * Moves card under gameTable and then removes it from tower
     *
     * @param group group (tower) where card is present
     */
    public void moveCardToFloorThenRemoveFromGroup(Group group) {
        setHoverEnabled(false);//Stop animations

        if (timeline.getStatus() == Animation.Status.RUNNING) timeline.stop();//If we are already animating, stop it.

        //Moves card to floor
        timeline = new Timeline(
                new KeyFrame(Duration.seconds(2),
                        new KeyValue(getTranslate().yProperty(), initialY),
                        new KeyValue(getTranslate().zProperty(), -150)));
        timeline.setAutoReverse(false);
        timeline.setCycleCount(1);
        timeline.play();

        //Removes card from group (tower) when finished
        timeline.setOnFinished(event -> {
            if (group.getChildren().contains(this))
                group.getChildren().remove(this);
        });
    }

    public boolean isHoverEnabled() {
        return isHoverEnabled;
    }

    public void setHoverEnabled(boolean hoverEnabled) {
        isHoverEnabled = hoverEnabled;
    }
}
