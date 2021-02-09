package com.example.prezentownik.models;

import com.google.firebase.firestore.Exclude;

public class Person {

    private String name;
    private int budget;
    private String giftQuantity;

    public Person(String name, int budget) {
        this.name = name;
        this.budget = budget;
    }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getGiftQuantity() { return giftQuantity; }

    public void setGiftQuantity(String giftQuantity) { this.giftQuantity = giftQuantity; }

    public int getBudget() { return budget; }

    public void setBudget(int budget) { this.budget = budget; }

    public Person() {
    }
}
