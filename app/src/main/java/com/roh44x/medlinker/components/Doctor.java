package com.roh44x.medlinker.components;

public class Doctor {

    public String firstName;
    public String lastName;
    public String email;
    public Integer age;
    public Boolean isConfirmed;
    public String url;

    public Doctor(){

    }

    public Doctor(String firstName, String lastName, String email, Integer age, Boolean isConfirmed, String url)
    {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.age = age;
        this.isConfirmed = isConfirmed;
        this.url = url;
    }

}
