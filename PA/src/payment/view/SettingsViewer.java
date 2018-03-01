package payment.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import payment.controller.BillsController;
import payment.controller.SettingsController;
import payment.model.User;

public class SettingsViewer {

    private User user;
    private SettingsController controller;

    public SettingsViewer(User user){
        this.user = user;
    }

    public void loadScene(Stage stage) throws Exception{
        controller = new SettingsController();
        controller.setBillViewer(this);
        controller.setUser(user);
        SettingsController.setStage(stage);
        Parent root = FXMLLoader.load(getClass().getResource("structures/SettingsStructure.fxml"));
        stage.setTitle("My Bills");

        Scene scene = new Scene(root, 335, 600);

        stage.setScene(scene);
        stage.show();
    }

}
