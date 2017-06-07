package Client.UI.GUI;

import Action.BaseAction;
import Action.StartMatch;
import Client.CommunicationManager;
import Client.UI.Lobby;
import Client.UI.UserInterfaceFactory;
import Model.User.User;
import com.jfoenix.controls.JFXButton;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by Io on 23/05/2017.
 */
public class LobbyPageController implements Lobby, Initializable {
    private List<User> userList = new ArrayList<>(1);
    private Timeline timeline;
    private int timer = 30;


    @FXML
    private StackPane root;

    @FXML
    private Label playersLabel;

    @FXML
    private Label countDownLabel;

    @FXML
    private JFXButton startGameButton;



    @Override
    public void showPage() {
        ((UserInterfaceImplemJFX) (UserInterfaceFactory.getInstance())).changeScene("Lobby", "fxml/LobbyPage.fxml", 415, 415, true, this);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ((UserInterfaceImplemJFX) UserInterfaceFactory.getInstance()).setStackPane(root);//Updates reference to root stack pane in UserInterface, this way popus will be displayed in this page.

        timeline = new Timeline(new KeyFrame(
                Duration.millis(1000),
                ae -> decrementCountdown()));
        timeline.setCycleCount(Animation.INDEFINITE);

        synchronized (this) {
            notifyAll();//Notify we are ready to get this filled
        }
    }


    @Override
    public void setMatchAttendees(List<User> userList) {
        this.userList = userList;
    }

    @Override
    public synchronized void restartTimer() {
        //Wait for UI to show up
        synchronized (this) {
            try {
                while (timeline == null)
                    wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        this.timer = 30;//Reset timer

        //If timeline is playing, stop it.
        if (timeline.getStatus() == Animation.Status.RUNNING) timeline.stop();

        //If we are at least in two, let's start countdown
        if (userList.size() > 1) {
            timeline.play();
            startGameButton.setDisable(false);//Enable button to start game immediately.
        }
    }

    private synchronized void decrementCountdown() {
        if (timer > 0 && userList.size() > 1) {
            timer--;
        }
        countDownLabel.setText(((Integer) timer).toString());
    }

    @FXML
    void startGame(ActionEvent event) {
        BaseAction baseAction = new StartMatch();
        CommunicationManager.getInstance().sendMessage(baseAction);
    }

}
