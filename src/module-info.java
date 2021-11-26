module bug.tracker
{
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens sample to javafx.fxml;

    exports sample;
}