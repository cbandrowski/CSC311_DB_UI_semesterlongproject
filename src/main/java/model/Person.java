package model;

import viewmodel.DB_GUI_Controller;

public class Person {
    private Integer id;
    private String firstName;
    private String lastName;
    private String position;
    private DB_GUI_Controller.Department department;

    private String email;
    private String imageURL;

    public Person() {
    }

    public Person(String firstName, String lastName, String position, DB_GUI_Controller.Department major, String email, String imageURL) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.position = position;
        this.department = major;
        this.email = email;
        this.imageURL = imageURL;
    }

    public Person(Integer id, String firstName, String lastName, String position, DB_GUI_Controller.Department major, String email, String imageURL) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.position = position;
        this.department = major;
        this.email = email;
        this.imageURL = imageURL;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }


    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public DB_GUI_Controller.Department getDepartment() {
        return department;
    }

    public void setDepartment(DB_GUI_Controller.Department department) {
        this.department = department;
    }

    // Optional: method to get Major as a String
    public String getDepartmentAsString() {
        return department != null ? department.name() : "";
    }

    public void setDepartmentFromString(String departmentString) {
        this.department = DB_GUI_Controller.Department.valueOf(departmentString);
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", position='" + position + '\'' +
                ", major='" + department + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

}