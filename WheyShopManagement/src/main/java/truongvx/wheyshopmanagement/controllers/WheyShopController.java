package truongvx.wheyshopmanagement.controllers;

import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import truongvx.wheyshopmanagement.utils.database;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class WheyShopController implements Initializable {

    @FXML
    private TextField fp_username;

    @FXML
    private TextField fp_answer;

    @FXML
    private Button fp_backBtn;

    @FXML
    private Button fp_proceedBtn;

    @FXML
    private ComboBox<?> fp_question;

    @FXML
    private AnchorPane fp_questionForm;

    @FXML
    private Button np_backBtn;

    @FXML
    private Button np_changePassBtn;

    @FXML
    private PasswordField np_confirmPassword;

    @FXML
    private AnchorPane np_newPassForm;

    @FXML
    private PasswordField np_newPassword;

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
    private Button side_alreadyAccount;

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

                        // KET NOI VS MAINfORM/ CHUYEN MAN HINH KHI DANG NHAP THANH CONG
                        Parent root = FXMLLoader.load(getClass().getResource("/truongvx/wheyshopmanagement/fxml/mainForm.fxml"));

                        Stage stage = new Stage();
                        Scene scene = new Scene(root);

                        stage.setTitle("Hệ thống quản lý bán hàng Whey Shop");
                        stage.setMinHeight(600);
                        stage.setMinWidth(1100);

                        stage.setScene(scene);
                        stage.show();

                        si_loginBtn.getScene().getWindow().hide();

                    } else {
                        alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Database Connection Error");
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
                } catch (IOException e) {
                    e.printStackTrace();
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("File Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Lỗi khi tải tệp FXML.");
                    alert.showAndWait();
                }
            } else {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Information Message");
                alert.setHeaderText(null);
                alert.setContentText("Thông tin nhập vào không chính xác");
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

    public void forgotPassQuestionList () {
        List<String> listQ = new ArrayList<>();

        for (String data: questionList) {
            listQ.add(data);
        }
            ObservableList listData = FXCollections.observableArrayList(listQ);
            fp_question.setItems(listData);
    }

    public void backLoginForm() {
        si_loginForm.setVisible(true);
        fp_questionForm.setVisible(false);

        // Xóa dữ liệu của form đăng nhập
        clearLoginForm();
    }


    public void backQuestionForm() {
        fp_questionForm.setVisible(true);
        np_newPassForm.setVisible(false);

        // Xóa dữ liệu của form đặt lại mật khẩu
        clearNewPassForm();
        clearForgotPassForm();
    }


    public  void proceedBtn() throws SQLException {
        if(fp_username.getText().isEmpty()|| fp_question.getSelectionModel().getSelectedItem() == null || fp_answer.getText().isEmpty()){

            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.getHeaderText();
            alert.setContentText("Vui lòng nhập đầy đủ thông tin!");
            alert.showAndWait();
        }
        else {
                String selectData = "SELECT username, question, answer FROM employee WHERE username = ? AND question = ? AND answer = ?";

                connect = database.connectDB();

                try{
                    prepare = connect.prepareStatement(selectData);
                    prepare.setString(1, fp_username.getText());
                    prepare.setString(2, (String)fp_question.getSelectionModel().getSelectedItem() );
                    prepare.setString(3,fp_answer.getText());

                    result = prepare.executeQuery();
                    if(result.next()){
                        np_newPassForm.setVisible(true);
                        fp_questionForm.setVisible(false);
                    }else {
                        alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error Message");
                        alert.getHeaderText();
                        alert.setContentText("Thông tin không chính xác!");
                        alert.showAndWait();

                    }

                }catch (Exception e) {e.printStackTrace();}
        }

    }

    public void changePassBtn() {
        if (np_newPassword.getText().isEmpty() || np_confirmPassword.getText().isEmpty()) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Vui lòng nhập đầy đủ thông tin!");
            alert.showAndWait();
        } else {
            if (np_newPassword.getText().equals(np_confirmPassword.getText())) {
                String date = null; // Khởi tạo giá trị date là null
                connect = database.connectDB();

                try {
                    // Lấy giá trị date từ cơ sở dữ liệu
                    String getDate = "SELECT date FROM employee WHERE username = ?";
                    prepare = connect.prepareStatement(getDate);
                    prepare.setString(1, fp_username.getText());
                    result = prepare.executeQuery();

                    if (result.next()) {
                        date = result.getString("date"); // Gán giá trị date từ cơ sở dữ liệu nếu có
                    }

                    // Câu lệnh cập nhật thông tin người dùng
                    String updatePass = "UPDATE employee SET password = ?, question = ?, answer = ?, date = ? WHERE username = ?";
                    prepare = connect.prepareStatement(updatePass);
                    prepare.setString(1, np_newPassword.getText());
                    prepare.setString(2, (String) fp_question.getSelectionModel().getSelectedItem());
                    prepare.setString(3, fp_answer.getText());
                    prepare.setString(4, date); // Truyền null nếu date không tồn tại
                    prepare.setString(5, fp_username.getText());

                    int rowsAffected = prepare.executeUpdate();

                    if (rowsAffected > 0) {
                        alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Information Message");
                        alert.setHeaderText(null);
                        alert.setContentText("Thay đổi mật khẩu thành công!");
                        alert.showAndWait();

                        // Chuyển về form đăng nhập và xóa dữ liệu cũ
                        si_loginForm.setVisible(true);
                        np_newPassForm.setVisible(false);

                        // Xóa dữ liệu các trường
                        np_confirmPassword.setText("");
                        np_newPassword.setText("");
                        fp_question.getSelectionModel().clearSelection();
                        fp_answer.setText("");
                        fp_username.setText("");
                    } else {
                        alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error Message");
                        alert.setHeaderText(null);
                        alert.setContentText("Không thể thay đổi mật khẩu. Vui lòng thử lại!");
                        alert.showAndWait();
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
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Thông tin không trùng khớp!");
                alert.showAndWait();
            }
        }
    }



    public void switchForgotPass() {
        fp_questionForm.setVisible(true);
        si_loginForm.setVisible(false);

        // Xóa dữ liệu của form quên mật khẩu
        clearForgotPassForm();

        // Tải danh sách câu hỏi bảo mật
        forgotPassQuestionList();
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

                // Xóa dữ liệu của form đăng ký
                clearSignupForm();

                // Đảm bảo các form khác được ẩn
                fp_questionForm.setVisible(false);
                si_loginForm.setVisible(true);
                np_newPassForm.setVisible(false);
            });
            slider.play();
        } else if (event.getSource() == side_alreadyAccount) {
            slider.setNode(side_form);
            slider.setToX(0);

            slider.setOnFinished((ActionEvent e) -> {
                side_alreadyAccount.setVisible(false);
                side_createBtn.setVisible(true);

                // Xóa dữ liệu của form đăng nhập
                clearLoginForm();

                // Đảm bảo các form khác được ẩn
                fp_questionForm.setVisible(false);
                si_loginForm.setVisible(true);
                np_newPassForm.setVisible(false);
            });
            slider.play();
        }
    }


    // CLEAR INFORMATION
    private void clearSignupForm() {
        su_username.setText("");
        su_password.setText("");
        su_question.getSelectionModel().clearSelection();
        su_answer.setText("");
    }

    private void clearLoginForm() {
        si_username.setText("");
        si_password.setText("");
    }

    private void clearForgotPassForm() {
        fp_username.setText("");
        fp_question.getSelectionModel().clearSelection();
        fp_answer.setText("");
    }

    private void clearNewPassForm() {
        np_newPassword.setText("");
        np_confirmPassword.setText("");
    }

}
