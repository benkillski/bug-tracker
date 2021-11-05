package sample;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;

public class LoginController
{
    @FXML
    private Button loginButton;
    @FXML
    private Label loginError;
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;

    public void userLogin(ActionEvent event) throws IOException
    {
        checkLogin();
    }

    public void checkLogin() throws IOException
    {
        Main m = new Main();
        if(username.getText().toString().equals("username") && password.getText().toString().equals("password"))
        {
            loginError.setText("Login Success!");

            m.changeScene("bugTracker.fxml");   //Change to application scene
        }
        else if(username.getText().isEmpty() && password.getText().isEmpty())
        {
            loginError.setText("Please enter your login credentials");
        }
        else
        {
            loginError.setText("Wrong username or password!");
        }
    }

    public void exitLogin()
    {
        Platform.exit();
    }
}
