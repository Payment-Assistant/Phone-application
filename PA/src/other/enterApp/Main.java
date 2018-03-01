package other.enterApp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import payment.controller.LoginPageController;
import payment.view.LoginPageViewer;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        LoginPageController.setStage(primaryStage);
        new LoginPageViewer().loadScene(primaryStage);
    }


    public static void main(String[] args) {
        launch(args);
    }
}
