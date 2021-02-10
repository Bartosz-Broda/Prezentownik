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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.example.prezentownik.adapters.RecyclerAdapter;
import com.example.prezentownik.models.GiftList;
import com.example.prezentownik.models.Gift;
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
    private TestowyViewModel testowyViewModel;

    //vars
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mGiftQuantity = new ArrayList<>();

    private RecyclerView recyclerView;
    private RecyclerAdapter adapter;
    private MainActivityViewModel mMainActivityViewModel;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    ProgressBar progressBar;
    private ProgressBar listProgress;
    private Animation fadeInAnim;
    private Animation fadeOutAnim;
    FloatingActionButton fabAddList;
    FloatingActionButton fabAddPerson;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: started.");

        firebaseAuth = FirebaseAuth.getInstance();

        testowyViewModel = new ViewModelProvider(this).get(TestowyViewModel.class);

        mMainActivityViewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
        mMainActivityViewModel.init();

        initUI();
    }


    private void initUI() {
        drawerLayout = findViewById(R.id.drawer_layout);
        progressBar = findViewById(R.id.progress);
        listProgress = findViewById(R.id.list_progress);
        fadeInAnim = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        fadeOutAnim = AnimationUtils.loadAnimation(this, R.anim.fade_out);
        navigationView = findViewById(R.id.nav_view);
        fabAddList = findViewById(R.id.FAB_Add_list);
        fabAddList.setOnClickListener(this);
        fabAddPerson = findViewById(R.id.fabAddPerson);
        fabAddPerson.setOnClickListener(this);

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

        testowyViewModel.getTestListModelData().observe(this, new Observer<List<GiftList>>() {
            @Override
            public void onChanged(List<GiftList> giftLists) {
                int x = 0;
                if (!giftLists.isEmpty()) {
                    SubMenu subMenu1 = navigationView.getMenu().addSubMenu(Menu.NONE, 1, 1, "Twoje listy prezentów");
                    for (GiftList element : giftLists) {
                        subMenu1.add(3, x, x, element.getListName());
                        x += 1;
                    }
                } else {
                    SubMenu subMenu2 = navigationView.getMenu().addSubMenu(Menu.NONE, 1, 1, "Twoje listy prezentów");
                    subMenu2.add(3, 1, 1, "Brak list z prezentami");
                }
            }
        });
    }

    private void initRecyclerView() {
        Log.d(TAG, "initRecyclerView: init recycelrview");
        recyclerView = findViewById(R.id.recycler_view);
        adapter = new RecyclerAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void addNewGiftList() {
        showAlert(1);
    }
    private void addNewPerson() {
        showAlert(2);
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
        if (item.getItemId() >= 0 && item.getItemId() < 100) {
            Log.d(TAG, "onNavigationItemSelected: FLAGA " + item.getTitle() + " " + item.getItemId());

            //---Getting info about selected list.---
            List<GiftList> selectedList = testowyViewModel.getTestListModelData().getValue();
            String selecteedListName = selectedList.get(item.getItemId()).getListName();
            int selectedListBudget = selectedList.get(item.getItemId()).getListBudget();

            //Changing UI depending on chosen list
            listProgress.setVisibility(View.VISIBLE);
            mMainActivityViewModel.initPersons(selecteedListName);
            toolbar.setTitle(selecteedListName);
            initRecyclerView();
            mMainActivityViewModel.getPersonModelData().observe(this, new Observer<List<Person>>() {
                        @Override
                        public void onChanged(List<Person> people) {
                            listProgress.startAnimation(fadeOutAnim);
                            adapter.setPersonModels(people);
                            adapter.setListName(selecteedListName);
                            adapter.notifyDataSetChanged();
                        }
                    });


            fabAddPerson.setVisibility(View.VISIBLE);

            if (selectedListBudget != 0) {
                toolbar.setTitleMarginBottom(38);
                toolbar.setSubtitle("Wykorzystany budżet: 0/" + selectedListBudget);
                progressBar.setVisibility(View.VISIBLE);
                drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                toolbar.setTitleMarginBottom(64);
                toolbar.setSubtitle("");
                progressBar.setVisibility(View.GONE);
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        }
        return true;
    }

    private void showAlert(int code){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText listNameEditText = new EditText(this);
        if(code==1){
            listNameEditText.setHint("Nazwa nowej listy");
        }else if(code == 2){
            listNameEditText.setHint("Imię osoby");
        }

        layout.addView(listNameEditText);
        final EditText listBudgetEditText = new EditText(this);
        if(code==1){
            listBudgetEditText.setHint("Budżet dla tej listy (opcjonalnie)");
        }else if(code == 2){
            listBudgetEditText.setHint("Budżet dla tej osoby (opcjonalnie)");
        }
        layout.addView(listBudgetEditText);

        layout.setDividerPadding(25);
        layout.setPadding(35, 0, 35, 25);

        if (code == 1) {
            alert.setTitle("Nowa lista prezentów");
        } else if (code == 2){
            alert.setTitle("Dodaj osobę do listy");
        }
        alert.setView(layout);

        alert.setPositiveButton("Zatwierdź", (dialog, whichButton) -> {
            //What ever you want to do with the value
            int budget = 0;
            String name = listNameEditText.getText().toString();

            if (!listBudgetEditText.getText().toString().equals("")) {
                budget = Integer.parseInt(listBudgetEditText.getText().toString());
            }

            if (code == 1) {
                mMainActivityViewModel.SetNewList(name, budget);
                Log.d(TAG, "addNewGiftList: " + name + budget);
                //SubMenu subMenu1 = navigationView.getMenu().addSubMenu(Menu.NONE, 1, 1, "Listy z zakupami");
            } else if (code == 2){
                String list = (String) toolbar.getTitle();
                mMainActivityViewModel.addNewPerson(name,budget,list);
            }

        });
        alert.setNegativeButton("Anuluj", (dialog, whichButton) -> {
        });
        alert.show();
    }

    @Override
    public void onClick(View v) {
        if (v == fabAddList) {
            addNewGiftList();
        }
        if (v == fabAddPerson){
            addNewPerson();
        }
    }
}