package Client.UI.GUI.resources.gameComponents;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.util.Duration;

/**
 * Created by andrea on 01/06/17.
 */
public class CylindricalPawn extends Group {
    public static CylindricalPawn last;
    private final double CYLINDER_RADIUS = 15;
    private final double CYLINDER_HEIGHT = 10;
    private final double CYLINDER_MIN_Z = -5;
    private Translate translate;
    private Timeline timeline;

    /**
     * Creates a new Cylindrical Pawn inside a group.
     *
     * @param color         color of pawn
     * @param xPos          initial x pos
     * @param yPos          initial y pos
     * @param stackPosition initial pawn stack value
     */
    public CylindricalPawn(Color color, double xPos, double yPos, int stackPosition) {
        Cylinder cylinder = new Cylinder(CYLINDER_RADIUS, CYLINDER_HEIGHT);
        PhongMaterial phongMaterial = new PhongMaterial(color);
        cylinder.setMaterial(phongMaterial);

        Group cylinderGroup = new Group(cylinder);
        translate = new Translate();
        Rotate rotate = new Rotate(90, Rotate.X_AXIS);
        cylinderGroup.getTransforms().addAll(translate, rotate);
        translate.setX(xPos);
        translate.setY(yPos);
        translate.setZ(calculateStackPosition(stackPosition));

        getChildren().add(cylinderGroup);

        last = this;
    }

    /**
     * Moves pawm to given position immediately
     *
     * @param xPos
     * @param yPos
     * @param stackPosition
     */
    public void setPosition(double xPos, double yPos, int stackPosition) {
        translate.setX(xPos);
        translate.setY(yPos);
        translate.setZ(calculateStackPosition(stackPosition));
    }

    /**
     * Moves Pawm to given position with animation
     *
     * @param xPos
     * @param yPos
     * @param stackPosition
     */
    public void animateToPosition(double xPos, double yPos, int stackPosition) {
        timeline = new Timeline(
                new KeyFrame(Duration.seconds(1),
                        new KeyValue(translate.xProperty(), xPos),
                        new KeyValue(translate.yProperty(), yPos),
                        new KeyValue(translate.zProperty(), calculateStackPosition(stackPosition))));
        timeline.setAutoReverse(false);
        timeline.setCycleCount(1);
        timeline.play();
    }

    public Translate getTranslate() {
        return translate;
    }

    private double calculateStackPosition(int stackPosition) {
        return (CYLINDER_MIN_Z - (CYLINDER_HEIGHT * 1.1d) * (double) stackPosition);
    }
}
