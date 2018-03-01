package other.scene1;

import payment.model.Bill;
import javafx.application.Application;
import javafx.stage.Stage;
import payment.model.User;
import payment.view.BillsViewer;

import java.util.Date;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        User user = new User("8(985)617-08-48", "123456");
        user.addBillIncome("1", "Кошка лялялялялялялялял", "ООО Мармелад", new Date(), new Date(),
                Bill.Currency.RUBLE, 1000 );
        user.addBillIncome("2", "Собака", "ООО Мармелад", new Date(), new Date(),
                Bill.Currency.RUBLE, 2000);
        user.addBillIncome("3", "Попугай", "ООО Мармелад", new Date(), new Date(),
                Bill.Currency.RUBLE, 4000);

        user.addBillOutcome("4", "Стрижка", "ООО Кат-кат", new Date(), new Date(),
                Bill.Currency.RUBLE, 600);
        user.addBillOutcome("5", "Парковка", "ООО Пенек", new Date(), new Date(),
                Bill.Currency.RUBLE, 500);


        BillsViewer billsViewer = new BillsViewer(user);
        billsViewer.loadScene(primaryStage);
    }


    public static void main(String[] args) {
        launch(args);
    }

}
