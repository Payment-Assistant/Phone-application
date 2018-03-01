package payment.view;

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

public class BillsViewer {

    private User user;
    private static BillsController billsController;
    private int sumOfIncomeBills;
    private int sumOfOutcomeBills;

    public BillsViewer(User user) {
        this.user = user;
    }

    public static class HBoxBill extends HBox {

        private Bill bill;
        private CheckBox checkBox;
        private Label label;

        HBoxBill(Bill bill) {
            this.bill = bill;
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
                    System.out.println(e.getStackTrace());
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

        HBoxIncomeBill(Bill bill){
            super(bill);
            Button button = new Button(String.valueOf(bill.getSum()) + " " + bill.getCurrency().getSymbol());
            button.getStylesheets().add(getClass().getResource("css/ButtonsStyle.css").toExternalForm());
            getChildren().add(2, button);
        }

    }

    public static class HBoxOutcomeBill extends HBoxBill{

        HBoxOutcomeBill(Bill bill){
            super(bill);
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
        for (Bill b:
                user.getBillsIncome()) {
            listView.getItems().add(new HBoxIncomeBill(b));
            sumOfIncomeBills += b.getSum();
        }
    }

    private void loadOutcomeData(ListView<HBoxBill> listView, Text textSum, User user) throws Exception{
        listView.setFixedCellSize(45);
        for (Bill b:
                user.getBillsOutcome()) {
            listView.getItems().add(new HBoxOutcomeBill(b));
            sumOfOutcomeBills += b.getSum();
        }
    }

    public static void setSumText(Text textSum, int sum){
        textSum.setText("Общая сумма:\n" + sum + "руб");
    }

    public void setIncomeSumText(Text textSum){textSum.setText("Общая сумма:\n" + sumOfIncomeBills + "руб");}

    public void setOutcomeSumText(Text textSum){textSum.setText("Общая сумма:\n" + sumOfOutcomeBills + "руб");}

    public void loadScene(Stage primaryStage) throws Exception{
        billsController = new BillsController();
        billsController.setBillViewer(this);
        billsController.setUser(user);
        BillsController.setStage(primaryStage);
        Parent root = FXMLLoader.load(getClass().getResource("structures/BillsStructure.fxml"));
        primaryStage.setTitle("My Bills");

        Scene scene = new Scene(root, 335, 600);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

}
