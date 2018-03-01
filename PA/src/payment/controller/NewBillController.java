package payment.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import payment.model.Bill;
import payment.model.User;
import payment.view.BillsViewer;
import payment.view.SettingsViewer;

import java.io.*;
import java.time.LocalDate;
import java.util.Date;

public class NewBillController {

    @FXML
    public TextField phoneTextField;
    @FXML
    public Label Message;

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
            new BillsViewer(user, stage).loadScene();
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    @FXML
    private void onToSendBillButtonClick(){
        createNewBill();
    }

    double sum;
    ServerConnection sc = new ServerConnection();
    private void createNewBill(){

        sc.connectToServer();
        try {
            Algorithm algorithm = Algorithm.HMAC256("password");
            String token = JWT.create()
                    .withClaim("action", "createBill")
                    .withClaim("login", user.getPhoneNumber()) //login == sender
                    .withClaim("receiver", phoneTextField.getText())
                    .withClaim("password", user.getPassword())
                    .withClaim("dateSent", LocalDate.now().toString()) //ВАЖНО! дату предаем как строку, иначе говно
                    .withClaim("currency", "RUB")
                    .withClaim("sum", sumTextField.getText())
                    .withClaim("description", descriptionTextArea.getText())
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
            if (ans.equals("success")) {
                Message.setText("Счет отправлен!");
                sc.connectToServer();
                token = JWT.create()
                        .withClaim("action","getSentBills")
                        .withClaim("login", user.getPhoneNumber())
                        .withClaim("password",user.getPassword())
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
                String[] re = response.toString().split(" ");
                decodedJWT = JWT.decode(re[0]);
                if (ans.equals("success")) {
                    int n = decodedJWT.getClaim("number").asInt();
                    for (int i = 1; i <= n; ++i) {
                        decodedJWT = JWT.decode(re[i]);
                        user.addBillOutcome(decodedJWT.getClaim("id").asInt(),
                                decodedJWT.getClaim("description").asString(),
                                decodedJWT.getClaim("sender").asString(),
                                /*decodedJWT.getClaim("date_sent").asDate(),                //TODO
                                decodedJWT.getClaim("date_paid").asDate(),*/
                                new Date(), new Date(),
                                Bill.getCurrency(decodedJWT.getClaim("currency").asString()),
                                decodedJWT.getClaim("sum").asInt());
                    }
                }
                try{
                    Thread.sleep(5000);
                    new BillsViewer(user, stage).loadScene();
                }
                catch(Exception e){
                    System.out.println(e.getMessage());
                }
            }
            if (ans.equals("unknownLogin")) {
                Message.setText("Неверный логин получателя");
                Message.setTextFill(Color.RED);
            }
        }
        catch(UnsupportedEncodingException e){
            System.out.println("Беда");
        }
        catch (IOException e) {
            System.out.println("IO beda");
        }
    }



    public void onSettingsButtonClick(){
        try {
            new SettingsViewer(user).loadScene(stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setUser(User newUser){user = newUser;}

    public static void setStage(Stage newStage){stage = newStage;}

}
