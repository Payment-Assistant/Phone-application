package payment.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import payment.model.BankEmulator;
import payment.model.Bill;
import payment.model.User;
import payment.view.BillsViewer;
import payment.view.CardInformationViewer;
import payment.view.NewBillViewer;

public class CardInformationController {

    @FXML
    private TextField numberCard1;

    @FXML
    private TextField numberCard2;

    @FXML
    private TextField numberCard3;

    @FXML
    private TextField numberCard4;

    @FXML
    private TextField dateText;

    @FXML
    private TextField yearText;

    @FXML
    private TextField nameText;

    @FXML
    private TextField cvvText;

    @FXML
    private Label resultLabel;

    private static User user;
    private static Stage stage;
    private static Bill bill;
    private static CardInformationViewer viewer;


    @FXML
    void initialize() throws Exception{
        resultLabel.setVisible(false);
    }

    public void onInvisibleButtonClick(){
        numberCard1.setText("1234");
        numberCard2.setText("5678");
        numberCard3.setText("8765");
        numberCard4.setText("4321");
        dateText.setText("01");
        yearText.setText("20");
        nameText.setText("IVANOV PETR");
        cvvText.setText("248");
    }

    public void onExitButtonClick(){
        try {
            new BillsViewer(user, stage).loadScene();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onPayButtonClick(){
        String pack = new StringBuilder(20)
                .append(numberCard1.getText())
                .append(numberCard2.getText())
                .append(numberCard3.getText())
                .append(numberCard4.getText())
                .append(" ")
                .append(dateText.getText())
                .append("/")
                .append(yearText.getText())
                .append(" ")
                .append(nameText.getText())
                .append(" ")
                .append(cvvText.getText())
                .toString();
        int result = BankEmulator.payBill(bill, pack);
        if(result == 1){
            resultLabel.setVisible(true);
        }

    }

    public void setUser(User newUser){user = newUser;}

    public static void setStage(Stage newStage){stage = newStage;}

    public void setBill(Bill newBill){bill = newBill;}

    public void setViewer(CardInformationViewer newViewer){viewer = newViewer;}

}
