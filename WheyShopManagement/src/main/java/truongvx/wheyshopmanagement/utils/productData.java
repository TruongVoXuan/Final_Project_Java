package truongvx.wheyshopmanagement.utils;

import java.sql.Date;

public class productData {
  private Integer id;
  private String productId;
  private String productName;
  private  String type;
  private Integer stock;
  private Double price;
  private String status;
  private String image;
  private Date date;

  public productData(Integer id, String productId, String productName,String type, Integer stock, Double price, String status, String image, Date date) {
    this.id = id;
    this.productId = productId;
    this.productName = productName;
    this.type=type;
    this.stock = stock;
    this.price = price;
    this.status = status;
    this.image = image;
    this.date = date;
  }

  public productData(Integer id, String productId, String productName, Double price, String image) {
    this.id = id;
    this.productId = productId;
    this.productName = productName;
    this.price = price;
    this.image = image;
  }

  public Integer getId() {
    return id;
  }

  public String getProductId() {
    return productId;
  }

  public String getProductName() {
    return productName;
  }

  public String getType() {
    return type;
  }

  public Integer getStock() {
    return stock;
  }

  public Double getPrice() {
    return price;
  }

  public String getStatus() {
    return status;
  }

  public String getImage() {
    return image;
  }

  public Date getDate() {
    return date;
  }
}
