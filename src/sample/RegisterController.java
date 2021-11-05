package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ResourceBundle;

public class RegisterController implements Initializable
{
    @FXML
    private Button cancelButton;
    @FXML
    private TextField firstnameTextField;
    @FXML
    private TextField lastnameTextField;
    @FXML
    private TextField usernameTextField;
    @FXML
    private TextField passwordField;
    @FXML
    private TextField confirmPasswordField;
    @FXML
    private Label confirmPasswordLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {

    }

    public void registerButtonOnAction(ActionEvent event)
    {
        if(passwordField.getText().equals(confirmPasswordField.getText()))
        {
            registerUser();
        }
        else
        {
            confirmPasswordLabel.setText("Passwords do not match!");
        }
    }

    public void cancelButtonOnAction(ActionEvent event)
    {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    public void registerUser()
    {
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();

        String firstname = firstnameTextField.getText();
        String lastname = lastnameTextField.getText();
        String username = usernameTextField.getText();
        String password = passwordField.getText();

        String insertFields = "INSERT INTO users(f_name, l_name, username, password) VALUES ('";
        String insertValues = firstname + "', '" + lastname + "', '" + username+ "', '" + password + "');";
        String insertToRegister = insertFields + insertValues;

        try
        {
            Statement statement = connectDB.createStatement();
            statement.executeUpdate(insertToRegister);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            e.getCause();
        }
    }
}
