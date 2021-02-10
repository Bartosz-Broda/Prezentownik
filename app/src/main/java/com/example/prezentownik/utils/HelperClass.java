package com.example.prezentownik.utils;


import android.util.Log;

import java.util.LinkedList;

public class HelperClass {
    private static final String TAG = "FirebaseAuthAppTag";

    public static void logErrorMessage(String errorMessage) {
        Log.d(TAG, errorMessage);
    }

    public LinkedList <String> mListOfLists;
}