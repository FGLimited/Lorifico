package Client.UI.GUI.resources.gameComponents;


import Client.UI.GameTable;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.transform.Translate;


public class GameTableGroup extends Group implements GameTable {
    //Constants
    private static final String TABLE_IMAGE_URL = "Client/UI/GUI/resources/images/gameTable/gameTable.png";

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
        getChildren().add(new GameTablePlace(50, 4, 430, 78.5, 135, 57));
        getChildren().add(new GameTablePlace(42, 1, false, 90, 105, 680.5, 455.5, 684, 462, 43, 40));
        getChildren().add(new GameTablePlace(43, 1, false, 91.17, 108.2, 748.5, 520, 751.5, 531.5, 43, 40));
        getChildren().add(new GameTablePlace(41, 1, 595.5, 437, 43, 40));
        getChildren().add(new GameTablePlace(40, 1, 501.5, 436.5, 43, 40));
        getChildren().add(new GameTablePlace(30, 1, 7.5, 461.5, 43, 40));
        getChildren().add(new GameTablePlace(20, 1, 8, 584, 43, 40));
        getChildren().add(new GameTablePlace(21, 3, false, 226, 111.8, 116.5, 575.5, 130.5, 587, 100, 35));
        getChildren().add(new GameTablePlace(31, 3, false, 224, 105.7, 116, 457, 130, 465, 100, 35));

        //Load dices
        getChildren().add(new Dice(Dice.DiceType.WHITE));
        getChildren().add(new Dice(Dice.DiceType.ORANGE));
        getChildren().add(new Dice(Dice.DiceType.BLACK));

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

        Image image = new Image(url);
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(height);
        imageView.setFitWidth(width);

        stackPane.getChildren().add(imageView);
        stackPane.setPrefSize(width, height);
        stackPane.getTransforms().add(new Translate(xPos, yPos, zPos));
        return stackPane;
    }
}