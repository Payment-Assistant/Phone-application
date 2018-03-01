package payment.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import payment.controller.NewBillController;
import payment.model.User;

public class NewBillViewer {

    private User user;
    private Stage stage;

    public NewBillViewer(User user, Stage stage){
        this.user = user;
        this.stage = stage;
    }

    public void loadScene() throws Exception{
        NewBillController.setUser(user);
        NewBillController.setStage(stage);
        Parent root = FXMLLoader.load(getClass().getResource("structures/NewBillStructure.fxml"));
        Scene scene = new Scene(root, 335, 600);
        stage.setScene(scene);
        stage.show();
    }

}
