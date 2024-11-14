package truongvx.wheyshopmanagement;

import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class WheyShopController implements Initializable {

    @FXML
    private Hyperlink si_forgotpass;

    @FXML
    private Button si_loginBtn;

    @FXML
    private AnchorPane si_loginForm;

    @FXML
    private PasswordField si_password;

    @FXML
    private TextField si_username;

    @FXML
    private Button side_createBtn;

    @FXML
    private AnchorPane side_form;

    @FXML
    private TextField su_answer;

    @FXML
    private PasswordField su_password;

    @FXML
    private ComboBox<String> su_question;

    @FXML
    private Button su_signupBtn;

    @FXML
    private AnchorPane su_signupForm;

    @FXML
    private TextField su_username;

    @FXML
    private Button side_alreadyAccount;

    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;
    private Alert alert;

    private String[] questionList = {"Màu bạn yêu thích là gì?", "Tên thú cưng của bạn?", "Tên bộ môn bạn yêu thích?"};

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        regLquestionList();
    }

    public void regLquestionList() {
        List<String> listQ = new ArrayList<>();
        for (String data : questionList) {
            listQ.add(data);
        }
        ObservableList<String> listData = FXCollections.observableArrayList(listQ);
        su_question.setItems(listData);
    }

    public void loginBtn() {
        if (si_username.getText().isEmpty() || si_password.getText().isEmpty()) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Lỗi thông tin đăng nhập");
            alert.showAndWait();
        } else {
            connect = database.connectDB();  // Kết nối cơ sở dữ liệu

            if (connect != null) {
                String selectData = "SELECT username, password FROM employee WHERE username = ? and password = ?";
                try {
                    prepare = connect.prepareStatement(selectData);
                    prepare.setString(1, si_username.getText());
                    prepare.setString(2, si_password.getText());

                    result = prepare.executeQuery();
                    // IF SUCCESSFULLY LOGIN, THEN PROCEED TO ANOTHER FORM WHICH IS OUR MAIN FORM
                    if (result.next()) {
                        alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Information Message");
                        alert.setHeaderText(null);
                        alert.setContentText("Đăng nhập thành công");
                        alert.showAndWait();
                    } else {
                        alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Information Message");
                        alert.setHeaderText(null);
                        alert.setContentText("Thông tin nhập vào không chính xác");
                        alert.showAndWait();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Database Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Lỗi khi kết nối cơ sở dữ liệu.");
                    alert.showAndWait();
                }
            } else {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Database Connection Error");
                alert.setHeaderText(null);
                alert.setContentText("Không thể kết nối đến cơ sở dữ liệu.");
                alert.showAndWait();
            }
        }
    }

    public void regBtn() {
        if (su_username.getText().isEmpty() || su_password.getText().isEmpty() ||
            su_question.getSelectionModel().getSelectedItem() == null || su_answer.getText().isEmpty()) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Vui lòng không để trống!");
            alert.showAndWait();
        } else {
            connect = database.connectDB();  // Kết nối cơ sở dữ liệu

            if (connect != null) {
                String regData = "INSERT INTO employee (username, password, question, answer) VALUES (?, ?, ?, ?)";

                try {
                    // CHECK IF THE USERNAME IS ALREADY RECORDED
                    String checkUsername = "SELECT username FROM employee WHERE username = ?";
                    prepare = connect.prepareStatement(checkUsername);
                    prepare.setString(1, su_username.getText());
                    result = prepare.executeQuery();

                    if (result.next()) {
                        alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error Message");
                        alert.setHeaderText(null);
                        alert.setContentText(su_username.getText() + " Tài khoản đã tồn tại");
                        alert.showAndWait();
                    } else if (su_password.getText().length() < 8) {
                        alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error Message");
                        alert.setHeaderText(null);
                        alert.setContentText("Mật khẩu phải có ít nhất 8 ký tự");
                        alert.showAndWait();
                    } else {
                        prepare = connect.prepareStatement(regData);
                        prepare.setString(1, su_username.getText());
                        prepare.setString(2, su_password.getText());
                        prepare.setString(3, su_question.getSelectionModel().getSelectedItem());
                        prepare.setString(4, su_answer.getText());

                        int rowsAffected = prepare.executeUpdate();
                        if (rowsAffected > 0) {
                            alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Information Message");
                            alert.setHeaderText(null);
                            alert.setContentText("Đăng ký tài khoản thành công");
                            alert.showAndWait();

                            su_username.setText("");
                            su_password.setText("");
                            su_question.getSelectionModel().clearSelection();
                            su_answer.setText("");

                            // Chuyển form đăng ký sang đăng nhập
                            switchToLoginForm();
                        } else {
                            alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Error Message");
                            alert.setHeaderText(null);
                            alert.setContentText("Đăng ký thất bại");
                            alert.showAndWait();
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("SQL Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Lỗi khi thực hiện truy vấn cơ sở dữ liệu.");
                    alert.showAndWait();
                }
            } else {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Database Connection Error");
                alert.setHeaderText(null);
                alert.setContentText("Không thể kết nối đến cơ sở dữ liệu.");
                alert.showAndWait();
            }
        }
    }

    private void switchToLoginForm() {
        TranslateTransition slider = new TranslateTransition();
        slider.setNode(side_form);
        slider.setToX(0);  // Reset the form to the original position
        slider.setDuration(Duration.seconds(0.5));
        slider.setOnFinished((ActionEvent e) -> {
            side_alreadyAccount.setVisible(false);
            side_createBtn.setVisible(true);
        });
        slider.play();
    }

    public void switchForm(ActionEvent event) {
        TranslateTransition slider = new TranslateTransition();
        slider.setDuration(Duration.seconds(0.5));

        if (event.getSource() == side_createBtn) {
            slider.setNode(side_form);
            slider.setToX(305.5);

            slider.setOnFinished((ActionEvent e) -> {
                side_alreadyAccount.setVisible(true);
                side_createBtn.setVisible(false);
                regLquestionList();
            });
            slider.play();
        } else if (event.getSource() == side_alreadyAccount) {
            slider.setNode(side_form);
            slider.setToX(0);

            slider.setOnFinished((ActionEvent e) -> {
                side_alreadyAccount.setVisible(false);
                side_createBtn.setVisible(true);
            });
            slider.play();
        }
    }
}
