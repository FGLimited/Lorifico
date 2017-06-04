package Client.UI.GUI.resources.gameComponents;


import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

/**
 * Creates a group containing an Image.
 */
public abstract class AbstractImageComponent extends Group {
    private Translate translate;
    private ImageView imageView;

    public void loadImage(String path, double xPos, double yPos, double zPos, double scale, double rotateX, double rotateY, double rotateZ) {
        Image image = new Image(path, true);
        imageView = new ImageView(image);
        imageView.setScaleX(scale);
        imageView.setScaleY(scale);

        translate = new Translate(xPos, yPos, zPos);

        getChildren().add(imageView);
        getTransforms().addAll(translate, new Rotate(rotateX, Rotate.X_AXIS), new Rotate(rotateY, Rotate.Y_AXIS), new Rotate(rotateZ, Rotate.Z_AXIS));
    }

    ImageView getImageView() {
        return imageView;
    }

    public Translate getTranslate() {
        return translate;
    }

}