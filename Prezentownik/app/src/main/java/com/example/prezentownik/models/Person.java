package com.example.prezentownik.models;

public class Person {

    private String name;
    private int budget;
    private String giftQuantity;

    public Person(String name, int budget, String giftQuantity) {
        this.name = name;
        this.budget = budget;
        this.giftQuantity = giftQuantity;
    }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getGiftQuantity() { return giftQuantity; }

    public void setGiftQuantity(String giftQuantity) { this.giftQuantity = giftQuantity; }

    public int getBudget() { return budget; }

    public void setBudget(int budget) { this.budget = budget; }


}
