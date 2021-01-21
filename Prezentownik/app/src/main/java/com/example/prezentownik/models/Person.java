package com.example.prezentownik.models;

public class Person {

    private String name;
    private String giftQuantity;

    public Person(String name, String giftQuantity) {
        this.name = name;
        this.giftQuantity = giftQuantity;
    }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getGiftQuantity() { return giftQuantity; }

    public void setGiftQuantity(String giftQuantity) { this.giftQuantity = giftQuantity; }


}
