package payment.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import payment.controller.CardInformationController;
import payment.controller.SettingsController;
import payment.model.Bill;
import payment.model.User;

public class CardInformationViewer {

    private User user;
    private Bill bill;
    private static CardInformationController controller;

    public CardInformationViewer(User user, Bill bill){
        this.user = user;
        this.bill = bill;
    }

    public void loadScene(Stage stage) throws Exception{
        controller = new CardInformationController();
        controller.setViewer(this);
        controller.setUser(user);
        controller.setBill(bill);
        CardInformationController.setStage(stage);
        Parent root = FXMLLoader.load(getClass().getResource("structures/CardInformationStructure.fxml"));
        stage.setTitle("My Bills");

        Scene scene = new Scene(root, 335, 600);

        stage.setScene(scene);
        stage.show();
    }

}
