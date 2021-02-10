package com.example.prezentownik.models;

public class Gift {

    private String giftName;
    private int giftPrice;
    private Boolean isBought;

    public Gift(String giftName, int giftPrice) {
        this.giftName = giftName;
        this.giftPrice = giftPrice;
    }

    public String getGiftName() { return giftName; }

    public void setGiftName(String giftName) { this.giftName = giftName; }

    public Boolean getIsBought() { return isBought; }

    public void setIsBought(Boolean isBought) { this.isBought = isBought; }

    public int getGiftPrice() { return giftPrice; }

    public void setGiftPrice(int giftPrice) { this.giftPrice = giftPrice; }

    public Gift() {
    }
}

