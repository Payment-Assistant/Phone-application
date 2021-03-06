package payment.view;

import payment.model.Bill;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import payment.controller.BillInformationAndPayController;
import payment.model.User;

import java.text.SimpleDateFormat;

public class BillInformationAndPayViewer {

    private User user;
    private Bill bill;

    public BillInformationAndPayViewer(User user, Bill bill){
        this.user = user;
        this.bill = bill;
    }

    public void showScene(Stage stage) throws Exception{
        BillInformationAndPayController.setViewer(this);
        BillInformationAndPayController.setUser(user);
        BillInformationAndPayController.setStage(stage);
        Parent root = FXMLLoader.load(getClass().getResource("structures/BillInformationAndPayStructure.fxml"));
        stage.setTitle("My Bills");
        Scene scene = new Scene(root, 335, 600);
        stage.setScene(scene);
        stage.show();
    }

    public void pullInformation(Label senderLabel,
                                 Label descriptionLabel,
                                 Label dateLabel,
                                 Label sumLabel,
                                 Label sumUpperLabel){
        if(senderLabel == null || descriptionLabel == null || dateLabel == null || sumLabel == null){
            System.out.println("Ups");
            return;
        }
        senderLabel.setText("Отправитель: " + bill.getNameOfSender());
        descriptionLabel.setText("Описание: " + bill.getDescription());
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        dateLabel.setText("Дата выставления: " + formatter.format(bill.getDateOfSending()));
        sumLabel.setText("Сумма: " + String.valueOf(bill.getSum()) + " " + bill.getCurrency().getSymbol());
        sumUpperLabel.setText("Сумма: \n" + String.valueOf(bill.getSum()) + " " + bill.getCurrency().getSymbol());
    }

}
