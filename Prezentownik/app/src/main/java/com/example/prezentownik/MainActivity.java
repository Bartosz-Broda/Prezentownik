package com.example.prezentownik;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.prezentownik.adapters.RecyclerAdapter;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    //vars
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mGiftQuantity = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: started.");

        addInfoToArrays();
    }

    private void addInfoToArrays(){
        mNames.add("Jolka");
        mGiftQuantity.add("zakupino 2 z 3 prezentów");

        mNames.add("Piotrek");
        mGiftQuantity.add("zakupino 0 z 1 prezentów");

        mNames.add("Michałek");
        mGiftQuantity.add("zakupino 0 z 2 prezentów");

        mNames.add("Zuzia");
        mGiftQuantity.add("zakupino 1 z 3 prezentów");

        initRecyclerView();
    }

    private void initRecyclerView(){
        Log.d(TAG, "initRecyclerView: init recycelrview");
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        RecyclerAdapter adapter = new RecyclerAdapter(this, mNames, mGiftQuantity);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}