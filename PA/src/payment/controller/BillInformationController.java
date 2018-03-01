package payment.controller;

import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import payment.model.User;
import payment.view.BillInformationAndPayViewer;
import payment.view.BillInformationViewer;
import payment.view.BillsViewer;

public class BillInformationController {

    @FXML
    private Label sumUpperLabel;

    @FXML
    private Label senderLabel;

    @FXML
    private Label descriptionLabel;

    @FXML
    private Label dateLabel;

    @FXML
    private Label sumLabel;

    @FXML
    private Label statusLabel;

    @FXML
    private GridPane menuPane;

    @FXML
    private Button menuButton;

    @FXML
    private Button menuReturnButton;

    private static BillInformationViewer viewer;
    private static User user;
    private static Stage stage;

    @FXML
    void initialize() throws Exception{
        viewer.pullInformation(senderLabel, descriptionLabel, dateLabel, sumLabel, sumUpperLabel, statusLabel);

        menuPane.setTranslateX(-200);
        TranslateTransition menuMoving = new TranslateTransition(Duration.millis(300), menuPane);
        menuMoving.setFromX(-200);
        menuMoving.setToX(0);

        menuButton.setOnMouseClicked(evt ->{
            menuMoving.setRate(1);
            menuMoving.play();
        });

        menuReturnButton.setOnMouseClicked(evt ->{
            menuMoving.setRate(-1);
            menuMoving.play();
        });
    }

    public static void setViewer(BillInformationViewer newViewer){
        viewer = newViewer;
    }

    public static void setUser(User newUser){user = newUser;}

    public static void setStage(Stage primaryStage){stage = primaryStage;}

    public void onReturnButtonClick(){
        try {
            new BillsViewer(user).loadScene(stage);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

}
