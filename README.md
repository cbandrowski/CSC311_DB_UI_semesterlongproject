# Student Statistics

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

https://github.com/user-attachments/assets/f486a2f6-0cba-4b69-91ed-a6a1a6f6616e



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

https://github.com/user-attachments/assets/db46e773-8e58-41b6-b5bb-b9b28e931224

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
   

https://github.com/user-attachments/assets/e9f62c3e-c6e5-45e3-8633-72b7a3a62eb5



### Editing an Existing User
1. Select a record in the TableView.
2. Modify the fields as needed.
3. Click **Edit** to update the record.
   

https://github.com/user-attachments/assets/f96ca3a2-28b7-4385-aba3-ef4c743b8a77



### Deleting a User
1. Select a record in the TableView.
2. Click **Delete** to remove the record.

### Importing Data
1. Click **Import CSV**.
2. Choose a valid CSV file and load the data.

https://github.com/user-attachments/assets/efce9638-4b17-41e5-831e-4af4435c92b8


### Exporting Data
1. Click **Export CSV**.
2. Save the file to your desired location.
   

https://github.com/user-attachments/assets/e49c9288-0e8e-41ef-9669-7e290f22ec98



CSV FIle Exported 
![Screenshot 2024-11-24 at 12 07 32 PM](https://github.com/user-attachments/assets/27e4398e-3f65-4ae3-a6b9-db5a79ff75ae)


### Generating Reports
1. Click **Generate PDF**.
2. Save the report to your desired location.
   

https://github.com/user-attachments/assets/e0c16231-0078-4834-82e3-0af74d630179


PDF Generated 

![Screenshot 2024-11-24 at 1 27 02 PM](https://github.com/user-attachments/assets/1d48d27b-8be5-4b51-9402-5cce9e0d013d)



---

For any issues or feature requests, please create an issue in the [GitHub repository](https://github.com/cbandrowski/CSC311_DB_UI_semesterlongproject/issues).

     
     
      
        

