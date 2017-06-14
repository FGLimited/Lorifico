package Client.UI.GUI.resources.gameComponents;

import Action.BaseAction;
import Action.Move;
import Client.CommunicationManager;
import Server.Game.UserObjects.Domestic;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.transform.Translate;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by andrea on 31/05/17.
 */
public class GameTablePlace extends StackPane {
    private final String COVERINGS_BASE_URL = "Client/UI/GUI/resources/images/gameTable";
    private final int Z_POS = -1;
    private Translate translate;
    private int id;//id of this covering
    private int capacity;//Max capacity of this position
    private Map<Domestic, Domestic3D> occupantsDomesticMap = new HashMap<>();

    private GameTablePlace binding = null;//GameTablePlace we are binded to, if that
    // binding place is free then no domestic can be placed in this


    protected GameTablePlace(int id, int capacity, boolean isCovered, GameTablePlace binding, double width, double height, double xCoveringPos, double yCoveringPos, double xRegionPos, double yRegionPos, double radiusX, double radiusY) {
        this(id, capacity, isCovered, width, height, xCoveringPos, yCoveringPos, xRegionPos, yRegionPos, radiusX, radiusY);
        this.binding = binding;
    }
    /**
     * Creates a gameTablePlace that could be covered or be click-able.
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
        this.capacity = capacity;
        this.id = id;
        if (isCovered) {
            final String url = COVERINGS_BASE_URL + "/covering" + id + ".png";
            createCoveringStackPane(url, width, height, xCoveringPos, yCoveringPos, Z_POS);
        } else {
            createClickableRegion(xRegionPos, yRegionPos, Z_POS, radiusX, radiusY);
        }
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
        this.capacity = capacity;
        this.id = id;
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
        ellipse.setOnMouseEntered(event -> {
            //Check if this position is bound to another one
            if (binding != null) {
                //If position we are bound to is not full, abort
                if (!binding.isFull()) return;
            }
            ColorAdjust colorAdjust = new ColorAdjust();
            colorAdjust.setContrast(0.1);
            colorAdjust.setHue(-0.05);
            colorAdjust.setBrightness(0.8);
            colorAdjust.setSaturation(0.3);
            ellipse.setEffect(colorAdjust);
            ellipse.setOpacity(0.35);
        });

        ellipse.setOnMouseExited(event -> {
            ellipse.setEffect(null);
            ellipse.setOpacity(0);
        });

        //Callback on click
        ellipse.setOnMouseClicked(event -> {
            //Check if this position is bound to another one
            if (binding != null) {
                //If position we are bound to is not full, abort
                if (!binding.isFull()) return;
            }

            //Else send message to server asking to occupy this position
            int position = (this.id + occupantsDomesticMap.size());
            BaseAction action = new Move(position, Collections.emptyList());
            CommunicationManager.getInstance().sendMessage(action);
            System.out.println("Asking server to occupy " + position);
        });

    }

    public boolean isFull() {
        if (occupantsDomesticMap.size() >= capacity) return true;
        return false;
    }
}
