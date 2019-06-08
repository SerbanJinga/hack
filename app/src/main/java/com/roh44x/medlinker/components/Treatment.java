package com.roh44x.medlinker.components;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Treatment {
    public String uid;
    public String title;
    public String author;
    public String body;
    public String medication;
    public String rating;
    public int usefulCount = 0;
    public Map<String, Boolean> usefuls  = new HashMap<>();


    public Treatment(){

    }

    public Treatment(String uid, String title,String author, String body, String medication, String rating)
    {
        this.uid = uid;
        this.title = title;
        this.author = author;
        this.body = body;
        this.medication = medication;
        this.rating = rating;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("author", author);
        result.put("title", title);
        result.put("body", body);
        result.put("medication", medication);
        result.put("starCount", usefulCount);
        result.put("stars", usefuls);
        result.put("rating", rating);

        return result;
    }
}
