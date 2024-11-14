module truongvx.wheyshopmanagement {
    requires javafx.controls;
    requires javafx.fxml;


    opens truongvx.wheyshopmanagement to javafx.fxml;
    exports truongvx.wheyshopmanagement;
}