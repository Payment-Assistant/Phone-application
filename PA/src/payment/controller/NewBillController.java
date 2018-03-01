package payment.controller;

import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import payment.model.Bill;
import payment.model.User;
import payment.view.BillsViewer;

import java.util.Date;

public class NewBillController {

    @FXML
    private GridPane menuPane;

    @FXML
    private Button menuButton;

    @FXML
    private Button menuReturnButton;

    @FXML
    private TextArea descriptionTextArea;

    @FXML
    private TextField sumTextField;

    private static User user;
    private static Stage stage;

    @FXML
    void initialize() throws Exception{
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

    @FXML
    private void onReturnButtonClick(){
        try {
            new BillsViewer(user).loadScene(stage);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    @FXML
    private void onToSendBillButtonClick(){
        createNewBill();
    }

    private void createNewBill(){
        User payUser = new User("8(888)888-88-88", "");   //здесь мы достаем юзера по номеру телефона
        String idOfBill = "1908";       //здесь генерируется id счета по базе данных
        payUser.addBillIncome(idOfBill,
                              descriptionTextArea.getText(),
                              user.getPhoneNumber(),
                              new Date(), new Date(),
                              Bill.Currency.RUBLE,
                              Integer.parseInt(sumTextField.getText())); //добавить парсинг даты оплаты и всякие проверки
        user.addBillOutcome(idOfBill,
                descriptionTextArea.getText(),
                user.getPhoneNumber(),
                new Date(), new Date(),
                Bill.Currency.RUBLE,
                Integer.parseInt(sumTextField.getText()));
        System.out.println("Счет создан");
    }

    public static void setUser(User newUser){user = newUser;}

    public static void setStage(Stage newStage){stage = newStage;}

}
