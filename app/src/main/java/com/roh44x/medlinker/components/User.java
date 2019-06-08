package com.roh44x.medlinker.components;

public class User {

    public String firstName;
    public String lastName;
    public String email;
    public String disease;
    public Integer age;

    public User() {}

    public User(String firstName, String lastName, String email, String disease, Integer age){
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.disease = disease;
        this.age = age;
    }
}
