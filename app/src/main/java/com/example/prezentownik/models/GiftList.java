package com.example.prezentownik.models;

import com.google.firebase.firestore.Exclude;

public class GiftList {
    @Exclude
    public boolean isCreated;
    @Exclude
    public String error;

    private String listName;
    private float listBudget;
    private int usedBudget;

    public String getListName() {
        return listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }

    public float getListBudget() {
        return listBudget;
    }

    public void setListBudget(float listBudget) {
        this.listBudget = listBudget;
    }

    public int getUsedBudget() {
        return usedBudget;
    }

    public void setUsedBudget(int usedBudget) {
        this.usedBudget = usedBudget;
    }


    public GiftList(String listName, float listBudget, int usedBudget) {
        this.listName = listName;
        this.listBudget = listBudget;
        this.usedBudget = usedBudget;
    }

    //empty constructor for firebase
    public GiftList(){}

}
