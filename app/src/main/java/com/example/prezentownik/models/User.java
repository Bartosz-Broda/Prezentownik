package com.example.prezentownik.models;

import com.google.firebase.firestore.Exclude;

public class User {
    public String email;
    public String uid;

    @Exclude
    public boolean isAuthenticated;
    @Exclude
    public boolean isNew, isCreated;
    @Exclude
    public String error;

    public User() {}

    public User(String email, String uid) {
        this.email = email;
        this.uid = uid;
    }

    public User(String email) {
        this.email = email;
    }
}

