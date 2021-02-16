package com.example.prezentownik;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prezentownik.adapters.RecyclerAdapter;
import com.example.prezentownik.adapters.RecyclerAdapterForGifts;
import com.example.prezentownik.models.Gift;
import com.example.prezentownik.viewmodels.MainActivityViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.protobuf.StringValue;

import java.util.List;

public class GiftsActivity extends AppCompatActivity implements View.OnClickListener, RecyclerAdapterForGifts.OnCheckboxClicked {
    private static final String TAG = "GiftsActivity";

    RecyclerView recyclerViewgifts;
    private RecyclerAdapterForGifts adapter;
    MainActivityViewModel mMainActivityViewModel;
    TextView nameTextView;
    TextView progressTextView;
    ProgressBar progressBarBudget;
    ProgressBar giftsProgress;
    Animation fadeOutAnim;
    FloatingActionButton fabAddGift;
    float CheckedGiftsSumPrice;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gifts);
        Intent intent = getIntent();
        String value = intent.getStringExtra("key");
        String listName = intent.getStringExtra("key2");
        int personBudget = intent.getIntExtra("key3", 0);

        nameTextView = findViewById(R.id.personNameTextView);
        progressTextView = findViewById(R.id.progressTitleTextView);
        progressBarBudget = findViewById(R.id.budget_progressbar);

        if (personBudget == 0) {
            nameTextView.setVisibility(View.GONE);
            progressTextView.setVisibility(View.GONE);
            Toolbar toolbar = findViewById(R.id.second_toolbar);
            toolbar.setTitle(value);
        } else {
            nameTextView.setText(value);
            progressTextView.setText("Budżet: " + personBudget);
            progressBarBudget.setVisibility(View.VISIBLE);
        }

        giftsProgress = findViewById(R.id.list_progress_gifts);
        fadeOutAnim = AnimationUtils.loadAnimation(this, R.anim.fade_out);
        fabAddGift = findViewById(R.id.fabAddGift);

        mMainActivityViewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
        //initializing repository with gifts
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

        fabAddGift.setOnClickListener(this);

    }

    private void initRecyclerView() {
        Log.d(TAG, "initRecyclerView: init recycelrview");
        recyclerViewgifts = findViewById(R.id.recycler_view_gifts);
        adapter = new RecyclerAdapterForGifts(this, this);
        recyclerViewgifts.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewgifts.setAdapter(adapter);
    }

    private void updateGift(boolean isGiftChecked, String name, int price) {
        Intent intent = GiftsActivity.this.getIntent();
        String personName = intent.getStringExtra("key");
        String listName = intent.getStringExtra("key2");

        mMainActivityViewModel.addNewGift(name, price, listName, personName, isGiftChecked);
    }

    private void addNewGift(String listName, String personName) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText giftNameEditText = new EditText(this);
        giftNameEditText.setHint("Nazwa prezentu");

        layout.addView(giftNameEditText);

        final EditText giftPriceEditText = new EditText(this);
        giftPriceEditText.setHint("Przewidywana cena (opcjonalnie)");
        layout.addView(giftPriceEditText);

        layout.setDividerPadding(25);
        layout.setPadding(35, 0, 35, 25);

        alert.setTitle("Nowy prezent");
        alert.setView(layout);

        alert.setPositiveButton("Zatwierdź", (dialog, whichButton) -> {
            //What ever you want to do with the value
            int price = 0;
            String name = giftNameEditText.getText().toString();

            if (!giftPriceEditText.getText().toString().equals("")) {
                price = Integer.parseInt(giftPriceEditText.getText().toString());
            }

            mMainActivityViewModel.addNewGift(name, price, listName, personName, false);

        });
        alert.setNegativeButton("Anuluj", (dialog, whichButton) -> {
        });
        alert.show();
    }

    @Override
    public void onClick(View v) {
        if (v == fabAddGift) {
            Intent intent = GiftsActivity.this.getIntent();
            String value = intent.getStringExtra("key");
            String listName = intent.getStringExtra("key2");
            addNewGift(listName, value);
        }
    }

    @Override
    public void getBoughtGiftPrice(int PriceOfCheckedGifts) {
        progressBarBudget.setProgress(0);
        Intent intent = GiftsActivity.this.getIntent();
        float budget = intent.getIntExtra("key3", 0);
        CheckedGiftsSumPrice = PriceOfCheckedGifts;
        Log.d(TAG, "getBoughtGiftPrice: " + CheckedGiftsSumPrice);
        //TODO Zrobic z tego floaty bo pokazuje zero :(
        float progressPercent = CheckedGiftsSumPrice / budget * 100;
        Log.d(TAG, "getBoughtGiftPriceProgress: " + progressPercent);

        progressBarBudget.setProgress((int) progressPercent);
        //Toast.makeText(this, "PRICE" + PriceOfCheckedGift, Toast.LENGTH_SHORT).show();
    }



    @Override
    public void getGiftCheck(boolean isGiftChecked, String name, int price) {
        //If gift is checked or unchecked, i have to update firestore info.
        updateGift(isGiftChecked, name, price);
        Toast.makeText(this, "isChecked " + isGiftChecked, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onError(Exception e) {
        Toast.makeText(this, "Błąd: " + e, Toast.LENGTH_SHORT).show();
    }
}