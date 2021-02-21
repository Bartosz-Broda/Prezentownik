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
import android.content.DialogInterface;
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
        float personBudget = intent.getFloatExtra("key3", 0);

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
        mMainActivityViewModel.initPersons(listName);
        mMainActivityViewModel.getSelectedPersonData(listName, value);

        initRecyclerView();
        giftsProgress.setVisibility(View.VISIBLE);

        mMainActivityViewModel.getGiftModelData().observe(this, new Observer<List<Gift>>() {
            @Override
            public void onChanged(List<Gift> gifts) {
                giftsProgress.startAnimation(fadeOutAnim);
                adapter.setGiftModels(gifts);
                adapter.notifyDataSetChanged();
                int giftsBought = 0;
                int size = gifts.size() -1;
                float priceOfAllGifts = 0;
                while (size >=0 ){
                    if(gifts.get(size).getIsBought()){
                        giftsBought += 1;
                        priceOfAllGifts += gifts.get(size).getGiftPrice();
                    }
                    size -= 1;
                }
                updatePersonGiftQuantity(giftsBought, priceOfAllGifts);
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

    private void updateGift(boolean isGiftChecked, String name, float price) {
        Intent intent = GiftsActivity.this.getIntent();
        String personName = intent.getStringExtra("key");
        String listName = intent.getStringExtra("key2");

        mMainActivityViewModel.addNewGift(name, price, listName, personName, isGiftChecked);
    }

    public void updatePersonGiftQuantity(int giftsBought, float priceOfAllGifts) {
        Intent intent = GiftsActivity.this.getIntent();
        String personName = intent.getStringExtra("key");
        String listName = intent.getStringExtra("key2");
        float personBudget = intent.getFloatExtra("key3", 0);
        //int giftQuantity = mMainActivityViewModel.getSelectedPersonModelData().getValue().get(0).getGiftQuantity();
        int giftQuantity = adapter.getItemCount();
        //giftQuantity += 1;
        mMainActivityViewModel.addNewPerson(personName, personBudget, listName, giftQuantity, giftsBought, priceOfAllGifts);
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
            float price = 0;
            String name = giftNameEditText.getText().toString();

            if (!giftPriceEditText.getText().toString().equals("")) {
                price = Float.parseFloat(giftPriceEditText.getText().toString());
            }

            mMainActivityViewModel.addNewGift(name, price, listName, personName, false);
            //TODO: OGARNAC UPDATE ILOSCI I KOSZTU PREZENTOW ---!!!___

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
        float budget = intent.getFloatExtra("key3", 0);
        CheckedGiftsSumPrice = PriceOfCheckedGifts;
        Log.d(TAG, "getBoughtGiftPrice: " + CheckedGiftsSumPrice);
        Log.d(TAG, "getBoughtGiftPriceBudget: " + budget);

        //setting progressbar depending on value of bought gifts
        float progressPercent = CheckedGiftsSumPrice / budget * 100;
        Log.d(TAG, "getBoughtGiftPriceProgress: " + progressPercent);

        progressBarBudget.setProgress((int) progressPercent);

        if (progressPercent > 100){
            final AlertDialog.Builder dialog = new AlertDialog.Builder(GiftsActivity.this);
            dialog.setTitle("Alert")
                    .setMessage("Kupując ten prezent przekraczasz założony budżet!")
                    .setNegativeButton("Ok", (paramDialogInterface, paramInt) -> { });
            dialog.show();
        }
    }



    @Override
    public void getGiftCheck(boolean isGiftChecked, String name, float price) {
        //If gift is checked or unchecked, i have to update firestore info.
        updateGift(isGiftChecked, name, price);
        //Toast.makeText(this, "isChecked " + isGiftChecked, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void deleteGift(String giftName) {
        Intent intent = GiftsActivity.this.getIntent();
        String value = intent.getStringExtra("key");
        String listName = intent.getStringExtra("key2");

        final AlertDialog.Builder dialog = new AlertDialog.Builder(GiftsActivity.this);
        dialog.setMessage("Czy chcesz usunąć ten prezent?")
                .setPositiveButton("Usuń", (paramDialogInterface, paramInt) -> mMainActivityViewModel.deleteGift(giftName, listName, value))
                .setNegativeButton("Anuluj", (paramDialogInterface, paramInt) -> {
                });
        dialog.show();

    }

    @Override
    public void onError(Exception e) {
        Toast.makeText(this, "Błąd: " + e, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}