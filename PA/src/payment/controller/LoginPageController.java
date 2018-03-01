package payment.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import javafx.fxml.FXML;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import payment.model.Bill;
import payment.model.User;
import payment.view.BillsViewer;

import java.io.*;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginPageController {

    ServerConnection sc = new ServerConnection();

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
    public Label errorMessageL;
    @FXML
    public TextField PassEnterText;
    @FXML
    public TextField NumberEnterText;
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
            try {
                Algorithm algorithm = Algorithm.HMAC256("password");
                String token = JWT.create()
                        .withClaim("action","register")
                        .withClaim("login", phoneNumber.getText())
                        .withClaim("password",password.getText())
                        .withClaim("name",name.getText().split(" ")[0])
                        .withClaim("surname",name.getText().split(" ")[1])
                        .withClaim("phone",phoneNumber.getText())
                        .withClaim("email",email.getText())
                        .sign(algorithm);

                sc.wr = new OutputStreamWriter(sc.con.getOutputStream());
                sc.wr.write(token);
                sc.wr.flush();
                sc.wr.close();

                sc.in = new BufferedReader(new InputStreamReader(sc.con.getInputStream()));
                String resp = sc.in.readLine();
                sc.in.close();
                DecodedJWT decodedJWT = JWT.decode(resp);
                String ans = decodedJWT.getClaim("answer").asString();
                if (ans.equals("success"))
                    loadNextScene();
                if(ans.equals("matchingLogin"))
                    errorMessage.setText("Этот телефон уже зарегистрирован");
            }
            catch(UnsupportedEncodingException e){
                System.out.println("Беда");
            }
            catch (IOException e){
                System.out.println("IO beda");
            }
            finally {
                System.out.println("Final беда");
            }
        }
        else
            errorMessage.setText("Проверьте корректность введённых данных!");

    }

    public void handleLoginButton(MouseEvent mouseEvent) {

        sc.connectToServer();
        try {
            Algorithm algorithm = Algorithm.HMAC256("password");
            String token = JWT.create()
                    .withClaim("action", "authorize")
                    .withClaim("login", NumberEnterText.getText())
                    .withClaim("password", PassEnterText.getText())
                    .sign(algorithm);
            sc.wr = new OutputStreamWriter(sc.con.getOutputStream());
            sc.wr.write(token);
            sc.wr.flush();
            sc.wr.close();

            sc.in = new BufferedReader(new InputStreamReader(sc.con.getInputStream()));
            String resp = sc.in.readLine();
            sc.in.close();
            DecodedJWT decodedJWT = JWT.decode(resp);
            String ans = decodedJWT.getClaim("answer").asString();
            if (ans.equals("true")) {
                user = new User(decodedJWT.getClaim("name").asString()+ decodedJWT.getClaim("surname").asString(),
                        decodedJWT.getClaim("phoneNumber").asString(), PassEnterText.getText());
                user.addEmail(decodedJWT.getClaim("email").asString());
                loadNextScene();
            }
            else
                errorMessageL.setText("Неверно введены данные");

        }
        catch(UnsupportedEncodingException e){
            System.out.println("Encoding beda");
        }
        catch(IOException e){
            System.out.println("IO beda");
            System.out.println(e.getMessage());
        }
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
        sc.connectToServer();
        try {
            Algorithm algorithm = Algorithm.HMAC256("password");
            String token = JWT.create()
                    .withClaim("action", "getReceivedBills")
                    .withClaim("login", user.getPhoneNumber())
                    .withClaim("password", user.getPassword())
                    .sign(algorithm);

            sc.wr = new OutputStreamWriter(sc.con.getOutputStream());
            sc.wr.write(token);
            sc.wr.flush();
            sc.wr.close();

            sc.in = new BufferedReader(new InputStreamReader(sc.con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = sc.in.readLine()) != null)
                response.append(inputLine);
            String[] resp = response.toString().split(" ");
            DecodedJWT decodedJWT = JWT.decode(resp[0]);
            String ans = decodedJWT.getClaim("answer").asString();
            if (ans.equals("success")){
                int n = decodedJWT.getClaim("number").asInt();
                for (int i = 1; i <= n; ++i) {
                    decodedJWT = JWT.decode(resp[i]);
                    user.addBillIncome(decodedJWT.getClaim("id").asInt(),
                            decodedJWT.getClaim("description").asString(),
                            decodedJWT.getClaim("sender").asString(),
                            /*decodedJWT.getClaim("date_sent").asDate(),        //TODO
                            decodedJWT.getClaim("date_paid").asDate(),*/
                            new Date(), new Date(),
                            Bill.getCurrency(decodedJWT.getClaim("currency").asString()),
                            decodedJWT.getClaim("sum").asInt());
                }
            }
        }
        catch(UnsupportedEncodingException e){
            System.out.println("Encoding beda");
            System.out.println(e.getMessage());
        }
        catch (IOException e){
            System.out.println("IO beda");
            System.out.println(e.getMessage());
        }

        BillsViewer billsViewer = new BillsViewer(user,stage);
        try {
            billsViewer.loadScene();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
