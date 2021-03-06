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
import payment.view.BillsViewer;
import payment.view.SettingsViewer;

public class BillInformationAndPayController {

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
    private GridPane menuPane;

    @FXML
    private Button menuButton;

    @FXML
    private Button menuReturnButton;

    @FXML
    private Button payButton;

    @FXML
    private Button payAndMessageButton;

    private static BillInformationAndPayViewer viewer;
    private static User user;
    private static Stage stage;

    @FXML
    void initialize() throws Exception{
        viewer.pullInformation(senderLabel, descriptionLabel, dateLabel, sumLabel, sumUpperLabel);

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

    public static void setViewer(BillInformationAndPayViewer newViewer){
        viewer = newViewer;
    }

    public static void setUser(User newUser){user = newUser;}

    public static void setStage(Stage primaryStage){stage = primaryStage;}

    public void onReturnButtonClick(){
        try {
            new BillsViewer(user, stage).loadScene();
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void onSettingsButtonClick(){
        try {
            new SettingsViewer(user).loadScene(stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}