package payment.controller;

import java.net.URL;
import java.util.ResourceBundle;

import payment.model.Bill;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import payment.model.User;
import payment.view.BillInformationAndPayViewer;
import payment.view.BillInformationViewer;
import payment.view.BillsViewer;
import javafx.scene.text.Text;
import payment.view.NewBillViewer;


public class BillsController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ListView<BillsViewer.HBoxBill> listViewIncome;

    @FXML
    private ListView<BillsViewer.HBoxBill> listViewOutcome;

    @FXML
    private Text textSum;

    @FXML
    private GridPane menuPane;

    @FXML
    private Button menuButton;

    @FXML
    private Button chooseButton;

    @FXML
    private Button switchBillsButton;

    @FXML
    private Button menuReturnButton;

    private static User user;
    private static Stage stage;    //поле для переключения сцен
    private static BillsViewer billsViewer;
    private int sumOfChoosedBills;
    private int sumOfAllBills;
    private static boolean flagListenersIncome = false;
    private static boolean flagListenersOutcome = false;
    private static boolean flagToReturn = false;        //флаг, помогающий возвращаться к тем счетам, из которых мы ушли к информации
                                                        //о конкретном счете
                                                        //false - ушли из Входящих счетов
                                                        //true - ушли из Исходящих счетов
    @FXML
    void initialize() throws Exception {
        assert listViewIncome != null : "fx:id=\"listViewIncome\" was not injected: check your FXML file 'BillsStructure.fxml'.";
        billsViewer.loadData(listViewIncome, listViewOutcome, textSum, user);
        flagListenersOutcome = false;
        flagListenersIncome = false;

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

        chooseButton.setOnMouseClicked(evt ->{
            onChooseButtonClick();
        });

        if(!flagToReturn) {
            listViewOutcome.setVisible(false);
        }
        else
        {
            onSwitchBillsClick();
        }
        switchBillsButton.setOnMouseClicked(evt->onSwitchBillsClick());
    }

    private void onSelectCheckBox(BillsViewer.HBoxBill hbox){
        if(hbox.getCheckBox().isSelected()) {
            hbox.setOpacity(1);
            sumOfChoosedBills += hbox.getBill().getSum();
            BillsViewer.setSumText(textSum, sumOfChoosedBills);
        }
        else{
            hbox.setOpacity(0.5);
            sumOfChoosedBills -= hbox.getBill().getSum();
            BillsViewer.setSumText(textSum, sumOfChoosedBills);
        }
    }

    private void onSwitchBillsClick(){
        if(listViewIncome.isVisible()){
            listViewIncome.setVisible(false);
            listViewOutcome.setVisible(true);
            switchBillsButton.setText("Входящие счета");
            billsViewer.setOutcomeSumText(textSum);
        }
        else
        {
            listViewOutcome.setVisible(false);
            listViewIncome.setVisible(true);
            switchBillsButton.setText("Исходящие счета");
            billsViewer.setIncomeSumText(textSum);
        }
    }

    public void onHBoxClick(BillsViewer.HBoxBill hbox, Bill bill) throws Exception{
        if(hbox instanceof BillsViewer.HBoxIncomeBill) {
            flagToReturn = false;
            BillInformationAndPayViewer informationViewer = new BillInformationAndPayViewer(user, bill);
            informationViewer.showScene(stage);
        }
        else if(hbox instanceof BillsViewer.HBoxOutcomeBill){
            flagToReturn = true;
            BillInformationViewer informationViewer = new BillInformationViewer(user, bill);
            informationViewer.showScene(stage);
        }
    }

    private void onChooseButtonClick(){
        ListView<BillsViewer.HBoxBill> list;
        if(listViewIncome.isVisible()){
            list = listViewIncome;
            switchBillsButton.setText("Оплатить выбранные");
        }
        else{
            list = listViewOutcome;
            switchBillsButton.setText("Удалить выбранные");
        }
        sumOfAllBills = 0;
        for (BillsViewer.HBoxBill hbox: list.getItems()) {
            hbox.getCheckBox().setSelected(false);
            hbox.getChildren().add(0, hbox.getCheckBox());
            if(!flagListenersIncome || !flagListenersOutcome) {
                hbox.getCheckBox().selectedProperty().addListener(evt -> onSelectCheckBox(hbox));
            }
            hbox.setOpacity(0.5);
            sumOfAllBills += hbox.getBill().getSum();
        }
        if(listViewIncome.isVisible()){
            flagListenersIncome = true;
        }
        else{
            flagListenersOutcome = true;
        }
        chooseButton.setText("Отменить");
        chooseButton.setOnMouseClicked(evt-> onCancelButtonClick());
        switchBillsButton.setOnMouseClicked(null);
        sumOfChoosedBills = 0;
        BillsViewer.setSumText(textSum, sumOfChoosedBills);
    }

    private void onCancelButtonClick(){
        ListView<BillsViewer.HBoxBill> list;
        if(listViewIncome.isVisible()){
            list = listViewIncome;
            switchBillsButton.setText("Исходящие счета");
        }
        else{
            list = listViewOutcome;
            switchBillsButton.setText("Входящие счета");
        }
        for (BillsViewer.HBoxBill hbox: list.getItems()) {
            hbox.getChildren().remove(0);
            hbox.setOpacity(1);
        }
        chooseButton.setText("Выбрать");
        chooseButton.setOnMouseClicked(evt-> onChooseButtonClick());
        switchBillsButton.setOnMouseClicked(evt->onSwitchBillsClick());
        BillsViewer.setSumText(textSum, sumOfAllBills);
        sumOfChoosedBills = 0;
    }

    @FXML
    private void onNewBillButtonClick(){
        try{
            new NewBillViewer(user, stage).loadScene();
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void setUser(User newUser){
        user = newUser;
    }

    public static void setStage(Stage primaryStage){
        stage = primaryStage;
    }

    public void setBillViewer(BillsViewer setBillsViewer){
        billsViewer = setBillsViewer;
    }

}
