package Client.UI.GUI.resources.gameComponents;

import Logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.util.Duration;

/**
 * Creates a PerspectiveCamera Object inside a Group
 */
public class MyCameraGroup extends Group {
    //Costants:
    private static final double CAMERA_NEAR_CLIP = 10;
    private static final double CAMERA_FAR_CLIP = 2000.0;

    //Rotations / Translations
    private Rotate rotateX, rotateY, rotateZ;
    private Translate translate;

    private PerspectiveCamera camera;

    private Timeline timeline;

    /**
     * Creates a PerspectiveCamera Object inside a Group
     *
     * @param xPos     Initial camera xPos
     * @param yPos     Initial camera yPos
     * @param zPos     Initial camera zPos
     * @param xAxisRot Initial rotation around xAxis
     * @param yAxisRot Initial rotation around yAxis
     * @param zAxisRot Initial rotation around zAxis
     */
    public MyCameraGroup(double xAxisRot, double yAxisRot, double zAxisRot, double xPos, double yPos, double zPos) {
        super();
        rotateX = new Rotate(xAxisRot, Rotate.X_AXIS);
        rotateY = new Rotate(yAxisRot, Rotate.Y_AXIS);
        rotateZ = new Rotate(zAxisRot, Rotate.Z_AXIS);
        translate = new Translate(xPos, yPos, zPos);

        camera = new PerspectiveCamera(true);
        camera.setNearClip(CAMERA_NEAR_CLIP);
        camera.setFarClip(CAMERA_FAR_CLIP);
        camera.setFieldOfView(45);
        Logger.log(Logger.LogLevel.Normal, "Camera field of view: " + camera.getFieldOfView());

        getTransforms().addAll(translate, rotateX, rotateY, rotateZ);
        getChildren().add(camera);

        timeline = new Timeline();
    }

    public Rotate getRotateX() {
        return rotateX;
    }

    public Rotate getRotateY() {
        return rotateY;
    }

    public Rotate getRotateZ() {
        return rotateZ;
    }

    public Translate getTranslate() {
        return translate;
    }

    public PerspectiveCamera getCamera() {
        return camera;
    }

    /**
     * Moves camera to specified position
     *
     * @param cameraPosition
     */
    public void setView(CameraPosition cameraPosition) {
        timeline = new Timeline(
                new KeyFrame(Duration.seconds(3),
                        new KeyValue(getRotateX().angleProperty(), cameraPosition.getxAxisRot()),
                        new KeyValue(getRotateY().angleProperty(), cameraPosition.getyAxisRot()),
                        new KeyValue(getRotateZ().angleProperty(), cameraPosition.getzAxisRot()),
                        new KeyValue(getTranslate().xProperty(), cameraPosition.getxAxisPos()),
                        new KeyValue(getTranslate().yProperty(), cameraPosition.getyAxisPos()),
                        new KeyValue(getTranslate().zProperty(), cameraPosition.getzAxisPos())));
        timeline.setAutoReverse(false);
        timeline.setCycleCount(1);
        timeline.play();
    }

    /**
     * This enum stores camera fixed position to look at different pieces of game
     */
    public enum CameraPosition {
        GAMETABLE(33.0, 0, 0.0, 413.66666, 812.6666663, -623.33333350),
        TOWERS(82.0, -9.5, 0.0, 560.16663, 648.16666, -332.666664);

        private double xAxisRot;
        private double yAxisRot;
        private double zAxisRot;

        private double xAxisPos;
        private double yAxisPos;
        private double zAxisPos;

        CameraPosition(double xAxisRot, double yAxisRot, double zAxisRot, double xAxisPos, double yAxisPos, double zAxisPos) {
            this.xAxisRot = xAxisRot;
            this.yAxisRot = yAxisRot;
            this.zAxisRot = zAxisRot;
            this.xAxisPos = xAxisPos;
            this.yAxisPos = yAxisPos;
            this.zAxisPos = zAxisPos;
        }

        protected double getxAxisPos() {
            return xAxisPos;
        }

        protected double getyAxisPos() {
            return yAxisPos;
        }

        protected double getzAxisPos() {
            return zAxisPos;
        }

        protected double getxAxisRot() {
            return xAxisRot;
        }

        protected double getyAxisRot() {
            return yAxisRot;
        }

        protected double getzAxisRot() {
            return zAxisRot;
        }
    }

    ;
}
