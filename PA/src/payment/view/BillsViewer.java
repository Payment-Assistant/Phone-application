package payment.view;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import payment.controller.ServerConnection;
import payment.model.Bill;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import payment.controller.BillsController;
import payment.model.User;
import sun.font.StandardGlyphVector;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Date;

public class BillsViewer {

    private User user;
    private Stage stage;
    private static BillsController billsController;
    private int sumOfIncomeBills;
    private int sumOfOutcomeBills;
    private ServerConnection sc;

    public BillsViewer(User user, Stage stage) {
        this.user = user;
        this.stage = stage;
        sc = new ServerConnection();
    }

    public static class HBoxBill extends HBox {

        private Bill bill;
        private User user;
        private Stage stage;
        private CheckBox checkBox;
        private Label label;

        HBoxBill(User user, Bill bill, Stage stage) {
            this.user = user;
            this.bill = bill;
            this.stage = stage;
            Pane pane = new Pane();

            checkBox = new CheckBox();
            checkBox.getStylesheets().add((getClass().getResource("css/CheckBoxStyle.css")).toExternalForm());

            label = new Label(checkDescriptionLength(bill.getDescription()));
            label.setFont(Font.font(20));

            super.setAlignment(Pos.CENTER);
            super.getChildren().addAll(label, pane);

            HBox.setHgrow(pane, Priority.ALWAYS);

            setOnMouseClicked(evt -> {
                try {
                    billsController.onHBoxClick(this, bill);
                } catch (Exception e) {
                    System.out.println("Иди сюды");
                    System.out.println(e.getMessage());
                }
            });
        }

        public CheckBox getCheckBox() {
            return checkBox;
        }

        public Bill getBill(){
            return bill;
        }

        private String checkDescriptionLength(String description) {
            if (description.length() > 21) {
                description = description.substring(0, 18) + "...";
            }
            return description;
        }
    }

    public static class HBoxIncomeBill extends HBoxBill{

        HBoxIncomeBill(User user, Bill bill, Stage stage){
            super(user, bill, stage);
            Button button = new Button(String.valueOf(bill.getSum()) + " " + bill.getCurrency().getSymbol());
            button.getStylesheets().add(getClass().getResource("css/ButtonsStyle.css").toExternalForm());
            getChildren().add(2, button);
            button.setOnMouseClicked(e->{
                try {
                    new CardInformationViewer(user, bill).loadScene(stage);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            });
        }

    }

    public static class HBoxOutcomeBill extends HBoxBill{

        HBoxOutcomeBill(User user, Bill bill, Stage stage){
            super(user, bill, stage);
            Label label = new Label(String.valueOf(bill.getSum()) + " " + bill.getCurrency().getSymbol());
            label.setFont(Font.font(15));
            getChildren().add(2, label);
        }

    }

    public void loadData(ListView<HBoxBill> listViewIncome, ListView<HBoxBill> listViewOutCome, Text textSum, User user) throws Exception{
        loadIncomeData(listViewIncome, textSum, user);
        loadOutcomeData(listViewOutCome, textSum, user);
        setIncomeSumText(textSum);
    }

    private void loadIncomeData(ListView<HBoxBill> listView, Text textSum, User user) throws Exception{
        listView.setFixedCellSize(45);

        sc.connectToServer();
        Algorithm algorithm = Algorithm.HMAC256("password");
        String token = JWT.create()
                .withClaim("action","getReceivedBills")
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
        DecodedJWT decodedJWT = JWT.decode(re[0]);
        String ans = decodedJWT.getClaim("answer").asString();
        if (ans.equals("success")) {
            int n = decodedJWT.getClaim("number").asInt();
            for (int i = 1; i <= n; ++i) {
                decodedJWT = JWT.decode(re[i]);
                Bill b = new Bill(decodedJWT.getClaim("id").asInt(),
                        decodedJWT.getClaim("description").asString(),
                        decodedJWT.getClaim("sender").asString(),
                        /*decodedJWT.getClaim("date_sent").asDate(),                //TODO
                        decodedJWT.getClaim("date_paid").asDate(),*/
                        new Date(), new Date(),
                        Bill.getCurrency(decodedJWT.getClaim("currency").asString()),
                        decodedJWT.getClaim("sum").asInt());
                listView.getItems().add(new HBoxIncomeBill(user, b, stage));
                sumOfIncomeBills += b.getSum();
            }
        }
    }

    private void loadOutcomeData(ListView<HBoxBill> listView, Text textSum, User user) throws Exception{
        listView.setFixedCellSize(45);
        sc.connectToServer();
        Algorithm algorithm = Algorithm.HMAC256("password");
        String token = JWT.create()
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
        DecodedJWT decodedJWT = JWT.decode(re[0]);
        String ans = decodedJWT.getClaim("answer").asString();
        if (ans.equals("success")) {
            int n = decodedJWT.getClaim("number").asInt();
            for (int i = 1; i <= n; ++i) {
                decodedJWT = JWT.decode(re[i]);
                Bill b = new Bill(decodedJWT.getClaim("id").asInt(),
                        decodedJWT.getClaim("description").asString(),
                        decodedJWT.getClaim("sender").asString(),
                        /*decodedJWT.getClaim("date_sent").asDate(),                //TODO
                        decodedJWT.getClaim("date_paid").asDate(),*/
                        new Date(), new Date(),
                        Bill.getCurrency(decodedJWT.getClaim("currency").asString()),
                        decodedJWT.getClaim("sum").asInt());
                listView.getItems().add(new HBoxIncomeBill(user, b, stage));
                sumOfIncomeBills += b.getSum();
            }
        }
    }

    public static void setSumText(Text textSum, int sum){
        textSum.setText("Общая сумма:\n" + sum + "руб");
    }

    public void setIncomeSumText(Text textSum){textSum.setText("Общая сумма:\n" + sumOfIncomeBills + "руб");}

    public void setOutcomeSumText(Text textSum){textSum.setText("Общая сумма:\n" + sumOfOutcomeBills + "руб");}

    public void loadScene() throws Exception{
        billsController = new BillsController();
        billsController.setBillViewer(this);
        billsController.setUser(user);
        BillsController.setStage(stage);
        Parent root = FXMLLoader.load(getClass().getResource("structures/BillsStructure.fxml"));
        stage.setTitle("My Bills");

        Scene scene = new Scene(root, 335, 600);

        stage.setScene(scene);
        stage.show();
    }

}
