package com.example.prezentownik.models;

public class Person {

    private String name;
    private int budget;
    private int giftQuantity;
    private int giftsBought;
    private float checkedGiftsPrice;

    public Person(String name, int budget, int giftQuantity, int giftsBought, float checkedGiftsPrice) {
        this.name = name;
        this.budget = budget;
        this.giftQuantity = giftQuantity;
        this.giftsBought = giftsBought;
        this.checkedGiftsPrice = checkedGiftsPrice;
    }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public int getGiftQuantity() { return giftQuantity; }

    public void setGiftQuantity(int giftQuantity) { this.giftQuantity = giftQuantity; }

    public int getBudget() { return budget; }

    public void setBudget(int budget) { this.budget = budget; }

    public int getGiftsBought() { return giftsBought; }

    public void setGiftsBought(int giftsBought) { this.giftsBought = giftsBought; }

    public float getCheckedGiftsPrice() {
        return checkedGiftsPrice;
    }

    public void setCheckedGiftsPrice(float checkedGiftsPrice) {
        this.checkedGiftsPrice = checkedGiftsPrice;
    }

    public Person() {
    }
}
