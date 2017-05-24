package Client.UI.GUI;

import Client.UI.Lobby;
import Client.UI.UserInterfaceFactory;
import Model.User.User;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
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
    private Timeline countDownTimeline;
    private List<User> userList = new ArrayList<>(1);
    private int timer = 30;

    @FXML
    private StackPane root;

    @FXML
    private Label playersLabel;

    @FXML
    private Label countDownLabel;


    @Override
    public void showPage() {
        ((UserInterfaceImplemJFX) (UserInterfaceFactory.getInstance())).changeScene("Lobby", "fxml/LobbyPage.fxml", 415, 415, true, this);

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ((UserInterfaceImplemJFX) UserInterfaceFactory.getInstance()).setStackPane(root);//Updates reference to root stack pane in UserInterface, this way popus will be displayed in this page.

        Timeline timeline = new Timeline(new KeyFrame(
                Duration.millis(1000),
                ae -> decrementCountdown()));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
        System.out.println("Started Countdown");
    }


    @Override
    public synchronized void setMatchAttendees(List<User> userList) {
        this.userList = userList;
        restartTimer();
    }

    @Override
    public synchronized void restartTimer() {
        this.timer = 30;
    }

    private synchronized void decrementCountdown() {
        if (timer > 0 && userList.size() > 1) {
            timer--;
        }
        countDownLabel.setText(((Integer) timer).toString());
    }

}
