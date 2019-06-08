package com.roh44x.medlinker.components;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Treatment {
    public String uid;
    public String title;
    public String author;
    public String body;
    public int usefulCount = 0;
    public Map<String, Boolean> usefuls  = new HashMap<>();


    public Treatment(){

    }

    public Treatment(String uid, String title,String author, String body)
    {
        this.uid = uid;
        this.title = title;
        this.author = author;
        this.body = body;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("author", author);
        result.put("title", title);
        result.put("body", body);
        result.put("starCount", usefulCount);
        result.put("stars", usefuls);

        return result;
    }
}
