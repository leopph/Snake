module hu.alkfejl {
    requires javafx.controls;
    requires javafx.fxml;

    opens hu.alkfejl to javafx.fxml;
    exports hu.alkfejl;
}