package Client.UI.GUI.resources.gameComponents;


import Client.UI.GameTable;
import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Translate;

public class GameTableGroup extends Group implements GameTable {
    //Constants
    private static final String IMAGE_URL = "Client/UI/GUI/resources/images/gameTable.png";

    private static final double IMAGE_WIDTH = 850;
    private static final double IMAGE_HEIGHT = 433;

    //Local Objs
    private StackPane stackPane;
    private Rectangle rectangle;
    private Pane pane;

    //Trasformations
    private Translate translate;

    /**
     * Creates a new plane showing game table
     *
     * @param xPos
     * @param yPos
     * @param zPos
     */
    public GameTableGroup(double xPos, double yPos, double zPos) {
        super();
        /*
         * We need a rectangle to limit drag n' drop movements to this plane
         * http://gamedev.stackexchange.com/questions/72924/how-do-i-add-an-image-inside-a-rectangle-or-a-circle-in-javafx
         * btw i modified it a bit...
         * */

        stackPane = new StackPane();
        rectangle = new Rectangle(IMAGE_WIDTH, IMAGE_HEIGHT);
        pane = new Pane();
        rectangle.setFill(Color.TRANSPARENT);

        pane.setStyle("-fx-background-image: url(\"" + IMAGE_URL + "\");\n" +
                "    -fx-background-repeat: no-repeat;\n" +
                "    -fx-background-size: contain;");

        stackPane.getChildren().addAll(pane, rectangle);
        rectangle.widthProperty().bind(stackPane.prefWidthProperty());
        rectangle.heightProperty().bind(stackPane.prefHeightProperty());
        stackPane.setPrefSize(IMAGE_WIDTH, IMAGE_HEIGHT);

        /*  </copied part> **/

        translate = new Translate(xPos, yPos, zPos);
        getChildren().add(stackPane);
        this.getTransforms().add(translate);

    }

    /**
     * Creates a new plane showing game table
     */
    public GameTableGroup() {
        this(0, 0, 0);
    }

    public Rectangle getRectangle() {
        return rectangle;
    }
}
