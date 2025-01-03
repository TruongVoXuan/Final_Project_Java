package truongvx.wheyshopmanagement.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import truongvx.wheyshopmanagement.utils.data;
import truongvx.wheyshopmanagement.utils.database;
import truongvx.wheyshopmanagement.utils.productData;

import java.net.URL;
import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.ResourceBundle;

public class cardProductController implements Initializable {


  @FXML
  private AnchorPane card_form;

  @FXML
  private Button prod_addBtn;

  @FXML
  private ImageView prod_imageView;

  @FXML
  private Label prod_name;

  @FXML
  private Label prod_price;

  @FXML
  private Spinner<Integer> prod_spinner;

  private productData prodData;

  private Image image;

  private SpinnerValueFactory<Integer> spin;

  private  String prodID;

  private  String type;
  private String prod_image;
  private String prod_date;


  private Connection connect;
  private PreparedStatement prepare;
  private ResultSet result;


  private Alert alert;



  public  void setData(productData prodData) {
    this.prodData =prodData;

    prod_image = prodData.getImage();
    prod_date = String.valueOf(prodData.getDate());
    type = prodData.getType();
    prodID =prodData.getProductId();
    prod_name.setText(prodData.getProductName());
    prod_price.setText("$"+String.valueOf(prodData.getPrice()));
    String path = "File:" + prodData.getImage();
    image = new Image(path, 200, 105, false, true);
    prod_imageView.setImage(image);
    pr = prodData.getPrice();
  }

  private  int qty;
  public  void setQuantity() {
    spin = new SpinnerValueFactory.IntegerSpinnerValueFactory(0,100,0);
    prod_spinner.setValueFactory(spin);
  }



  private  double totalP;
  private  double pr;
  public  void addBtn() {

    mainFormController mForm = new mainFormController();
    mForm.customerID();

    qty = prod_spinner.getValue();
    String check ="";
    String checkAvailable = "SELECT status FROM product WHERE prod_id ='" + prodID +"'";

    connect = database.connectDB();

    try{

      int checkStck =0;
      String checkStock ="SELECT stock FROM product WHERE prod_id = '"
          +prodID +"'";

      prepare = connect.prepareStatement(checkStock);
      result= prepare.executeQuery();

      if(result.next())
      {
        checkStck = result.getInt("stock");
      }

      if(checkStck ==0) {

        String updateStock = "UPDATE product SET prod_name = '"
            +prod_name.getText() +"' , type = '"
            + type +"', stock =  0, price =" + pr + ", status = 'Hết hàng', image = '"
            +prod_image +"', date = '"
            + prod_date + "' WHERE prod_id = '"
            + prodID +"'";

        prepare = connect.prepareStatement(updateStock);
        prepare.executeUpdate();
      }


      prepare = connect.prepareStatement(checkAvailable);
      result= prepare.executeQuery();

      if(result.next())
      {
        check= result.getString("status");

      }

      if(!check.equals("Còn hàng") || qty ==0){
        alert=new Alert(Alert.AlertType.ERROR);
        alert.setTitle ("Error Message");
        alert.setHeaderText (null);
        alert.setContentText ("Lỗi!!!");
        alert.showAndWait();
      }
      else {


        if(checkStck <qty)
        {

          alert = new Alert (Alert.AlertType. ERROR) ;
          alert.setTitle ("Error Message");
          alert.setHeaderText (null);
          alert.setContentText ("Sản phẩm đã hết hàng!");
          alert.showAndWait();
        }

        else {

          prod_image= prod_image.replace("\\", "\\\\");

          String insertData ="INSERT INTO customer "
              + "(customer_id, prod_id ,prod_name,type , quantity, price, date,image, em_username)"
              + "VALUES(?,?,?,?,?,?,?,?,?)";
          prepare = connect.prepareStatement(insertData);
          prepare.setString(1,String.valueOf(data.cID));
          prepare.setString(2,prodID);
          prepare.setString (3, prod_name.getText ());
          prepare.setString(4,type);
          prepare.setString(5, String.valueOf(qty));

          totalP = (qty*pr);
          prepare.setString(6,String.valueOf(totalP));

          Date date = new Date();
          java.sql.Date sqlDate = new java.sql. Date(date.getTime ());
          prepare.setString(7, String.valueOf(sqlDate));

          prepare.setString(8,prod_image);

          prepare.setString (9, data.username) ;

          prepare. executeUpdate () ;

          int upStock = checkStck -qty;



          String updateStock = "UPDATE product SET prod_name = '"
              +prod_name.getText() +"' , type = '"
              + type +"', stock =  " + upStock + ", price =" + pr + ", status = '"
              + check +"', image = '"
              +prod_image +"', date = '"
              + prod_date + "' WHERE prod_id = '"
              + prodID +"'";

          prepare = connect.prepareStatement(updateStock);
          prepare.executeUpdate();


          alert = new Alert (Alert.AlertType. INFORMATION) ;
          alert.setTitle ("Information Message");
          alert.setHeaderText (null);
          alert.setContentText ("Thêm thành công!");
          alert.showAndWait();


          mForm.menuGetTotal();
        }


      }

    } catch (Exception e) {
      e.printStackTrace();
    }
  }




  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    setQuantity();
  }
}
