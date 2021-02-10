package com.example.prezentownik;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;

import com.example.prezentownik.adapters.RecyclerAdapter;
import com.example.prezentownik.adapters.RecyclerAdapterForGifts;
import com.example.prezentownik.models.Gift;
import com.example.prezentownik.viewmodels.MainActivityViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class GiftsActivity extends AppCompatActivity {
    private static final String TAG = "GiftsActivity";

    RecyclerView recyclerViewgifts;
    private RecyclerAdapterForGifts adapter;
    MainActivityViewModel mMainActivityViewModel;
    ProgressBar progressBarBudget;
    ProgressBar giftsProgress;
    Animation fadeOutAnim;
    FloatingActionButton fabAddGift;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gifts);
        Intent intent = getIntent();
        String value = intent.getStringExtra("key");
        String listName = intent.getStringExtra("key2");

        Toolbar toolbar = findViewById(R.id.second_toolbar);
        toolbar.setTitle(value);
        progressBarBudget = findViewById(R.id.budget_progressbar);
        giftsProgress = findViewById(R.id.list_progress_gifts);
        fadeOutAnim = AnimationUtils.loadAnimation(this, R.anim.fade_out);
        fabAddGift = findViewById(R.id.fabAddGift);

        mMainActivityViewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
        mMainActivityViewModel.initGifts(listName, value);

        initRecyclerView();
        giftsProgress.setVisibility(View.VISIBLE);

        mMainActivityViewModel.getGiftModelData().observe(this, new Observer<List<Gift>>() {
            @Override
            public void onChanged(List<Gift> gifts) {
                giftsProgress.startAnimation(fadeOutAnim);
                adapter.setGiftModels(gifts);
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void initRecyclerView() {
        Log.d(TAG, "initRecyclerView: init recycelrview");
        recyclerViewgifts = findViewById(R.id.recycler_view_gifts);
        adapter = new RecyclerAdapterForGifts(this);
        recyclerViewgifts.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewgifts.setAdapter(adapter);
    }
}