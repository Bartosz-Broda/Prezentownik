package com.example.prezentownik;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.prezentownik.adapters.RecyclerAdapter;
import com.example.prezentownik.adapters.TestowyAdapter;
import com.example.prezentownik.models.GiftList;
import com.example.prezentownik.models.Person;
import com.example.prezentownik.viewmodels.MainActivityViewModel;
import com.example.prezentownik.viewmodels.TestowyViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivity";
    private RecyclerView listViewTestowy;
    private TestowyViewModel testowyViewModel;
    private TestowyAdapter adapterTest;

    //vars
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mGiftQuantity = new ArrayList<>();

    private RecyclerView recyclerView;
    private RecyclerAdapter adapter;
    private MainActivityViewModel mMainActivityViewModel;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    FloatingActionButton fabAddList;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: started.");

        firebaseAuth = FirebaseAuth.getInstance();

        listViewTestowy = findViewById(R.id.recycler_view_testowy);
        adapterTest = new TestowyAdapter();

        testowyViewModel = new ViewModelProvider(this).get(TestowyViewModel.class);
        testowyViewModel.getTestListModelData().observe(this, new Observer<List<GiftList>>() {
            @Override
            public void onChanged(List<GiftList> giftLists) {
                adapterTest.setListModels(giftLists);
                adapter.notifyDataSetChanged();
            }
        });

        mMainActivityViewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
        mMainActivityViewModel.init();
        mMainActivityViewModel.getPerson().observe(this, people -> adapter.notifyDataSetChanged());

        listViewTestowy.setLayoutManager(new LinearLayoutManager(this));
        listViewTestowy.setHasFixedSize(true);
        listViewTestowy.setAdapter(adapterTest);
        initUI();
        initRecyclerView();
    }


    private void initUI() {
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        fabAddList = findViewById(R.id.FAB_Add_list);
        fabAddList.setOnClickListener(this);

        toolbar = findViewById(R.id.my_toolbar);
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        setSupportActionBar(toolbar);

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        List<String> myListofGiftLists = mMainActivityViewModel.getMyLists().getValue();

        if (!myListofGiftLists.isEmpty()) {
            SubMenu subMenu1 = navigationView.getMenu().addSubMenu(Menu.NONE, 1, 1, "Listy z zakupami");
            for (String element : myListofGiftLists) {
                subMenu1.add(3, 1, 1, element);
                subMenu1.add(3, 2, 2, element);
            }
        } else {
            SubMenu subMenu1 = navigationView.getMenu().addSubMenu(Menu.NONE, 1, 1, "Listy z zakupami");
            subMenu1.add(3, 1, 1, "Siema");
        }
    }

    private void initRecyclerView() {
        Log.d(TAG, "initRecyclerView: init recycelrview");
        recyclerView = findViewById(R.id.recycler_view);
        adapter = new RecyclerAdapter(this, mMainActivityViewModel.getPerson().getValue());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void addNewGiftList() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText listNameEditText = new EditText(this);
        listNameEditText.setHint("Nazwa nowej listy");
        layout.addView(listNameEditText);
        final EditText listBudgetEditText = new EditText(this);
        listBudgetEditText.setHint("Budżet dla tej listy (opcjonalnie)");
        layout.addView(listBudgetEditText);

        layout.setDividerPadding(25);
        layout.setPadding(35, 0, 35, 25);

        alert.setMessage("Nazwij swoją listę:");
        alert.setTitle("Nowa lista prezentów");

        alert.setView(layout);

        alert.setPositiveButton("Zatwierdź", (dialog, whichButton) -> {
            //What ever you want to do with the value
            String newListName = listNameEditText.getText().toString();
            int newListBudget = Integer.parseInt(listBudgetEditText.getText().toString());

            mMainActivityViewModel.SetNewList(newListName, newListBudget);
            Log.d(TAG, "addNewGiftList: " + newListName + newListBudget);

            //SubMenu subMenu1 = navigationView.getMenu().addSubMenu(Menu.NONE, 1, 1, "Listy z zakupami");

        });

        alert.setNegativeButton("Anuluj", (dialog, whichButton) -> {
        });

        alert.show();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.account_logout) {
            Log.d(TAG, "onClick: Logout");
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        if (v == fabAddList) {
            addNewGiftList();
        }
    }
}