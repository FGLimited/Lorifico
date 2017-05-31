package Client.UI.GUI.resources.gameComponents;


import Client.UI.GameTable;
import javafx.scene.Group;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Translate;


public class GameTableGroup extends Group implements GameTable {
    //Constants
    private static final String TABLE_IMAGE_URL = "Client/UI/GUI/resources/images/gameTable/gameTable.png";
    private static final String COVERINGS_BASE_URL = "Client/UI/GUI/resources/images/gameTable";

    private static final double IMAGE_WIDTH = 850;
    private static final double IMAGE_HEIGHT = 694;
    private int i = 0;

    /**
     * Creates a new plane showing game table
     */
    public GameTableGroup() {
        this(0, 0, 0);
    }

    /**
     * Creates a new plane showing game table at given position
     *
     * @param xPos
     * @param yPos
     * @param zPos
     */
    public GameTableGroup(double xPos, double yPos, double zPos) {
        super();
        //Place Table
        getChildren().add(createImageBox(TABLE_IMAGE_URL, IMAGE_WIDTH, IMAGE_HEIGHT, xPos, yPos, zPos));

        //Place Coverings
        getChildren().add(createImageBox(COVERINGS_BASE_URL + "/covering1.png", 95, 100, 685, 460, 5));

        //Apply Transforms
        getTransforms().add(new Translate(xPos, yPos, zPos));

    }


    /**
     * Creates a new stackpane containing a rectangle filled with passed image
     *
     * @param url    image used to fill rectangle
     * @param width  image w
     * @param height image h
     * @param xPos
     * @param yPos
     * @param zPos
     * @return
     */
    private StackPane createImageBox(String url, double width, double height, double xPos, double yPos, double zPos) {
        StackPane stackPane = new StackPane();
        Rectangle rectangle = new Rectangle(width, height);
        rectangle.setFill(Color.RED);


        Image image = new Image(url, true);
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(height);
        imageView.setFitWidth(width);


        i++;
        if (i == 2) {
            imageView.setOpacity(0.4);
            imageView.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> {
                ColorAdjust colorAdjust = new ColorAdjust();
                colorAdjust.setContrast(0.1);
                colorAdjust.setHue(-0.05);
                colorAdjust.setBrightness(0.5);
                colorAdjust.setSaturation(0.3);
                imageView.setEffect(colorAdjust);
            });
            imageView.addEventHandler(MouseEvent.MOUSE_EXITED, event -> {
                imageView.setEffect(null);
            });
        }


        stackPane.getChildren().add(imageView);
        rectangle.widthProperty().bind(stackPane.prefWidthProperty());
        rectangle.heightProperty().bind(stackPane.prefHeightProperty());
        stackPane.setPrefSize(width, height);
        stackPane.getTransforms().add(new Translate(xPos, yPos, zPos));
        return stackPane;
    }
}