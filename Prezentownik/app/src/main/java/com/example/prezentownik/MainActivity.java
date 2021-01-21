package com.example.prezentownik;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.prezentownik.adapters.RecyclerAdapter;
import com.example.prezentownik.models.Person;
import com.example.prezentownik.viewmodels.MainActivityViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    //vars
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mGiftQuantity = new ArrayList<>();

    private RecyclerView recyclerView;
    private RecyclerAdapter adapter;
    private MainActivityViewModel mMainActivityViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: started.");

        mMainActivityViewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);

        mMainActivityViewModel.init();

        mMainActivityViewModel.getPerson().observe(this, new Observer<List<Person>>() {
            @Override
            public void onChanged(List<Person> people) {
                adapter.notifyDataSetChanged();
            }
        });

        initRecyclerView();
    }



    private void initRecyclerView(){
        Log.d(TAG, "initRecyclerView: init recycelrview");
        recyclerView = findViewById(R.id.recycler_view);
        adapter = new RecyclerAdapter(this, mMainActivityViewModel.getPerson().getValue());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}