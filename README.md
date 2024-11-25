# Employee Statistics

## Table of Contents
- [About](#about)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Installation](#installation)
  - [Account Creation](#account-creation)
  - [Username Requirements](#username-requirements)
  - [Password Requirements](#password-requirements)
  - [After Initial Login](#after-initial-login)
- [Features](#features)
- [Backend](#backend)
- [Usage](#usage)

## About
This application allows a school to manage and track student information, including majors and overall student statistics. It provides a user-friendly interface for administrators to add, edit, delete, and visualize data. The app also generates reports and supports data import/export for efficient management.

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
- 



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
- **Add User**: Create new student records.
- **Edit User**: Modify existing records.
- **Delete User**: Remove records from the database.
- **Clear Form**: Reset all input fields.

### Visualization
- **Pie Chart**: Displays the distribution of students across different majors.
- **Total Users**: Shows the total number of students.

### Data Import & Export
- **CSV Import**: Import student data from a CSV file.
- **CSV Export**: Export current data to a CSV file.

### Reporting
- **PDF Generation**: Generate a detailed PDF report of student statistics, including counts by major.

### Theming
- **Light/Dark Themes**: Switch between light and dark modes.

### Profile Management
- Upload and display a profile picture for logged-in users.

## Backend

### Database
The application uses **Microsoft Azure SQL** for data persistence. The database includes:
- `Person` table: Stores student details such as name, department, email, and major.

### File Uploads
Files are uploaded to **Azure Blob Storage** with progress tracking.

### Validation
The application ensures data integrity with validation for:
- **Names**: Only alphabetic characters allowed.
- **Department**: Alphanumeric and spaces allowed.
- **Email**: Basic email format validation.
- **Image URL**: Must end with `.jpg`, `.png`, or `.gif`.

### Reporting
PDF generation is implemented using the **iText library** for creating formatted reports.

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
   


https://github.com/user-attachments/assets/8aec8b53-0f6b-42fd-9889-89fc090d493d





### Editing an Existing User
1. Select a record in the TableView.
2. Modify the fields as needed.
3. Click **Edit** to update the record.
   



https://github.com/user-attachments/assets/e61f2980-8cac-4b48-b3d4-ee496be8cf82




### Deleting a User
1. Select a record in the TableView.
2. Click **Delete** to remove the record.

   

https://github.com/user-attachments/assets/ec7ec9d6-1322-4411-af92-25661cce8dc1



### Importing Data
1. Click **Import CSV**.
2. Choose a valid CSV file and load the data.


https://github.com/user-attachments/assets/e61accc9-e16f-4b87-9173-cd9e4fad7eb0



### Exporting Data
1. Click **Export CSV**.
2. Save the file to your desired location.
   

https://github.com/user-attachments/assets/29f9a3b5-616e-40e2-8af3-b8d2c8444398

Export CSV output 

![Screenshot 2024-11-25 at 6 29 43 PM](https://github.com/user-attachments/assets/22410d8e-d6a6-4c68-a622-95c71be2de19)



### Generating Reports
1. Click **Generate PDF**.
2. Save the report to your desired location.
   


https://github.com/user-attachments/assets/936b3dff-cc21-4087-9689-776e01c13d65




PDF Generated 

![Screenshot 2024-11-25 at 6 32 01 PM](https://github.com/user-attachments/assets/66a11e03-50f9-4d9d-a626-41970f3724f9)



---

For any issues or feature requests, please create an issue in the [GitHub repository](https://github.com/cbandrowski/CSC311_DB_UI_semesterlongproject/issues).

     
     
      
        

