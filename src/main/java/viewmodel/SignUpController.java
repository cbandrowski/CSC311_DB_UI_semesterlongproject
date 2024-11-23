package viewmodel;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import service.UserSession;
import javafx.scene.control.TextField;


import java.awt.*;

public class SignUpController {
    public Button togglePasswordButton;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField passwordVisibleField; // TextField for visible password
    @FXML


    private boolean isPasswordVisible = false;
    public void initialize() {
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

    @FXML
    public void createNewAccount(ActionEvent actionEvent) {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        // Check if fields are empty
        if (username.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Sign-Up Failed", "Both fields must be filled.");
            return;
        }

        // Regex for validation
        String usernameRegex = "^[a-zA-Z0-9]{5,15}$"; // 5-15 alphanumeric characters
        String passwordRegex = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=]).{8,20}$";
        // Password must have 8-20 characters, at least one upper, one lower, one digit, and one special character

        StringBuilder errorMessages = new StringBuilder();

        // Validate username
        if (!username.matches(usernameRegex)) {
            errorMessages.append("Username must be 5-15 characters long and contain only letters and numbers.\n");
        }

        // Validate password
        if (!password.matches(passwordRegex)) {
            errorMessages.append("Password must:\n")
                    .append("- Be 8-20 characters long\n")
                    .append("- Contain at least one uppercase letter\n")
                    .append("- Contain at least one lowercase letter\n")
                    .append("- Contain at least one digit\n")
                    .append("- Contain at least one special character (@#$%^&+=)\n");
        }

        // Show errors if validation fails
        if (errorMessages.length() > 0) {
            showAlert(Alert.AlertType.WARNING, "Sign-Up Failed", errorMessages.toString());
            return;
        }

        // Save credentials and show success message
        UserSession.saveCredentials(username,password,"USER");
        showAlert(Alert.AlertType.INFORMATION, "Sign-Up Successful", "Account created for: " + username);

        // Navigate to the login page
        navigateToLogin(actionEvent);
    }


    private void navigateToLogin(ActionEvent actionEvent) {
        try {
            Parent loginRoot = FXMLLoader.load(getClass().getResource("/view/login.fxml"));
            Scene loginScene = new Scene(loginRoot, 900, 600);
            Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            window.setScene(loginScene);
            window.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Navigation Error", "Unable to load the login page.");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
    public void goBack(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/login.fxml"));
            Scene scene = new Scene(root, 900, 600);
            scene.getStylesheets().add(getClass().getResource("/css/lightTheme.css").toExternalForm());
            Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void togglePasswordVisibility(ActionEvent actionEvent) {
        if (isPasswordVisible) {
            // Switch to PasswordField
            passwordField.setText(passwordVisibleField.getText());
            passwordField.setVisible(true);
            passwordVisibleField.setVisible(false);
            togglePasswordButton.setText("Show"); // Icon for hidden password
        } else {
            // Switch to TextField
            passwordVisibleField.setText(passwordField.getText());
            passwordVisibleField.setVisible(true);
            passwordField.setVisible(false);
            togglePasswordButton.setText("Hidden"); // Icon for visible password
        }
        isPasswordVisible = !isPasswordVisible;

    }
}
