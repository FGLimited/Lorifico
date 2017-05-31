package Client.UI.GUI.resources.gameComponents;

import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.transform.Translate;

/**
 * Created by andrea on 31/05/17.
 */
public class GameTablePlace extends StackPane {
    //DEBUG:
    public static GameTablePlace last;
    private final String COVERINGS_BASE_URL = "Client/UI/GUI/resources/images/gameTable";
    private final int Z_POS = 2;
    private Translate translate;

    /**
     * Creates a gameTablePlace that could be covered
     *
     * @param id
     * @param isCovered
     * @param width
     * @param height
     * @param xCoveringPos
     * @param yCoveringPos
     * @param xRegionPos
     * @param yRegionPos
     * @param radiusX
     * @param radiusY
     */
    protected GameTablePlace(int id, int capacity, boolean isCovered, double width, double height, double xCoveringPos, double yCoveringPos, double xRegionPos, double yRegionPos, double radiusX, double radiusY) {
        if (isCovered) {
            final String url = COVERINGS_BASE_URL + "/covering" + id + ".png";
            createCoveringStackPane(url, width, height, xCoveringPos, yCoveringPos, Z_POS);
        } else {
            createClickableRegion(xRegionPos, yRegionPos, Z_POS, radiusX, radiusY);
        }
        this.last = this;
    }

    /**
     * Creates a gameTablePlace always active.
     *
     * @param id
     * @param xRegionPos
     * @param yRegionPos
     * @param radiusX
     * @param radiusY
     */
    protected GameTablePlace(int id, int capacity, double xRegionPos, double yRegionPos, double radiusX, double radiusY) {
        createClickableRegion(xRegionPos, yRegionPos, Z_POS, radiusX, radiusY);
        this.last = this;
    }


    /**
     * Loads the covering image and places it
     *
     * @param url
     * @param width
     * @param height
     * @param xPos
     * @param yPos
     * @param zPos
     */
    private void createCoveringStackPane(String url, double width, double height, double xPos, double yPos, double zPos) {
        Image image = new Image(url, true);
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(height);
        imageView.setFitWidth(width);
        getChildren().add(imageView);
        setPrefSize(width, height);
        translate = new Translate(xPos, yPos, zPos);
        getTransforms().add(translate);
    }

    /**
     * Creates a trasparent figure and places it
     *
     * @param xPos
     * @param yPos
     * @param zPos
     * @param radiusX
     * @param radiusY
     */
    private void createClickableRegion(double xPos, double yPos, double zPos, double radiusX, double radiusY) {
        Ellipse ellipse = new Ellipse(radiusX, radiusY);
        ellipse.setFill(Color.WHITE);
        ellipse.setOpacity(0);//it's trasparent

        getChildren().add(ellipse);
        ellipse.radiusXProperty().bind(prefWidthProperty());
        ellipse.radiusYProperty().bind(prefHeightProperty());
        setPrefSize(radiusX, radiusY);
        translate = new Translate(xPos, yPos, zPos);
        getTransforms().add(translate);

        //We highlight it when mouse is over it
        ellipse.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> {
            ColorAdjust colorAdjust = new ColorAdjust();
            colorAdjust.setContrast(0.1);
            colorAdjust.setHue(-0.05);
            colorAdjust.setBrightness(0.8);
            colorAdjust.setSaturation(0.3);
            ellipse.setEffect(colorAdjust);
            ellipse.setOpacity(0.35);
        });

        ellipse.addEventHandler(MouseEvent.MOUSE_EXITED, event -> {
            ellipse.setEffect(null);
            ellipse.setOpacity(0);
        });
    }

    //Debugging / placing purpose
    public Translate getTranslate() {
        return translate;
    }
}
