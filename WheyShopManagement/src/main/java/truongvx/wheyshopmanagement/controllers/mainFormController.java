package truongvx.wheyshopmanagement.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import truongvx.wheyshopmanagement.utils.data;
import truongvx.wheyshopmanagement.utils.database;
import truongvx.wheyshopmanagement.utils.productData;

import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

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
  private ImageView inventory_ImageView;

  @FXML
  private TableColumn<?, ?> inventory_col_date;

  @FXML
  private Button inventory_col_importBtn;

  @FXML
  private TableColumn<productData, String> inventory_col_price;

  @FXML
  private TableColumn<productData, String> inventory_col_productID;

  @FXML
  private TableColumn<productData, String> inventory_col_productName;

  @FXML
  private TableColumn<productData, String> inventory_col_status;

  @FXML
  private TableColumn<productData, String> inventory_col_stock;

  @FXML
  private TableColumn<productData, String> inventory_col_type;

  @FXML
  private Button inventory_deleteBtn;

  @FXML
  private AnchorPane inventory_form;

  @FXML
  private TextField inventory_price;

  @FXML
  private TextField inventory_productID;

  @FXML
  private TextField inventory_productName;

  @FXML
  private ComboBox<?> inventory_status;

  @FXML
  private TextField inventory_stock;

  @FXML
  private TableView<productData> inventory_tableView;

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

  @FXML
  private ComboBox<?> inventory_type;

  private Alert alert;

  private Connection connect;
  private PreparedStatement prepare;
  private Statement statement;
  private ResultSet result;

  private Image image;

  public  void inventoryAddBtn() {
      if(inventory_productID.getText().isEmpty() ||  inventory_productName.getText().isEmpty() || inventory_type.getSelectionModel().getSelectedItem()==null || inventory_stock.getText().isEmpty() || inventory_price.getText().isEmpty() || inventory_status.getSelectionModel().getSelectedItem()==null || data.path == null){


        alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Thông báo lỗi");
        alert.setHeaderText (null);
        alert.setContentText ("Vui nhập nhập đầy đủ các trường!");
        alert.showAndWait ();



      }
      else {
      String checkProdID = "SELECT prod_id FROM product WHERE prod_id = '"
          + inventory_productID.getText() +"'";

      connect = database.connectDB();

      try {

        statement = connect.createStatement();
        result = statement.executeQuery(checkProdID);

        if(result.next())
        {
          alert = new Alert(Alert.AlertType.ERROR);
          alert.setTitle ("Error Message");
          alert.setHeaderText (null);
          alert.setContentText (inventory_productID.getText () + " đã có sẵn");
          alert.showAndWait();
        }
        else {
          String insertData = "INSERT INTO product "
              + "(prod_id,prod_name,type,stock,price,status,image,date) "
              +   "VALUES(?,?,?,?,?,?,?,?)";

          prepare = connect.prepareStatement(insertData);

          prepare.setString(1,inventory_productID.getText());
          prepare.setString(2,inventory_productName.getText());
          prepare.setString(3,(String) inventory_type.getSelectionModel().getSelectedItem());
          prepare.setString(4,inventory_stock.getText());
          prepare.setString(5, inventory_price.getText ());
          prepare.setString(6, (String) inventory_status.getSelectionModel() .getSelectedItem());

          String path = data.path;
          path = path.replace("\\", "\\\\");

          prepare.setString(7,path);


          //Láy ngày hiện tại
          Date date = new Date();
          java.sql.Date sqlDate = new  java.sql.Date(date.getTime());


          prepare.setString(8, String.valueOf(sqlDate));

          prepare.executeUpdate();

          alert = new Alert (Alert.AlertType. INFORMATION) ;
          alert.setTitle("Error Message");
          alert.setHeaderText (null);
          alert.setContentText ("THÊM THÀNH CÔNG!");
          alert.showAndWait ();

          inventoryShowData();
          inventoryClearBtn();
        }


      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }


  public  void inventoryUpdateBtn(){
    if(inventory_productID.getText().isEmpty() ||  inventory_productName.getText().isEmpty() || inventory_type.getSelectionModel().getSelectedItem()==null || inventory_stock.getText().isEmpty() || inventory_price.getText().isEmpty() || inventory_status.getSelectionModel().getSelectedItem()==null || data.path == null ||data.id==0){


      alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle("Thông báo lỗi");
      alert.setHeaderText (null);
      alert.setContentText ("Vui nhập nhập đầy đủ các trường!");
      alert.showAndWait ();



    }
    else {

      String path = data.path;
      path = path.replace ("\\", "\\\\");

        String updateData= "UPDATE product SET  "
            + "prod_id = '" + inventory_productID.getText() + "', prod_name = '" +inventory_productName.getText() + "',type = '" + inventory_type.getSelectionModel().getSelectedItem() + "', stock = '" + inventory_stock.getText() + "', price = '" + inventory_price.getText() + "', status ='"+ inventory_status.getSelectionModel().getSelectedItem() +"', image = '" + path + "', date = '" +data.date + "' WHERE id = " + data.id;


        connect = database.connectDB();
        try {

            alert = new Alert(Alert.AlertType.CONFIRMATION);
          alert.setTitle("Error Message");
          alert.setHeaderText (null);
          alert.setContentText ("Xác nhận cập nhập id: " + inventory_productID.getText() );
        Optional<ButtonType> option =  alert.showAndWait();

        if(option.get().equals(ButtonType.OK))
        {
          prepare = connect.prepareStatement(updateData);
          prepare.executeUpdate();

          alert = new Alert(Alert.AlertType.INFORMATION);
          alert.setTitle("Error Message") ;
          alert.setHeaderText (null);
          alert.setContentText ("Cập nhập thành công!");
          alert.showAndWait ();

          //update table view lại
          inventoryShowData();
          //xóa các trường
          inventoryClearBtn();
        }
        else {
          alert = new Alert (Alert.AlertType. ERROR) ;
          alert.setTitle("Error Message") ;
          alert.setHeaderText (null);
          alert.setContentText ("Cancelled.");
          alert.showAndWait ();
        }


        } catch (Exception e) {
          e.printStackTrace();
        }
    }
  }

  public  void inventoryClearBtn() {
    inventory_productID.setText("");
    inventory_productName.setText ("");
    inventory_type.getSelectionModel() .clearSelection();
    inventory_stock.setText ("");
    inventory_price.setText ("");
    inventory_status.getSelectionModel() .clearSelection();
    data.path = "";
    data.id=0;
    inventory_ImageView.setImage(null);
  }

  public  void inventory_DeleteBtn() {
    if(data.id==0){


      alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle("Thông báo lỗi");
      alert.setHeaderText (null);
      alert.setContentText ("Vui nhập nhập đầy đủ các trường!");
      alert.showAndWait ();



    }
    else {
      alert = new Alert(Alert.AlertType.CONFIRMATION);
      alert.setTitle("Thông báo lỗi");
      alert.setHeaderText (null);
      alert.setContentText ("Xác nhận xóa sản phẩm có id " + inventory_productID.getText());
      Optional<ButtonType> option = alert.showAndWait();
      if(option.get().equals(ButtonType.OK))
      {
        String deleteData = "DELETE FROM product WHERE id = " + data.id;

        try {
            prepare = connect.prepareStatement(deleteData);
            prepare.executeUpdate();


            alert =new Alert(Alert.AlertType.ERROR);
          alert.setTitle("Error Message");
          alert.setHeaderText (null);
          alert.setContentText ("Xóa thành công!");

          inventoryShowData();
          inventoryClearBtn();

        } catch (Exception e) {
          e.printStackTrace();
        }
      }
      else {
            alert=new Alert(Alert.AlertType.ERROR);
        alert.setTitle ("Error Message");
        alert.setHeaderText (null);
        alert.setContentText ("Cancelled");
        alert.showAndWait();
      }

    }
  }

  public  void inventoryImportBtn(){
    FileChooser openFile =  new FileChooser();
    openFile.getExtensionFilters().add(new FileChooser.ExtensionFilter("Open Image File","*png","*jpg"));
    File file = openFile.showOpenDialog(main_form.getScene().getWindow());

    if( file != null){
      data.path= file.getAbsolutePath();
      image = new Image(file.toURI().toString(), 120, 134, false, true);

      inventory_ImageView.setImage(image);
    }
  }


  public  ObservableList<productData> inventoryDataList() {
    ObservableList<productData> listData = FXCollections.observableArrayList();

    String sql = "SELECT * FROM product";

    connect = database.connectDB();
    try {
          prepare = connect.prepareStatement(sql);
          result = prepare.executeQuery();

          productData prodData;

          while (result.next()){
            prodData = new productData(result.getInt("id"), result.getString("prod_id"),result.getString("prod_name"),result.getString("type"),result.getInt("stock"), result.getDouble("price"),result.getString("status"),result.getString("image"),result.getDate("date"));

            listData.add(prodData);
          }

    } catch (Exception e) {
      e.printStackTrace();
    }
    return listData;
  }


  //Hiển thị data lên table
  private  ObservableList<productData> inventoryListData;
  public  void inventoryShowData() {
    inventoryListData = inventoryDataList();
    inventory_col_productID.setCellValueFactory(new PropertyValueFactory<>("productId"));
    inventory_col_productName.setCellValueFactory(new PropertyValueFactory<>("productName"));
    inventory_col_type.setCellValueFactory(new PropertyValueFactory<>("type"));
    inventory_col_stock.setCellValueFactory(new PropertyValueFactory<>("stock"));
    inventory_col_price.setCellValueFactory(new PropertyValueFactory<>("price"));
    inventory_col_status.setCellValueFactory(new PropertyValueFactory<>("status"));
    inventory_col_date.setCellValueFactory(new PropertyValueFactory<>("date"));

    inventory_tableView.setItems(inventoryListData);
  }

  public  void inventorySelectData() {
    productData prodData =inventory_tableView.getSelectionModel().getSelectedItem();
    int num = inventory_tableView.getSelectionModel().getSelectedIndex();

    if((num -1) < -1) return;

    inventory_productID.setText(prodData.getProductId());
    inventory_productName.setText(prodData.getProductName());
    inventory_stock.setText (String.valueOf(prodData.getStock()));
    inventory_price.setText (String.valueOf(prodData.getPrice()));

   data.path = prodData.getImage();

    String path = "File:" + prodData.getImage ();
    data.date = String.valueOf(prodData.getDate());
    data.id = prodData.getId();

    image = new Image(data.path, 120, 134, false, true);

    inventory_ImageView.setImage(image);
  }

  private  String[] typeList = {"Vitamin","Protein"};
  public  void inventoryTypeList() {

    List<String> typeL = new ArrayList<>();

    for(String data:typeList){
      typeL.add(data);
    }

    ObservableList listData = FXCollections.observableArrayList(typeL);

    inventory_type.setItems(listData);
  }


  private  String[] statusList = {"Còn hàng","Hết hàng"};

  public  void inventoryStatusList() {
    List<String> statusL = new ArrayList<> ();
    for (String data: statusList) {
      statusL.add(data);
    }
    ObservableList listData = FXCollections. observableArrayList (statusL) ;
    inventory_status.setItems (listData);
  }

  public void logout() {
    try {
      alert = new Alert(Alert.AlertType.CONFIRMATION);
      alert.setTitle("Thông báo lỗi");
      alert.setHeaderText(null);
      alert.setContentText("Bạn có chắc chắn muốn thoát?");
      Optional<ButtonType> option = alert.showAndWait();

      if (option.get().equals(ButtonType.OK)) {
        // Ẩn form main
        logout_btn.getScene().getWindow().hide();

        // Sử dụng đường dẫn chính xác đến tệp FXML
        Parent root = FXMLLoader.load(getClass().getResource("/truongvx/wheyshopmanagement/fxml/login.fxml"));
        Stage stage = new Stage();
        Scene scene = new Scene(root);

        stage.setTitle("Hệ thống quản lý bán hàng WheyShop");
        stage.setScene(scene);
        stage.show();
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public  void displayUsername() {
      String user = data.username;
      user = user.substring(0, 1).toUpperCase() + user.substring(1);
    username.setText(user);
  }

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {

    displayUsername();
    inventoryTypeList();
    inventoryStatusList();
    inventoryShowData();
  }
}
