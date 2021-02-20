package com.example.prezentownik.models;

public class Gift {

    private String giftName;
    private float giftPrice;
    private Boolean isBought;

    public Gift(String giftName, float giftPrice, boolean isBought) {
        this.giftName = giftName;
        this.giftPrice = giftPrice;
        this.isBought = isBought;
    }

    public String getGiftName() { return giftName; }

    public void setGiftName(String giftName) { this.giftName = giftName; }

    public Boolean getIsBought() { return isBought; }

    public void setIsBought(Boolean isBought) { this.isBought = isBought; }

    public float getGiftPrice() { return giftPrice; }

    public void setGiftPrice(float giftPrice) { this.giftPrice = giftPrice; }

    public Gift() {
    }
}

