package viewmodel;

import com.azure.storage.blob.BlobClient;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import dao.DbConnectivityClass;
import javafx.scene.layout.*;

import dao.StorageUploader;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Person;
import service.MyLogger;
import service.UserSession;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class DB_GUI_Controller implements Initializable {
    public MenuItem CopyItem;
    public Label userNameLabel;
    public PieChart deparmtmentPieChart;
    public Label studentCountLabel;
    public ImageView backgroundImage;
    StorageUploader store = new StorageUploader();
    @FXML
    ProgressBar progressBar;

    public MenuItem ClearItem;
    @FXML
    TextField first_name, last_name, position, department, email, imageURL;
    @FXML
    ImageView img_view;
    @FXML
    MenuBar menuBar;
    @FXML
    private TableView<Person> tv;
    @FXML
    private Label statusLabel;

    @FXML
    private ComboBox<Department> combo_department;
    @FXML
    private TableColumn<Person, Department> tv_department;

    @FXML
    private TableColumn<Person, Integer> tv_id;
    @FXML
    private Button addBtn, editBtn, deleteBtn, clearBtn;
    @FXML
    private MenuItem editItem, deleteItem;
    @FXML
    private TableColumn<Person, String> tv_fn, tv_ln, tv_position, tv_email;
    private final DbConnectivityClass cnUtil = new DbConnectivityClass();
    private final ObservableList<Person> data = cnUtil.getData();
    UserSession currentSession = UserSession.getCurrentSession();
    @FXML
    private AnchorPane anchorPane;



    public void initialize(URL url, ResourceBundle resourceBundle) {
        applyDarkTheme();
        initializePieChart(); // Initialize the PieChart
        initializeStudentCountLabel(); // Initialize the student count label
        initializeButtons();
        initializeTableView();
        initializeComboBox();
        initializeSession();
        manageMenuButton();
        tv.setOnMouseClicked(this::selectedItemTV);


        // Manage button states based on input and selection
        manageButtonStates();
        manageAddButtonState();
        anchorPane.setBackground(new Background(
                new BackgroundImage(
                        new Image(getClass().getResource("/images/coffeeInterface.png").toExternalForm()),
                        BackgroundRepeat.NO_REPEAT,
                        BackgroundRepeat.NO_REPEAT,
                        BackgroundPosition.CENTER,
                        new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true)
                )
        ));

    }
    private void initializeButtons(){
        // Keyboard shortcuts for menu items
        editItem.setAccelerator(KeyCombination.keyCombination("Ctrl+E"));
        deleteItem.setAccelerator(KeyCombination.keyCombination("Ctrl+D"));
        ClearItem.setAccelerator(KeyCombination.keyCombination("Ctrl+R"));
        CopyItem.setAccelerator(KeyCombination.keyCombination("Ctrl+C"));

        // Map menu items to actions
        editItem.setOnAction(event -> editRecord());
        deleteItem.setOnAction(event -> deleteRecord());
        ClearItem.setOnAction(event -> clearForm());
        CopyItem.setOnAction(event -> copySelectedRecord());
    }
    private void applyDarkTheme() {
        // Ensure the Scene is available
        Platform.runLater(() -> {
            Scene scene = menuBar.getScene();
            if (scene != null) {
                // Add the dark theme CSS
                scene.getStylesheets().add(getClass().getResource("/css/darktheme.css").toExternalForm());
            } else {
                System.err.println("Scene is not ready. Dark theme was not applied.");
            }
        });
    }
    private void initializeTableView() {
        try {
            // Set up columns for TableView
            tv_id.setCellValueFactory(new PropertyValueFactory<>("id"));
            tv_fn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
            tv_ln.setCellValueFactory(new PropertyValueFactory<>("lastName"));
            tv_position.setCellValueFactory(new PropertyValueFactory<>("Position"));
            tv_email.setCellValueFactory(new PropertyValueFactory<>("email"));
            tv_department.setCellValueFactory(cellData ->
                    new SimpleObjectProperty<>(cellData.getValue().getDepartment())
            );
            tv_department.setCellFactory(ComboBoxTableCell.forTableColumn(FXCollections.observableArrayList(Department.values())));

            tv.setItems(data);
            tv.setEditable(true);

            // Enable column-specific editing
            setTableColumnsEditable();

            // Allow adding new rows by clicking empty space
            tv.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && tv.getSelectionModel().getSelectedItem() == null) {
                    Person newPerson = new Person();
                    data.add(newPerson);
                    tv.getSelectionModel().select(newPerson);
                    tv.scrollTo(newPerson);
                }
            });
        } catch (Exception e) {
            throw new RuntimeException("Error initializing TableView: " + e.getMessage());
        }
    }

    private void initializePieChart() {
        updatePieChart(); // Populate initially

        // Add listener to data changes to refresh the PieChart
        data.addListener((ListChangeListener<Person>) change -> updatePieChart());
    }
    private void initializeStudentCountLabel() {
        // Set initial count
        updateStudentCount();

        // Add a listener to update the label when the data changes
        data.addListener((ListChangeListener<Person>) change -> updateStudentCount());
    }
    private void updateStudentCount() {
        int totalStudents = data.size();
        studentCountLabel.setText(String.valueOf(totalStudents));
    }
    private void setTableColumnsEditable() {
        // Make text columns editable
        tv_fn.setCellFactory(TextFieldTableCell.forTableColumn());
        tv_fn.setOnEditCommit(event -> {
            Person person = event.getRowValue();
            person.setFirstName(event.getNewValue());
            updateDatabase(person);
        });

        tv_ln.setCellFactory(TextFieldTableCell.forTableColumn());
        tv_ln.setOnEditCommit(event -> {
            Person person = event.getRowValue();
            person.setLastName(event.getNewValue());
            updateDatabase(person);
        });

        tv_position.setCellFactory(TextFieldTableCell.forTableColumn());
        tv_position.setOnEditCommit(event -> {
            Person person = event.getRowValue();
            person.setDepartment(Department.valueOf(event.getNewValue()));
            updateDatabase(person);
        });

        tv_email.setCellFactory(TextFieldTableCell.forTableColumn());
        tv_email.setOnEditCommit(event -> {
            Person person = event.getRowValue();
            person.setEmail(event.getNewValue());
            updateDatabase(person);
        });

        // Make Major column editable with ComboBox
        tv_department.setCellFactory(ComboBoxTableCell.forTableColumn(
                FXCollections.observableArrayList(Department.values())
        ));
        tv_department.setOnEditCommit(event -> {
            Person person = event.getRowValue();
            person.setDepartment(event.getNewValue());
            updateDatabase(person);
        });
    }
    private void updatePieChart() {
        Map<Department, Integer> majorCounts = new HashMap<>();
        for (Person person : data) {
            Department major = person.getDepartment();
            majorCounts.put(major, majorCounts.getOrDefault(major, 0) + 1);
        }

        // Create PieChart data
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        for (Map.Entry<Department, Integer> entry : majorCounts.entrySet()) {
            String majorName = entry.getKey() != null ? entry.getKey().name() : "Unknown";
            int count = entry.getValue();
            pieChartData.add(new PieChart.Data(majorName, count));
        }

        // Update PieChart
        deparmtmentPieChart.setData(pieChartData);
    }
    private void updateDatabase(Person person) {
        try {
            cnUtil.editUser(person.getId(), person);
            showTemporaryStatus("Record updated successfully.");
        } catch (Exception e) {
            showAlert("Update Error", "Failed to update the record in the database.");
            e.printStackTrace();
        }
    }
    private void initializeComboBox() {
        combo_department.setItems(FXCollections.observableArrayList(Department.values()));
    }
    private void initializeSession() {
        if (currentSession != null) {
            System.out.println("Logged in as: " + currentSession.getUserName());
            userNameLabel.setText(currentSession.getUserName());

            // Load the image path specific to the current user
            String imgPath = currentSession.getImg();
            if (imgPath != null) {
                img_view.setImage(new Image(imgPath)); // Load image
            } else {
                System.out.println("No profile picture set for this user.");
            }
        } else {
            System.out.println("No active session. Redirecting to login...");
            logout(null);
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
        position.textProperty().addListener(formListener);
        email.textProperty().addListener(formListener);
        imageURL.textProperty().addListener(formListener);
        for (Person person : data) {
            System.out.println("Person: " + person.getFirstName() + ", Department: " + person.getDepartment());
        }

        // Add a listener for the ComboBox selection
        combo_department.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            addBtn.setDisable(!isFormValid());
            clearBtn.setDisable(!isAnyFieldFilled());
        });
    }
    private void manageMenuButton() {
        // Enable/disable buttons and menu items based on selection
        tv.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            boolean isSelected = newSelection != null;

            // Buttons
            editBtn.setDisable(!isSelected);
            deleteBtn.setDisable(!isSelected);
            clearBtn.setDisable(!isSelected);

            // Menu items
            editItem.setDisable(!isSelected);
            deleteItem.setDisable(!isSelected);
            ClearItem.setDisable(!isSelected);
            CopyItem.setDisable(!isSelected);
        });
    }
    private boolean isAnyFieldFilled() {
        // Checks if any field has text to enable the clear button
        return !first_name.getText().trim().isEmpty() ||
                !last_name.getText().trim().isEmpty() ||
                !position.getText().trim().isEmpty() ||
                combo_department.getSelectionModel().getSelectedItem() != null || // Check ComboBox selection
                !email.getText().trim().isEmpty() ||
                !imageURL.getText().trim().isEmpty();
    }
    private boolean isFormValid() {
        // Basic validation: all fields should be non-empty; email should contain "@" as a simplistic check
        return !first_name.getText().trim().isEmpty() &&
                !last_name.getText().trim().isEmpty() &&
                !position.getText().trim().isEmpty() &&
                combo_department.getSelectionModel().getSelectedItem() != null && // Check ComboBox selection
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
            Department selectedDepartment = combo_department.getSelectionModel().getSelectedItem();
            Person p = new Person(
                    first_name.getText(),
                    last_name.getText(),
                    position.getText(),
                    selectedDepartment,
                    email.getText(),
                    imageURL.getText()
            );

            try {
                // Attempt to insert the record
                cnUtil.insertUser(p);
                p.setId(cnUtil.retrieveId(p));
                data.add(p); // Add to the list only if the database insertion succeeds
                clearForm();

                // Update PieChart
                updatePieChart();

                // Show success message temporarily
                showTemporaryStatus("Record added successfully.");
            } catch (SQLException e) {
                // Check if the exception is due to a duplicate entry
                if (e.getSQLState().startsWith("23")) { // SQLState 23xxx is for integrity constraint violations
                    showAlert("Duplicate Record", "A record with the same email already exists.");
                    showTemporaryStatus("Error: Duplicate record detected.");
                } else {
                    // Handle other SQL exceptions
                    showAlert("Database Error", "An error occurred while adding the record: " + e.getMessage());
                    showTemporaryStatus("Error: Database issue encountered.");
                }
            }
        } else {
            // Handle validation errors
            showAlert("Invalid Input", validationMessage);
            showTemporaryStatus("Error: " + validationMessage); // Show validation error message temporarily
        }
    }

    @FXML
    protected void editRecord() {
        Person selectedPerson = tv.getSelectionModel().getSelectedItem();
        String validationMessage = validateFields();
        if (selectedPerson != null && validationMessage.isEmpty()) {
            Department selectedDepartment = combo_department.getSelectionModel().getSelectedItem();
            Person updatedPerson = new Person(
                    selectedPerson.getId(),
                    first_name.getText(),
                    last_name.getText(),
                    position.getText(),
                    selectedDepartment,
                    email.getText(),
                    imageURL.getText()
            );

            cnUtil.editUser(selectedPerson.getId(), updatedPerson);
            int index = data.indexOf(selectedPerson);
            data.set(index, updatedPerson);
            tv.getSelectionModel().select(updatedPerson);
            updatePieChart();


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
        String departmentError = validateDepartment(position.getText());
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
        position.setText("");
        combo_department.getSelectionModel().clearSelection(); // Clear the ComboBox selection
        email.setText("");
        imageURL.setText("");
    }
    @FXML
    protected void logout(ActionEvent actionEvent) {
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
        updatePieChart();

        tv.getSelectionModel().select(index);
    }
    @FXML
    protected void showImage() {
        File file = (new FileChooser()).showOpenDialog(img_view.getScene().getWindow());
        if (file != null) {
            String fileUri = file.toURI().toString();

            // Update UI
            img_view.setImage(new Image(fileUri));

            // Persist the image in the session
            UserSession currentSession = UserSession.getCurrentSession();
            if (currentSession != null) {
                currentSession.setImg(fileUri);
            } else {
                System.out.println("No active session to save image!");
            }

            // Simulate uploading the image
            Task<Void> uploadTask = createUploadTask(file, progressBar);
            progressBar.setVisible(true);
            progressBar.progressProperty().bind(uploadTask.progressProperty());

            uploadTask.setOnSucceeded(event -> {
                progressBar.setVisible(false);
                progressBar.progressProperty().unbind();
                progressBar.setProgress(0); // Reset progress
            });

            uploadTask.setOnFailed(event -> {
                progressBar.setVisible(false);
                progressBar.progressProperty().unbind();
                progressBar.setProgress(0);
                System.out.println("Upload failed: " + uploadTask.getException().getMessage());
            });

            new Thread(uploadTask).start();
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
            tv_position.setText(p.getPosition());

            // Set the selected item in the ComboBox instead of setting text
            if (p.getDepartment() != null) {
                combo_department.getSelectionModel().select(p.getDepartment());
            } else {
                combo_department.getSelectionModel().clearSelection();
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
        ObservableList<Department> options = FXCollections.observableArrayList(Department.values());
        ComboBox<Department> comboBox = new ComboBox<>(options);
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
            MyLogger.makeLog(results.fname + " " + results.lname + " " + results.department);
        });
    }
    private Task<Void> createUploadTask(File file, ProgressBar progressBar) {
        return new Task<>() {
            @Override
            protected Void call() throws Exception {
                BlobClient blobClient = store.getContainerClient().getBlobClient(file.getName());
                long fileSize = Files.size(file.toPath());
                long uploadedBytes = 0;

                try (FileInputStream fileInputStream = new FileInputStream(file);
                     OutputStream blobOutputStream = blobClient.getBlockBlobClient().getBlobOutputStream()) {

                    byte[] buffer = new byte[1024 * 1024]; // 1 MB buffer size
                    int bytesRead;

                    while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                        blobOutputStream.write(buffer, 0, bytesRead);
                        uploadedBytes += bytesRead;

                        // Calculate and update progress as a percentage
                        int progress = (int) ((double) uploadedBytes / fileSize * 100);
                        updateProgress(progress, 100);
                    }
                }

                return null;
            }
        };
    }
    public void importCSV(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File file = fileChooser.showOpenDialog(tv.getScene().getWindow());

        if (file != null) {
            int duplicateCount = 0; // Counter for duplicates
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] fields = line.split(","); // Assumes CSV fields are separated by commas
                    if (fields.length >= 6) { // Ensure at least 6 fields (adjust based on CSV structure)
                        // Convert all fields to uppercase
                        String firstName = fields[0].trim();
                        String lastName = fields[1].trim();
                        String department = fields[2].trim();

                        // Handle Major enum safely
                        Department major = null;
                        try {
                            major = Department.valueOf(fields[3].trim().toUpperCase()); // Convert to uppercase
                        } catch (IllegalArgumentException e) {
                            // Assign default value or skip invalid major (optional)
                            major = null; // Or Major.UNKNOWN if such a default exists
                        }

                        String email = fields[4].trim();
                        String imageURL = fields[5].trim(); // Keeping image URL as is, as URLs are case-sensitive

                        // Create a Person object
                        Person person = new Person(firstName, lastName, department, major, email, imageURL);

                        // Attempt to insert into database
                        try {
                            cnUtil.insertUser(person); // Only proceed if insert succeeds
                            person.setId(cnUtil.retrieveId(person));
                            data.add(person); // Add to list view only after successful database insertion
                        } catch (SQLException e) {
                            duplicateCount++; // Increment duplicate counter if insert fails
                        }
                    }
                }
                // Show a success message and number of duplicates
                showAlert("Import Completed", "CSV imported successfully.\n" +
                        "Duplicates skipped: " + duplicateCount);
            } catch (Exception e) {
                showAlert("Error", "Failed to import CSV: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void exportCSV(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File file = fileChooser.showSaveDialog(tv.getScene().getWindow());

        if (file != null) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                for (Person person : data) {
                    String line = String.format("%s,%s,%s,%s,%s,%s",
                            person.getFirstName(),
                            person.getLastName(),
                            person.getDepartment(),
                            person.getDepartment().name(), // Convert Enum to String
                            person.getEmail(),
                            person.getImageURL());
                    writer.write(line);
                    writer.newLine();
                }
                showTemporaryStatus("CSV exported successfully.");
            } catch (Exception e) {
                showAlert("Error", "Failed to export CSV: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
    @FXML
    public void pdfMajorNum(ActionEvent actionEvent) {
        // Count students by major (department)
        Map<Department, Integer> departmentCounts = new HashMap<>();
        int totalEmployees = 0;

        for (Person person : data) {
            Department department = person.getDepartment();
            departmentCounts.put(department, departmentCounts.getOrDefault(department, 0) + 1);
            totalEmployees++; // Increment total employee count
        }

        // Use FileChooser for saving the PDF
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save PDF Report");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        File file = fileChooser.showSaveDialog(tv.getScene().getWindow());

        if (file == null) return; // User canceled

        // Create a PDF document
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream(file));
            document.open();

            // Add title
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
            document.add(new Paragraph("Number of Employees by Department", titleFont));
            document.add(new Paragraph("\n"));

            // Add table
            PdfPTable table = new PdfPTable(2); // Two columns: Department and Count
            table.setWidthPercentage(100); // Set table width to 100%
            table.addCell(new PdfPCell(new Phrase("Department", titleFont)));
            table.addCell(new PdfPCell(new Phrase("Count", titleFont)));

            for (Map.Entry<Department, Integer> entry : departmentCounts.entrySet()) {
                table.addCell(entry.getKey() != null ? entry.getKey().name() : "Unknown");
                table.addCell(String.valueOf(entry.getValue()));
            }

            // Add total employees row
            Font boldFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
            PdfPCell totalCell = new PdfPCell(new Phrase("Total Employees", boldFont));
            totalCell.setColspan(1); // Span only one column
            table.addCell(totalCell);
            table.addCell(String.valueOf(totalEmployees));

            document.add(table);

            // Add timestamp
            document.add(new Paragraph("\n"));
            Paragraph timestamp = new Paragraph("Generated on: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            timestamp.setAlignment(Element.ALIGN_RIGHT);
            document.add(timestamp);

            // Success message
            showTemporaryStatus("PDF report generated successfully.");
        } catch (DocumentException | IOException e) {
            showAlert("PDF Generation Error", "Failed to generate the PDF report: " + e.getMessage());
            e.printStackTrace();
        } finally {
            document.close();
        }
    }

    @FXML
    protected void copySelectedRecord() {
        Person selectedPerson = tv.getSelectionModel().getSelectedItem();
        if (selectedPerson != null) {
            Clipboard clipboard = Clipboard.getSystemClipboard();
            ClipboardContent content = new ClipboardContent();
            content.putString(selectedPerson.toString()); // Customize toString() in `Person` if needed
            clipboard.setContent(content);
            showTemporaryStatus("Record copied to clipboard.");
        } else {
            showAlert("No Selection", "Please select a record to copy.");
        }
    }
    public static enum Department {BOH, FOH, MG}
    private static class Results {
        String fname;
        String lname;
        Department department;

        public Results(String name, String date, Department venue) {
            this.fname = name;
            this.lname = date;
            this.department = venue;
        }
    }
    public static Department fromString(String value) {
        if (value == null) return null;
        try {
            return Department.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid Department value: " + value);
            return null; // Or throw an exception if necessary
        }
    }
}