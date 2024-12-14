package truongvx.wheyshopmanagement.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import truongvx.wheyshopmanagement.utils.data;

import java.net.URL;
import java.util.ResourceBundle;

public class mainFormController  implements Initializable {
  @FXML
  private Button customers_btn;

  @FXML
  private Button dashboard_btn;

  @FXML
  private Button inventory_addBtn;

  @FXML
  private Button inventory_btn;

  @FXML
  private Button inventory_clearBtn;

  @FXML
  private ImageView inventory_col_ImageView;

  @FXML
  private TableColumn<?, ?> inventory_col_date;

  @FXML
  private Button inventory_col_importBtn;

  @FXML
  private TableColumn<?, ?> inventory_col_price;

  @FXML
  private TableColumn<?, ?> inventory_col_productID;

  @FXML
  private TableColumn<?, ?> inventory_col_productName;

  @FXML
  private TableColumn<?, ?> inventory_col_status;

  @FXML
  private TableColumn<?, ?> inventory_col_stock;

  @FXML
  private TableColumn<?, ?> inventory_col_type;

  @FXML
  private Button inventory_deleteBtn;

  @FXML
  private AnchorPane inventory_form;

  @FXML
  private TableView<?> inventory_tableView;

  @FXML
  private Button inventory_updateBtn;

  @FXML
  private Button logout_btn;

  @FXML
  private AnchorPane main_form;

  @FXML
  private Button menu_btn;

  @FXML
  private Label username;

  public  void displayUsername() {
      String user = data.username;
      user = user.substring(0, 1).toUpperCase() + user.substring(1);
    username.setText(user);
  }

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {

    displayUsername();

  }
}
