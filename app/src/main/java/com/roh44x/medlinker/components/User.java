package com.roh44x.medlinker.components;

public class User {

    public String firstName;
    public String lastName;
    public String email;
    public String disease;
    public Integer age;
    public Boolean isDoctor;
    public Boolean isConfirmed;

    public User() {}

    public User(String firstName, String lastName, String email, String disease, Integer age, Boolean isDoctor, Boolean isConfirmed) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.disease = disease;
        this.age = age;
        this.isDoctor = isDoctor;
        this.isConfirmed = isConfirmed;
    }
}
