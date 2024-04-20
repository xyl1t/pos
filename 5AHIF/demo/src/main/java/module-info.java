module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.mongodb.bson;
    requires org.mongodb.driver.core;
    requires org.mongodb.driver.sync.client;

    requires org.controlsfx.controls;

    opens org.example.demo to javafx.fxml;
    exports org.example.demo;
}
