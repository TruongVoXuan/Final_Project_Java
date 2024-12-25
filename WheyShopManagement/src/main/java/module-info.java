module truongvx.wheyshopmanagement {
    requires javafx.controls;
    requires javafx.fxml;
  requires java.sql;


  opens truongvx.wheyshopmanagement to javafx.fxml;
    exports truongvx.wheyshopmanagement;
  exports truongvx.wheyshopmanagement.utils;
  opens truongvx.wheyshopmanagement.utils to javafx.fxml;
  exports truongvx.wheyshopmanagement.controllers;
  opens truongvx.wheyshopmanagement.controllers to javafx.fxml;


}