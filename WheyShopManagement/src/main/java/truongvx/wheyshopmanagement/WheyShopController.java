package truongvx.wheyshopmanagement;


import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

public class WheyShopController {

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

    public void switchForm(ActionEvent event) {
        TranslateTransition slider = new TranslateTransition();
        slider.setDuration(Duration.seconds(0.5)); // Duration should be set outside the conditions

        // Check which button triggered the event
        if (event.getSource() == side_createBtn) {
            slider.setNode(side_form);
            slider.setToX(300);  // Move the form to the right

            slider.setOnFinished((ActionEvent e) -> {
                side_alreadyAccount.setVisible(true);
                side_createBtn.setVisible(false);
            });
            slider.play();
        } else if (event.getSource() == side_alreadyAccount) {
            slider.setNode(side_form);
            slider.setToX(0);  // Reset the form to the original position

            slider.setOnFinished((ActionEvent e) -> {
                side_alreadyAccount.setVisible(false);
                side_createBtn.setVisible(true);
            });
            slider.play();
        }
    }
}