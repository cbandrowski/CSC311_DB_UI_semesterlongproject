package viewmodel;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import service.UserSession;


public class LoginController {
    public PasswordField passwordField;
    public ImageView backgroundImage;
    @FXML
    private GridPane root_pane;

    @FXML
    private TextField usernameField;

    
    @FXML
    private TextField passwordVisibleField; // Visible password field
    @FXML
    private Button togglePasswordButton; // Button to toggle visibility

    private boolean isPasswordVisible = false;


    public void initialize() {
        root_pane.setBackground(new Background(
                new BackgroundImage(
                        new Image(getClass().getResource("/images/coffeeLogin.jpg").toExternalForm()),
                        BackgroundRepeat.NO_REPEAT,
                        BackgroundRepeat.NO_REPEAT,
                        BackgroundPosition.CENTER,
                        new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true)
                )
        ));

        root_pane.setOpacity(0);
        FadeTransition fadeOut2 = new FadeTransition(Duration.seconds(10), root_pane);
        fadeOut2.setFromValue(0);
        fadeOut2.setToValue(1);
        fadeOut2.play();
        // Synchronize PasswordField and TextField
        passwordField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!passwordVisibleField.isFocused()) {
                passwordVisibleField.setText(newValue);
            }
        });

        passwordVisibleField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!passwordField.isFocused()) {
                passwordField.setText(newValue);
            }
        });


    }

    private static BackgroundImage createImage(String url) {
        return new BackgroundImage(
                new Image(url),
                BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT,
                new BackgroundPosition(Side.LEFT, 0, true, Side.BOTTOM, 0, true),
                new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, true, true, false, true));
    }

    @FXML
    public void login(ActionEvent actionEvent) {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Login Failed", "Both fields must be filled.");
            return;
        }

        boolean usernameCorrect = UserSession.isUsernameCorrect(username);
        boolean passwordCorrect = UserSession.isPasswordCorrect(username, password);

        if (!usernameCorrect && !passwordCorrect) {
            showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid Username and Password.");
        } else if (usernameCorrect && !passwordCorrect) {
            showAlert(Alert.AlertType.ERROR, "Login Failed", "Incorrect Password.");
        } else if (!usernameCorrect) {
            showAlert(Alert.AlertType.ERROR, "Login Failed", "Username does not exist.");
        } else {
            // Login successful
            UserSession.saveCredentials(username, password,"USER"); // Save session details
            loadView("/view/db_interface_gui.fxml", actionEvent);
        }
    }


    @FXML
    public void signUp(ActionEvent actionEvent) {
        loadView("/view/signUp.fxml", actionEvent);
    }

    private void loadView(String fxmlPath, ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            Scene scene = new Scene(root, 900, 600);
            Stage window = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void togglePasswordVisibility(ActionEvent event) {
        if (isPasswordVisible) {
            // Switch to hidden PasswordField
            passwordField.setText(passwordVisibleField.getText());
            passwordField.setVisible(true);
            passwordVisibleField.setVisible(false);
            togglePasswordButton.setText("Show");
        } else {
            // Switch to visible TextField
            passwordVisibleField.setText(passwordField.getText());
            passwordVisibleField.setVisible(true);
            passwordField.setVisible(false);
            togglePasswordButton.setText("Hide");
        }
        isPasswordVisible = !isPasswordVisible;
    }

}