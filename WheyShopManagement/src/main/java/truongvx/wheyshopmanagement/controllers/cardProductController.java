package truongvx.wheyshopmanagement.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import truongvx.wheyshopmanagement.utils.productData;

import java.net.URL;
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


  public  void setData(productData prodData) {
    this.prodData =prodData;
    prod_name.setText(prodData.getProductName());
    prod_price.setText("$"+String.valueOf(prodData.getPrice()));
    String path = "File:" + prodData.getImage();
    image = new Image(path, 200, 105, false, true);
    prod_imageView.setImage(image);
  }

  private  int qty;
  public  void setQuantity() {
    spin = new SpinnerValueFactory.IntegerSpinnerValueFactory(0,100,0);
    prod_spinner.setValueFactory(spin);
  }


  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {

  }
}
