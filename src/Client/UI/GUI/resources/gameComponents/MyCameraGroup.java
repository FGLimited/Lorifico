package Client.UI.GUI.resources.gameComponents;

import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

/**
 * Creates a PerspectiveCamera Object inside a Group
 */
public class MyCameraGroup extends Group {
    //Costants:
    private static final double CAMERA_NEAR_CLIP = 0.1;
    private static final double CAMERA_FAR_CLIP = 10000.0;

    //Rotations / Translations
    private Rotate rotateX, rotateY, rotateZ;
    private Translate translate;

    private PerspectiveCamera camera;

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
    public MyCameraGroup(double xPos, double yPos, double zPos, double xAxisRot, double yAxisRot, double zAxisRot) {
        super();
        rotateX = new Rotate(xAxisRot, Rotate.X_AXIS);
        rotateY = new Rotate(yAxisRot, Rotate.Y_AXIS);
        rotateZ = new Rotate(zAxisRot, Rotate.Z_AXIS);
        translate = new Translate(xPos, yPos, zPos);

        camera = new PerspectiveCamera(true);
        camera.setNearClip(CAMERA_NEAR_CLIP);
        camera.setFarClip(CAMERA_FAR_CLIP);

        getTransforms().addAll(translate, rotateX, rotateY, rotateZ);
        getChildren().add(camera);
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
}
