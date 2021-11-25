package sample;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;

public class ChangeStatusController implements Initializable
{
    private ObservableList<YourReports> selectedReportRow;

    @FXML
    private Label bugReportTitleLabel;
    @FXML
    private ComboBox newStatusComboBox;

    private DatabaseConnection connectNow;
    private Connection connectDB;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        connectNow = new DatabaseConnection();
        connectDB = connectNow.getConnection();

        newStatusComboBox.getItems().removeAll(newStatusComboBox.getItems());
        newStatusComboBox.getItems().addAll("Duplicate", "By design", "Won't fix", "Can't reproduce", "Complete", "In-progress", "Blocked");
    }

    public void initChangeStatusWindowElements()
    {
        String selectTitleAndStatus = "SELECT CONCAT(name, '            ID: ', id) AS title, status FROM bug_reports WHERE id = " + selectedReportRow.get(0).getId() + ";";

        try
        {
            Statement statement = connectDB.createStatement();
            ResultSet result = statement.executeQuery(selectTitleAndStatus);

            if(result.next())
            {
                bugReportTitleLabel.setText(result.getString("title"));
                newStatusComboBox.setPromptText(result.getString("status"));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            e.getCause();
        }
    }

    public void changeReportStatusOnAction(ActionEvent event) throws IOException
    {
        String updateReportStatus = "UPDATE bug_reports SET status = '" + newStatusComboBox.getValue().toString() + "' WHERE id = " + selectedReportRow.get(0).getId() + ";";

        try
        {
            Statement statement = connectDB.createStatement();
            statement.executeUpdate(updateReportStatus);

            Stage stage = (Stage) bugReportTitleLabel.getScene().getWindow();
            stage.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            e.getCause();
        }
    }

    public void selectRow(ObservableList<YourReports> row)
    {
        selectedReportRow = row;
    }
}
