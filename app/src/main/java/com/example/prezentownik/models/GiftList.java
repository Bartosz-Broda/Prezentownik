package com.example.prezentownik.models;

import com.google.firebase.firestore.Exclude;

public class GiftList {
    @Exclude
    public boolean isCreated;
    @Exclude
    public String error;

    private String listName;
    private int listBudget;
    private int usedBudget;

    public String getListName() {
        return listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }

    public int getListBudget() {
        return listBudget;
    }

    public void setListBudget(int listBudget) {
        this.listBudget = listBudget;
    }

    public int getUsedBudget() {
        return usedBudget;
    }

    public void setUsedBudget(int usedBudget) {
        this.usedBudget = usedBudget;
    }


    public GiftList(String listName, int listBudget, int usedBudget) {
        this.listName = listName;
        this.listBudget = listBudget;
        this.usedBudget = usedBudget;
    }

    //empty constructor for firebase
    public GiftList(){}

}
