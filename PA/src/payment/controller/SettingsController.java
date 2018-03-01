package payment.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import payment.model.User;
import payment.view.SettingsViewer;

public class SettingsController {

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField phoneTextField;

    @FXML
    private TextField emailTextField;

    @FXML
    private Button checkEmailButton;

    @FXML
    private PasswordField oldPasswordField;

    @FXML
    private PasswordField newPasswordField;

    @FXML
    private PasswordField againPasswordField;

    @FXML
    private Button changePasswordField;

    @FXML
    private Button saveButton;

    @FXML
    private GridPane menuPane;

    @FXML
    private Button menuReturnButton;

    private static User user;
    private static Stage stage;    //поле для переключения сцен
    private static SettingsViewer viewer;

    @FXML
    void initialize() throws Exception{
        nameTextField.setText(user.getName());
        phoneTextField.setText(user.getPhoneNumber());
        if(user.getEmail() != null){
            emailTextField.setText(user.getEmail());
            checkEmailButton.setText(user.getEmail());
        }
    }

    public void onSettingsButtonClick(){
        
    }

    public void setUser(User newUser){
        user = newUser;
    }

    public static void setStage(Stage primaryStage){
        stage = primaryStage;
    }

    public void setBillViewer(SettingsViewer newViewer){
        viewer = newViewer;
    }
}
