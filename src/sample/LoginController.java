package sample;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.*;

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

        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();

        String verifyLogin = "SELECT count(1) FROM users WHERE username = '" + username.getText() + "' AND password = '" + password.getText() + "';";

        try
        {
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(verifyLogin);

            while(queryResult.next())
            {
                if(queryResult.getInt(1) == 1)
                {
                    m.changeScene("bugTracker.fxml");   //Change to application scene
                }
                else if(username.getText().isEmpty() && password.getText().isEmpty())
                {
                    loginError.setText("Please enter a username and password!");
                }
                else
                {
                    loginError.setText("Invalid login credentials!");
                }
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
            e.getCause();
        }
    }

    public void exitLogin()
    {
        Platform.exit();
    }
}
