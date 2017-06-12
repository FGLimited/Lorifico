package Client.UI.GUI.resources.gameComponents;


import Client.UI.GameTable;
import Game.UserObjects.DomesticColor;
import Game.UserObjects.FamilyColor;
import Server.Game.UserObjects.Domestic;
import javafx.application.Platform;
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

    private GameTablePlacesBlock gameTablePlacesBlock;//Group containing coverings and gameTablePlaces

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
        getChildren().add(createGameImageBox(TABLE_IMAGE_URL, IMAGE_WIDTH, IMAGE_HEIGHT, xPos, yPos, zPos));

        //Place coverings / places
        gameTablePlacesBlock = new GameTablePlacesBlock();
        getChildren().add(gameTablePlacesBlock);

        //Load test domestic
        getChildren().add(new Domestic3D(new Domestic(FamilyColor.Yellow, DomesticColor.Orange, 3)));

        //Apply Transforms
        getTransforms().add(new Translate(xPos, yPos, zPos));

    }

    /**
     * Called when a new turn is starting and all position have to be free.
     */
    @Override
    public void freeAllPositions() {
        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(() -> freeAllPositions());
            return;
        }
        gameTablePlacesBlock.freeAllPosition();
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
    private StackPane createGameImageBox(String url, double width, double height, double xPos, double yPos, double zPos) {
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