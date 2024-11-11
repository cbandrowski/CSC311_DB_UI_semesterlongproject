package viewmodel;

import dao.DbConnectivityClass;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Person;
import service.MyLogger;

import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class DB_GUI_Controller implements Initializable {

    public MenuItem ClearItem;
    @FXML
    TextField first_name, last_name, department, major, email, imageURL;
    @FXML
    ImageView img_view;
    @FXML
    MenuBar menuBar;
    @FXML
    private TableView<Person> tv;
    @FXML
    private Label statusLabel;

    @FXML
    private ComboBox<Major> combo_major;
    @FXML
    private TableColumn<Person, String> tv_major;

    @FXML
    private TableColumn<Person, Integer> tv_id;
    @FXML
    private Button addBtn, editBtn, deleteBtn,clearBtn;
    @FXML
    private MenuItem editItem, deleteItem;
    @FXML
    private TableColumn<Person, String> tv_fn, tv_ln, tv_department, tv_email;
    private final DbConnectivityClass cnUtil = new DbConnectivityClass();
    private final ObservableList<Person> data = cnUtil.getData();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            tv_id.setCellValueFactory(new PropertyValueFactory<>("id"));
            tv_fn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
            tv_ln.setCellValueFactory(new PropertyValueFactory<>("lastName"));
            tv_department.setCellValueFactory(new PropertyValueFactory<>("department"));
            tv_email.setCellValueFactory(new PropertyValueFactory<>("email"));

            // Set items for ComboBox major
            combo_major.setItems(FXCollections.observableArrayList(Major.values()));

            // Configure tv_major to display the ComboBox as a dropdown
            tv_major.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMajorAsString()));

            tv_major.setEditable(true);
            tv.setItems(data);

            // Initialize button states and listeners
            manageButtonStates();
            manageAddButtonState();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void manageButtonStates() {
        // Disable Edit and Delete buttons and menu items if no row is selected in the table
        tv.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            boolean recordSelected = newSelection != null;
            editBtn.setDisable(!recordSelected);
            deleteBtn.setDisable(!recordSelected);
            editItem.setDisable(!recordSelected);
            deleteItem.setDisable(!recordSelected);
        });
    }

    private void manageAddButtonState() {
        // Use a listener to monitor form field input and enable the "Add" button when all fields are valid
        ChangeListener<String> formListener = (observable, oldValue, newValue) -> {
            addBtn.setDisable(!isFormValid());
            clearBtn.setDisable(!isAnyFieldFilled());
        };

        first_name.textProperty().addListener(formListener);
        last_name.textProperty().addListener(formListener);
        department.textProperty().addListener(formListener);
        email.textProperty().addListener(formListener);
        imageURL.textProperty().addListener(formListener);
        // Add a listener for the ComboBox selection
        combo_major.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            addBtn.setDisable(!isFormValid());
            clearBtn.setDisable(!isAnyFieldFilled());
        });
    }

    private boolean isAnyFieldFilled() {
        // Checks if any field has text to enable the clear button
        return !first_name.getText().trim().isEmpty() ||
                !last_name.getText().trim().isEmpty() ||
                !department.getText().trim().isEmpty() ||
                combo_major.getSelectionModel().getSelectedItem() != null || // Check ComboBox selection
                !email.getText().trim().isEmpty() ||
                !imageURL.getText().trim().isEmpty();
    }

    private boolean isFormValid() {
        // Basic validation: all fields should be non-empty; email should contain "@" as a simplistic check
        return !first_name.getText().trim().isEmpty() &&
                !last_name.getText().trim().isEmpty() &&
                !department.getText().trim().isEmpty() &&
                combo_major.getSelectionModel().getSelectedItem() != null && // Check ComboBox selection
                email.getText().contains("@") &&
                !imageURL.getText().trim().isEmpty();
    }
    private void showTemporaryStatus(String message) {
        statusLabel.setText(message);
        statusLabel.setVisible(true);

        // Hide the status label after 3 seconds
        PauseTransition pause = new PauseTransition(Duration.seconds(3));
        pause.setOnFinished(event -> statusLabel.setVisible(false));
        pause.play();
    }

    @FXML
    protected void addNewRecord() {
        String validationMessage = validateFields();
        if (validationMessage.isEmpty()) {
            Major selectedMajor = combo_major.getSelectionModel().getSelectedItem();
            Person p = new Person(
                    first_name.getText(),
                    last_name.getText(),
                    department.getText(),
                    selectedMajor,
                    email.getText(),
                    imageURL.getText()
            );

            cnUtil.insertUser(p);
            p.setId(cnUtil.retrieveId(p));
            data.add(p);
            clearForm();

            // Show success message temporarily
            showTemporaryStatus("Record added successfully.");
        } else {
            showAlert("Invalid Input", validationMessage);
            showTemporaryStatus("Error: " + validationMessage); // Show validation error message temporarily
        }
    }

    @FXML
    protected void editRecord() {
        Person selectedPerson = tv.getSelectionModel().getSelectedItem();
        String validationMessage = validateFields();
        if (selectedPerson != null && validationMessage.isEmpty()) {
            Major selectedMajor = combo_major.getSelectionModel().getSelectedItem();
            Person updatedPerson = new Person(
                    selectedPerson.getId(),
                    first_name.getText(),
                    last_name.getText(),
                    department.getText(),
                    selectedMajor,
                    email.getText(),
                    imageURL.getText()
            );

            cnUtil.editUser(selectedPerson.getId(), updatedPerson);
            int index = data.indexOf(selectedPerson);
            data.set(index, updatedPerson);
            tv.getSelectionModel().select(updatedPerson);

            // Show success message temporarily
            showTemporaryStatus("Record updated successfully.");
        } else {
            showAlert("Invalid Input", validationMessage);
            showTemporaryStatus("Error: " + validationMessage); // Show validation error message temporarily
        }
    }



    private String validateFields() {
        StringBuilder errors = new StringBuilder();

        String firstNameError = validateName(first_name.getText(), "First Name");
        String lastNameError = validateName(last_name.getText(), "Last Name");
        String departmentError = validateDepartment(department.getText());
        String emailError = validateEmail(email.getText());
        String imageUrlError = validateImageURL(imageURL.getText());

        if (!firstNameError.isEmpty()) errors.append(firstNameError).append("\n");
        if (!lastNameError.isEmpty()) errors.append(lastNameError).append("\n");
        if (!departmentError.isEmpty()) errors.append(departmentError).append("\n");
        if (!emailError.isEmpty()) errors.append(emailError).append("\n");
        if (!imageUrlError.isEmpty()) errors.append(imageUrlError).append("\n");

        return errors.toString();
    }

    private String validateName(String name, String fieldName) {
        String regex = "^[A-Za-z]+$"; // Alphabetic characters only
        if (name == null || !name.matches(regex)) {
            return fieldName + " should only contain alphabetic characters. Provided: " + name;
        }
        return "";
    }

    private String validateDepartment(String department) {
        String regex = "^[A-Za-z0-9 ]+$"; // Alphanumeric and spaces allowed
        if (department == null || !department.matches(regex)) {
            return "Department should only contain alphanumeric characters and spaces. Provided: " + department;
        }
        return "";
    }

    private String validateEmail(String email) {
        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"; // Basic email format
        if (email == null || !email.matches(regex)) {
            return "Email is invalid. Provided: " + email;
        }
        return "";
    }

    private String validateImageURL(String imageURL) {
        String regex = "^[\\w-]+\\.(jpg|gif|png)$";
        if (imageURL == null || !imageURL.matches(regex)) {
            return "Image URL must be a valid link ending in .jpg, .gif, or .png. Provided: " + imageURL;
        }
        return "";
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }


    @FXML
    protected void clearForm() {
        first_name.setText("");
        last_name.setText("");
        department.setText("");
        combo_major.getSelectionModel().clearSelection(); // Clear the ComboBox selection
        email.setText("");
        imageURL.setText("");
    }


    @FXML
    protected void logOut(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/login.fxml"));
            Scene scene = new Scene(root, 900, 600);
            scene.getStylesheets().add(getClass().getResource("/css/lightTheme.css").getFile());
            Stage window = (Stage) menuBar.getScene().getWindow();
            window.setScene(scene);
            window.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void closeApplication() {
        System.exit(0);
    }

    @FXML
    protected void displayAbout() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/about.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(root, 600, 500);
            stage.setScene(scene);
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    @FXML
    protected void deleteRecord() {
        Person p = tv.getSelectionModel().getSelectedItem();
        int index = data.indexOf(p);
        cnUtil.deleteRecord(p);
        data.remove(p);
        tv.getSelectionModel().select(index);
    }

    @FXML
    protected void showImage() {
        File file = (new FileChooser()).showOpenDialog(img_view.getScene().getWindow());
        if (file != null) {
            img_view.setImage(new Image(file.toURI().toString()));
        }
    }

    @FXML
    protected void addRecord() {
        showSomeone();
    }

    @FXML
    protected void selectedItemTV(MouseEvent mouseEvent) {
        Person p = tv.getSelectionModel().getSelectedItem();
        if (p != null) {
            first_name.setText(p.getFirstName());
            last_name.setText(p.getLastName());
            department.setText(p.getDepartment());

            // Set the selected item in the ComboBox instead of setting text
            if (p.getMajor() != null) {
                combo_major.getSelectionModel().select(p.getMajor());
            } else {
                combo_major.getSelectionModel().clearSelection();
            }
            email.setText(p.getEmail());
            imageURL.setText(p.getImageURL());
        }
    }


    public void lightTheme(ActionEvent actionEvent) {
        try {
            Scene scene = menuBar.getScene();
            Stage stage = (Stage) scene.getWindow();
            stage.getScene().getStylesheets().clear();
            scene.getStylesheets().add(getClass().getResource("/css/lightTheme.css").toExternalForm());
            stage.setScene(scene);
            stage.show();
            System.out.println("light " + scene.getStylesheets());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void darkTheme(ActionEvent actionEvent) {
        try {
            Stage stage = (Stage) menuBar.getScene().getWindow();
            Scene scene = stage.getScene();
            scene.getStylesheets().clear();
            scene.getStylesheets().add(getClass().getResource("/css/darkTheme.css").toExternalForm());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void showSomeone() {
        Dialog<Results> dialog = new Dialog<>();
        dialog.setTitle("New User");
        dialog.setHeaderText("Please specify…");
        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        TextField textField1 = new TextField("Name");
        TextField textField2 = new TextField("Last Name");
        TextField textField3 = new TextField("Email ");
        ObservableList<Major> options = FXCollections.observableArrayList(Major.values());
        ComboBox<Major> comboBox = new ComboBox<>(options);
        comboBox.getSelectionModel().selectFirst();
        dialogPane.setContent(new VBox(8, textField1, textField2, textField3, comboBox));
        Platform.runLater(textField1::requestFocus);

        dialog.setResultConverter((ButtonType button) -> {
            if (button == ButtonType.OK) {
                return new Results(textField1.getText(), textField2.getText(), comboBox.getValue());
            }
            return null;
        });

        Optional<Results> optionalResult = dialog.showAndWait();
        optionalResult.ifPresent((Results results) -> {
            MyLogger.makeLog(results.fname + " " + results.lname + " " + results.major);
        });
    }


    public static enum Major {Business, CSC, CPIS}

    private static class Results {

        String fname;
        String lname;
        Major major;

        public Results(String name, String date, Major venue) {
            this.fname = name;
            this.lname = date;
            this.major = venue;
        }
    }

}