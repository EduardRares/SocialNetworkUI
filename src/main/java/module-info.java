module com.example.socialnetworkui {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires java.sql;
    requires java.desktop;

    opens com.example.socialnetworkui to javafx.fxml;
    exports com.example.socialnetworkui;
}