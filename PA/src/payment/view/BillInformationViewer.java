package payment.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import payment.controller.BillInformationController;
import payment.model.Bill;
import payment.model.User;

import java.text.SimpleDateFormat;

public class BillInformationViewer {

    private User user;
    private Bill bill;

    public BillInformationViewer(User user, Bill bill){
        this.user = user;
        this.bill = bill;
    }

    public void showScene(Stage stage) throws Exception{
        BillInformationController.setViewer(this);
        BillInformationController.setUser(user);
        BillInformationController.setStage(stage);
        Parent root = FXMLLoader.load(getClass().getResource("structures/BillInformationStructure.fxml"));
        stage.setTitle("My Bills");
        Scene scene = new Scene(root, 335, 600);
        stage.setScene(scene);
        stage.show();
    }

    public void pullInformation(Label senderLabel,
                                Label descriptionLabel,
                                Label dateLabel,
                                Label sumLabel,
                                Label sumUpperLabel,
                                Label statusLabel){
        if(senderLabel == null || descriptionLabel == null || dateLabel == null || sumLabel == null){
            System.out.println("Ups");
            return;
        }
        senderLabel.setText("Получатель: " + bill.getNameOfSender());
        descriptionLabel.setText("Описание: " + bill.getDescription());
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        dateLabel.setText("Дата выставления: " + formatter.format(bill.getDateOfSending()));
        sumLabel.setText("Сумма: " + String.valueOf(bill.getSum()) + " " + bill.getCurrency().getSymbol());
        sumUpperLabel.setText("Сумма: \n" + String.valueOf(bill.getSum()) + " " + bill.getCurrency().getSymbol());
        if(bill.getStatus()){
            statusLabel.setText("Статус: Оплачен");
        }
        else
        {
            statusLabel.setText("Статус: Не оплачен");
        }
    }

}
