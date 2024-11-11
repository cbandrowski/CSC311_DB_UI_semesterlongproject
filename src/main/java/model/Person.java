package model;

import viewmodel.DB_GUI_Controller;

public class Person {
    private Integer id;
    private String firstName;
    private String lastName;
    private String department;
    private DB_GUI_Controller.Major major;

    private String email;
    private String imageURL;

    public Person() {
    }

    public Person(String firstName, String lastName, String department, DB_GUI_Controller.Major major, String email, String imageURL) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.department = department;
        this.major = major;
        this.email = email;
        this.imageURL = imageURL;
    }

    public Person(Integer id, String firstName, String lastName, String department, DB_GUI_Controller.Major major, String email, String imageURL) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.department = department;
        this.major = major;
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

    public DB_GUI_Controller.Major getMajor() {
        return major;
    }

    public void setMajor(DB_GUI_Controller.Major major) {
        this.major = major;
    }

    // Optional: method to get Major as a String
    public String getMajorAsString() {
        return major != null ? major.name() : "";
    }

    public void setMajorFromString(String majorString) {
        this.major = DB_GUI_Controller.Major.valueOf(majorString);
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
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
                ", department='" + department + '\'' +
                ", major='" + major + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

}