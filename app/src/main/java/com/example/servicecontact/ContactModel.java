package com.example.servicecontact;

public class ContactModel {
    static String firstName ;
    static String lastName ;
    static String phoneMobile ;
    static String email ;
    int id ;

    public ContactModel(){

    }

    public ContactModel(String firstName, String lastName, String phoneMobile, String email) {
        ContactModel.firstName = firstName;
        ContactModel.lastName = lastName;
        ContactModel.phoneMobile = phoneMobile;
        ContactModel.email = email;
    }

    public ContactModel(String firstName, String lastName, String phoneMobile, String email, int id) {
        ContactModel.firstName = firstName;
        ContactModel.lastName = lastName;
        ContactModel.phoneMobile = phoneMobile;
        ContactModel.email = email;
        this.id = id;
    }


    public static String getFirstName() {
        return firstName;
    }

    public static String getLastName() {
        return lastName;
    }

    public static String getPhoneMobile() {
        return phoneMobile;
    }

    public static String getEmail() {
        return email;
    }

    public int getId() {
        return id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPhoneMobile(String phoneMobile) {
        this.phoneMobile = phoneMobile;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setId(int id) {
        this.id = id;
    }
}
