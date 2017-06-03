package Client.UI.GUI;

import Client.UI.GUI.resources.gameComponents.CylindricalPawn;
import Client.UI.GUI.resources.gameComponents.MyCameraGroup;
import Client.UI.UserInterfaceFactory;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.DepthTest;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Translate;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Io on 27/05/2017.
 */
public class GameUIController implements Client.UI.GameUI, Initializable {
    private MyCameraGroup cameraGroup;//Group containing camera
    private Group world;//World group

    @FXML
    private StackPane root;

    @FXML
    private SubScene subScene;
    //Temp variables for moving objs
    private double mousePosX;
    private double mousePosY;
    private double mouseOldX;
    private double mouseOldY;
    private double mouseDeltaX;
    private double mouseDeltaY;

    @Override
    public void showPage() {
        ((UserInterfaceImplemJFX) (UserInterfaceFactory.getInstance())).changeScene("Gioca", "fxml/GamePage.fxml", 780, 480, true, this);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ((UserInterfaceImplemJFX) UserInterfaceFactory.getInstance()).setStackPane(root);//Updates reference to root stack pane in UserInterface, this way popus will be displayed in this page.

        cameraGroup = new MyCameraGroup(35, 1, 0.0, 270.0, 650.0, -550.);
        world = new Group(cameraGroup);//Create world group containing camera.

        //Setup subscene
        subScene.setDepthTest(DepthTest.ENABLE);
        subScene.setFill(Color.valueOf("8cb22a"));
        subScene.setCamera(cameraGroup.getCamera());
        subScene.setRoot(world);

        //Make subscene resizable
        subScene.heightProperty().bind(root.heightProperty());
        subScene.widthProperty().bind(root.widthProperty());

        Group gameTableGroup = (Group) (UserInterfaceFactory.getInstance().getGameTable());
        world.getChildren().add(gameTableGroup);

        //cameraGroup.setView(MyCameraGroup.CameraPosition.GAMETABLE);
        //UserInterfaceFactory.getInstance().displayPopup(DisplayPopup.Level.Warning, "Titolone", "Messaggione");

        camMouseDrag();
    }

    /**
     * Metodo di servizio per spostare la visuale
     */
    private void camMouseDrag() {

        Scene scene = ((UserInterfaceImplemJFX) (UserInterfaceFactory.getInstance())).getPrimaryStage().getScene();

        subScene.setOnMousePressed(me -> {
            mousePosX = me.getSceneX();
            mousePosY = me.getSceneY();
            mouseOldX = me.getSceneX();
            mouseOldY = me.getSceneY();
        });
        subScene.setOnMouseDragged(me -> {
            mouseOldX = mousePosX;
            mouseOldY = mousePosY;
            mousePosX = me.getSceneX();
            mousePosY = me.getSceneY();
            mouseDeltaX = (mousePosX - mouseOldX);
            mouseDeltaY = (mousePosY - mouseOldY);

            final double MULTIPLIER = .5d;

            if (me.isMiddleButtonDown()) {//Se il tasto centrale è premuto altero l'angolazione X,Y
                cameraGroup.getRotateX().setAngle(cameraGroup.getRotateX().getAngle() - mouseDeltaY * MULTIPLIER);
                cameraGroup.getRotateY().setAngle(cameraGroup.getRotateY().getAngle() + mouseDeltaX * MULTIPLIER);
                printCamCoords();
            }
            if (me.isAltDown()) {//Se il tasto centrale è premuto altero l'angolazione Z
                cameraGroup.getRotateZ().setAngle(cameraGroup.getRotateZ().getAngle() - mouseDeltaY * MULTIPLIER);
                printCamCoords();
            }
            if (me.isSecondaryButtonDown()) {//Se è premuto il tasto DX altero la posizione X,Y
                cameraGroup.getTranslate().setX(cameraGroup.getTranslate().getX() + mouseDeltaX * MULTIPLIER);
                cameraGroup.getTranslate().setY(cameraGroup.getTranslate().getY() + mouseDeltaY * MULTIPLIER);
                printCamCoords();
            }
            if (me.isControlDown()) {//Se è premuto il tasto DX altero la posizione Z
                cameraGroup.getTranslate().setZ(cameraGroup.getTranslate().getZ() + mouseDeltaY * MULTIPLIER);
                printCamCoords();
            }
            if (me.isShiftDown()) {//Placing purpose
                Translate translate = CylindricalPawn.last.getTranslate();
                translate.setX(translate.getX() + mouseDeltaX * MULTIPLIER);
                translate.setY(translate.getY() + mouseDeltaY * MULTIPLIER);
                printStackPCoords(translate);
            }
        });
    }

    /**
     * Camera debug
     */
    private void printCamCoords() {
        System.out.print("Camera: " + cameraGroup.getRotateX().getAngle());
        System.out.print(", " + cameraGroup.getRotateY().getAngle());
        System.out.print(", " + cameraGroup.getRotateZ().getAngle());
        System.out.print(", " + cameraGroup.getTranslate().getX());
        System.out.print(", " + cameraGroup.getTranslate().getY());
        System.out.print(", " + cameraGroup.getTranslate().getZ());
        System.out.println();
    }

    private void printStackPCoords(Translate transform) {
        System.out.print("ObjCoord: " + transform.getX());
        System.out.print(", " + transform.getY());
        System.out.println();
    }

}
