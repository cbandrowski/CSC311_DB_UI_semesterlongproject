# Employee Statistics

## Table of Contents
1. [About](#about)
2. [Getting Started](#getting-started)
   - [Prerequisites](#prerequisites)
   - [Installation](#installation)
   - [Account Creation](#account-creation)
3. [Features](#features)
   - [User Management](#user-management)
   - [Visualization](#visualization)
   - [Data Import & Export](#data-import--export)
   - [Reporting](#reporting)
   - [Theming](#theming)
   - [Profile Management](#profile-management)
4. [Backend](#backend)
   - [Database](#database)
   - [File Uploads](#file-uploads)
   - [Validation](#validation)
5. [Usage](#usage)
   - [Adding a New User](#adding-a-new-user)
   - [Editing an Existing User](#editing-an-existing-user)
   - [Deleting a User](#deleting-a-user)
   - [Importing Data](#importing-data)
   - [Exporting Data](#exporting-data)
   - [Generating Reports](#generating-reports)
   - [Login and Password Manaagment](#login-and-password-management)
     


## About
This application allows a Employeer to manage and track Employee;s, including Positions and overall student statistics. It provides a user-friendly interface for administrators to add, edit, delete, and visualize data. The app also generates reports and supports data import/export for efficient management.

## Getting Started

### Prerequisites
- **JavaFX**: Ensure you have JavaFX properly set up in your development environment.
- **Microsoft Azure SQL & Blob Storage**: Required for data persistence and file uploads.

### Installation
1. Clone the repository:
   ```bash
   git clone https://github.com/cbandrowski/CSC311_DB_UI_semesterlongproject.git
   ```
2. Open the project in your IDE and configure the database connection settings in `DbConnectivityClass`.

### Account Creation
- **Initial Use**: Create an account to start using the application.
- **Subsequent Use**: Log in with your credentials.
  



https://github.com/user-attachments/assets/df755635-21b0-43fc-a16f-e8d8424e9919





### Username Requirements
- Must be 5-15 characters long.
- Can only contain letters and numbers.

### Password Requirements
- Must be 8-20 characters long.
- Include at least one uppercase letter.
- Include at least one lowercase letter.
- Include at least one digit.
- Include at least one special character (`@#$%^&+=`).

### After Initial Login
1. Navigate to **File > ChangePic** from the menu bar.
2. Select and upload your profile picture from your desktop.



https://github.com/user-attachments/assets/f19242d5-58a7-4676-899b-d841f1313211

## Features

### User Management
- **Add User (`addNewRecord`)**: Adds a new user by validating form inputs and saving them to the database.
- **Edit User (`editRecord`)**: Updates the selected user's details in the database.
- **Delete User (`deleteRecord`)**: Removes the selected user record from the database.
- **Clear Form (`clearForm`)**: Resets all input fields.

### Visualization
- **Pie Chart (`initializePieChart`)**: Displays the distribution of students across departments using the `updatePieChart` method.
- **Total Users (`updateStudentCount`)**: Dynamically calculates and displays the total number of users.

### Data Import & Export
- **Import Data (`importCSV`)**: Reads user records from a CSV file, validates, and inserts them into the database.
- **Export Data (`exportCSV`)**: Exports the current user records to a CSV file.

### Reporting
- **Generate PDF (`pdfMajorNum`)**: Creates a PDF report showing the number of employees by department, including a timestamp.

### Theming
- **Light Theme (`lightTheme`)**: Applies the light theme to the application.
- **Dark Theme (`darkTheme`)**: Applies the dark theme to the application.

### Profile Management
- **Upload Profile Picture (`showImage`)**: Allows users to upload and display a profile picture. Progress tracking is implemented for uploads.
  
### Login and Password Management
-**Login (login): Authenticates user credentials against the database. Displays error messages for invalid inputs or incorrect credentials.
-**Sign Up (signUp): Navigates the user to the sign-up screen to create a new account.
-**Toggle Password Visibility (togglePasswordVisibility): Allows users to toggle between hiding and showing their password during login.


## Backend

### Database
- **Microsoft Azure SQL**: Used to store user details, including:
  - `Person` table: Contains fields for first name, last name, department, email, and profile picture URL.

### File Uploads
- **Azure Blob Storage (`createUploadTask`)**: Manages file uploads with progress tracking.

### Validation
- Ensures data integrity with validation methods:
  - `validateName`: Checks if names contain only alphabetic characters.
  - `validateEmail`: Ensures email addresses match a basic format.
  - `validateImageURL`: Verifies the URL ends with `.jpg`, `.png`, or `.gif`.


## Usage

### Adding a New User
1. Fill in the form fields for:
   - First Name
   - Last Name
   - Department
   - Major (select from dropdown)
   - Email
   - Profile Image URL
2. Click **Add** to save the record.

   Method: **`addNewRecord`**
- Validates input fields using `validateFields`.
- Saves the new user to the database with `cnUtil.insertUser`.
- Updates the `ObservableList` and Pie Chart.

   


https://github.com/user-attachments/assets/8aec8b53-0f6b-42fd-9889-89fc090d493d





### Editing an Existing User
1. Select a record in the TableView.
2. Modify the fields as needed.
3. Click **Edit** to update the record.

Method: **`editRecord`**
- Updates the selected user's details in the database using `cnUtil.editUser`.
- Updates the TableView and Pie Chart.

   



https://github.com/user-attachments/assets/e61f2980-8cac-4b48-b3d4-ee496be8cf82




### Deleting a User
1. Select a record in the TableView.
2. Click **Delete** to remove the record.

Method: **`deleteRecord`**
- Deletes the selected user from the database using `cnUtil.deleteRecord`.
- Removes the user from the `ObservableList`.


   

https://github.com/user-attachments/assets/ec7ec9d6-1322-4411-af92-25661cce8dc1



### Importing Data
1. Click **Import CSV**.
2. Choose a valid CSV file and load the data.

   
Method: **`importCSV`**
- Parses CSV records line-by-line.
- Inserts valid records into the database using `cnUtil.insertUser`.



https://github.com/user-attachments/assets/e61accc9-e16f-4b87-9173-cd9e4fad7eb0



### Exporting Data
1. Click **Export CSV**.
2. Save the file to your desired location.
   
   Method: **`exportCSV`**
- Formats user data into CSV rows.
- Writes the rows to a file using `BufferedWriter`.

   

https://github.com/user-attachments/assets/29f9a3b5-616e-40e2-8af3-b8d2c8444398

Export CSV output 

![Screenshot 2024-11-25 at 6 29 43 PM](https://github.com/user-attachments/assets/22410d8e-d6a6-4c68-a622-95c71be2de19)



### Generating Reports
1. Click **Generate PDF**.
2. Save the report to your desired location.
   
   Method: **`pdfMajorNum`**
- Counts employees by department using a `HashMap`.
- Creates a formatted PDF report with iText.

   


https://github.com/user-attachments/assets/936b3dff-cc21-4087-9689-776e01c13d65




PDF Generated 

![Screenshot 2024-11-25 at 6 32 01 PM](https://github.com/user-attachments/assets/66a11e03-50f9-4d9d-a626-41970f3724f9)



---

For any issues or feature requests, please create an issue in the [GitHub repository](https://github.com/cbandrowski/CSC311_DB_UI_semesterlongproject/issues).

     
     
      
        

