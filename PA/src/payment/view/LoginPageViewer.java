package payment.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class LoginPageViewer {

    public void loadScene(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("structures/LoginPageStructure.fxml"));
        Scene s = new Scene(root, 335, 600);
        Image logo = new Image("payment/view/img/logoPageLogoImg.png");
        primaryStage.setScene(s);
        primaryStage.getIcons().add(logo); //картинка на иконку
        primaryStage.show();
    }

}
