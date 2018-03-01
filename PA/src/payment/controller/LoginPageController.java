package payment.controller;

import javafx.fxml.FXML;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import payment.model.Bill;
import payment.model.User;
import payment.view.BillsViewer;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginPageController {

    private static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    private static final Pattern VALID_PHONE_NUMBER_REGEX =
            Pattern.compile("^((8|\\+7)[\\- ]?)?(\\(?\\d{3}\\)?[\\- ]?)?[\\d\\- ]{7,10}$", Pattern.CASE_INSENSITIVE);

    private static boolean validateEmail(String emailStr) {
        if (emailStr == null || emailStr.equals("")) return false;
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }

    private static boolean validatePhoneNumber(String phoneNumb) {
        if (phoneNumb == null || phoneNumb.equals("")) return false;
        Matcher matcher = VALID_PHONE_NUMBER_REGEX.matcher(phoneNumb);
        return matcher.find();
    }

    private static boolean validatePassword(String passwd) {
        return !(passwd == null) && !passwd.equals("") && !(passwd.length() < 6) ;
    }

    private static boolean validateName(String name) {
        return (!(name == null || name.equals("") ));
    }

    @FXML
    private TextField email;
    @FXML
    private TextField phoneNumber;
    @FXML
    private TextField password;
    @FXML
    private TextField repeatPassword;
    @FXML
    private TextField name;
    @FXML
    private Label errorMessage;

    private User user;

    private static Stage stage;

    @FXML
    protected void handleRegistrationButton(MouseEvent event) {

        if (validateName(name.getText())
                && validatePhoneNumber(phoneNumber.getText())
                && (validatePassword(password.getText()))
                && (validatePassword(repeatPassword.getText()) && repeatPassword.getText().equals(password.getText()))) {
            user = new User(name.getText(), phoneNumber.getText(), password.getText());
            if(validateEmail(email.getText())) user.changeEmail(email.getText());
            loadNextScene();
        }
        else
            errorMessage.setText("Проверьте корректность введённых данных!");

    }

    @FXML
    public void handleNameKeyReleased(KeyEvent keyEvent) {
        if (!validateName(name.getText())) {
            name.setStyle("-fx-control-inner-background: rgba(255,0,49,0.47)");
        } else name.setStyle("-fx-control-inner-background: LIGHTGREEN");
    }

    @FXML
    public void handlePasswordKeyReleased(KeyEvent keyEvent) {
        if (!validatePassword(password.getText())) {
            password.setStyle("-fx-control-inner-background: rgba(255,0,49,0.47)");
        } else password.setStyle("-fx-control-inner-background: LIGHTGREEN");

        handleRepeatPasswordKeyReleased(keyEvent);
    }

    @FXML
    public void handlePhoneKeyReleased(KeyEvent keyEvent) {
        if (!validatePhoneNumber(phoneNumber.getText())) {
            phoneNumber.setStyle("-fx-control-inner-background: rgba(255,0,49,0.47)");
        } else phoneNumber.setStyle("-fx-control-inner-background: LIGHTGREEN");
    }

    @FXML
    public void handleEmailKeyReleased(KeyEvent keyEvent) {
        if (validateEmail(email.getText())) {
            email.setStyle("-fx-control-inner-background: LIGHTGREEN");
        } else {
            if (email.getText().equals("")) email.setStyle("-fx-control-inner-background: WHITE");
            else email.setStyle("-fx-control-inner-background: rgba(255,0,49,0.47)");
        }
    }

    @FXML
    public void handleRepeatPasswordKeyReleased(KeyEvent keyEvent) {
        if (validatePassword(repeatPassword.getText()) && repeatPassword.getText().equals(password.getText())) {
            repeatPassword.setStyle("-fx-control-inner-background: LIGHTGREEN");
        } else repeatPassword.setStyle("-fx-control-inner-background: rgba(255,0,49,0.47)");
    }

    public static void setStage(Stage newStage){
        stage = newStage;
    }

    private void loadNextScene(){
        user.addBillIncome("1", "Кошка лялялялялялялялял", "ООО Мармелад", new Date(), new Date(),
                Bill.Currency.RUB, 1000 );
        user.addBillIncome("2", "Собака", "ООО Мармелад", new Date(), new Date(),
                Bill.Currency.RUB, 2000);
        user.addBillIncome("3", "Попугай", "ООО Мармелад", new Date(), new Date(),
                Bill.Currency.RUB, 4000);

        user.addBillOutcome("4", "Стрижка", "ООО Кат-кат", new Date(), new Date(),
                Bill.Currency.RUB, 600);
        user.addBillOutcome("5", "Парковка", "ООО Пенек", new Date(), new Date(),
                Bill.Currency.RUB, 500);

        BillsViewer billsViewer = new BillsViewer(user);
        try {
            billsViewer.loadScene(stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
