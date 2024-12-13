module com.example.cs {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.sql;
    requires mysql.connector.j;
    requires fontawesomefx;


    opens com.example.cs262 to javafx.fxml;
    exports com.example.cs262;
    exports com.example.cs262.products;
    opens com.example.cs262.products to javafx.fxml;
    exports com.example.cs262.gui;
    opens com.example.cs262.gui to javafx.fxml;
    exports com.example.cs262.model;
    opens com.example.cs262.model to javafx.fxml;
}