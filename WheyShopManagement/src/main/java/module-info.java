module truongvx.wheyshopmanagement {
    requires javafx.controls;
    requires javafx.fxml;
  requires java.sql;


  opens truongvx.wheyshopmanagement to javafx.fxml;
    exports truongvx.wheyshopmanagement;
}