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

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener, RecyclerAdapter.OnRecyclerItemClicked {

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
    TextView toolbarTitleTextView;
    TextView progressTitleTextView;
    TextView listNameTextView;
    Button addNewListBigButton;
    Button checkOutNewIdeasBigButton;
    private ProgressBar listProgress;
    private Animation fadeOutAnim;
    FloatingActionButton fabAddList;
    FloatingActionButton fabAddPerson;
    SubMenu subMenu1 = null;
    SubMenu subMenu2 = null;

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
        fadeOutAnim = AnimationUtils.loadAnimation(this, R.anim.fade_out);
        navigationView = findViewById(R.id.nav_view);
        fabAddList = findViewById(R.id.FAB_Add_list);
        fabAddList.setOnClickListener(this);
        fabAddPerson = findViewById(R.id.fabAddPerson);
        fabAddPerson.setOnClickListener(this);
        toolbarTitleTextView = findViewById(R.id.toolbarTitleTextView);
        progressTitleTextView = findViewById(R.id.progressTitleTextView0);
        listNameTextView = findViewById(R.id.listNameTextView);
        addNewListBigButton = findViewById(R.id.button_add_new_list);
        checkOutNewIdeasBigButton = findViewById(R.id.button_check_inspiration);

        addNewListBigButton.setOnClickListener(this);
        checkOutNewIdeasBigButton.setOnClickListener(this);

        recyclerView = findViewById(R.id.recycler_view);
        initRecyclerView();
        recyclerView.setVisibility(View.GONE);

        toolbar = findViewById(R.id.my_toolbar);
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        setSupportActionBar(toolbar);
        toolbar.setTitle("");

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                if (slideOffset == 1) {
                    // If drawer is opened, refresh menu
                    refreshSideMenu();
                }
            }
        };

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        refreshSideMenu();
    }

    private void refreshSideMenu() {
        Log.d(TAG, "refreshSideMenu: KURWA");
        testowyViewModel.init();
        testowyViewModel.getTestListModelData().observe(this, giftLists -> {
            int x = 0;
            if (!giftLists.isEmpty() && subMenu1==null) {
                if(subMenu2 != null){subMenu2.clear(); subMenu2.clearHeader(); subMenu2.close();}
                subMenu1 = navigationView.getMenu().addSubMenu(Menu.NONE, 1, 1, "Twoje listy prezentów");
                for (GiftList element : giftLists) {
                    subMenu1.add(3, x, x, element.getListName());
                    x += 1;
                }
            } else if (!giftLists.isEmpty()){
                subMenu1.removeGroup(3);
                for (GiftList element : giftLists) {
                    subMenu1.add(3, x, x, element.getListName());
                    x += 1;
                }
            } else if (subMenu2 == null) {
                subMenu2 = navigationView.getMenu().addSubMenu(Menu.NONE, 1, 1, "Twoje listy prezentów");
                subMenu2.add(3, 1, 1, "Brak list z prezentami");
            } else {
                subMenu2.removeGroup(3);
                subMenu2.add(3, 1, 1, "Brak list z prezentami");
            }
        });
    }

    private void initRecyclerView() {
        Log.d(TAG, "initRecyclerView: init recycelrview");
        recyclerView = findViewById(R.id.recycler_view);
        adapter = new RecyclerAdapter(this, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        recyclerView.setVisibility(View.VISIBLE);
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
        } else if (recyclerView.getVisibility() == View.GONE){
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setMessage("Czy chcesz wyjść z apliakcji?");
            alert.setPositiveButton("Tak", (dialog, whichButton) -> {
                super.onBackPressed();
            });
            alert.setNegativeButton("Nie", (dialog, whichButton) -> {});
            alert.show();
        } else {
            recyclerView.setVisibility(View.GONE);
            fabAddPerson.setVisibility(View.GONE);
            listNameTextView.setVisibility(View.GONE);
            progressTitleTextView.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            toolbarTitleTextView.setText(R.string.hello);
            toolbarTitleTextView.setVisibility(View.VISIBLE);
            addNewListBigButton.setVisibility(View.VISIBLE);
            checkOutNewIdeasBigButton.setVisibility(View.VISIBLE);
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
            float selectedListBudget = selectedList.get(item.getItemId()).getListBudget();

            //Changing UI depending on chosen list
            addNewListBigButton.setVisibility(View.GONE);
            checkOutNewIdeasBigButton.setVisibility(View.GONE);
            listProgress.setVisibility(View.VISIBLE);
            mMainActivityViewModel.initPersons(selecteedListName);
            listNameTextView.setText(selecteedListName);
            initRecyclerView();
            mMainActivityViewModel.getPersonModelData().observe(this, new Observer<List<Person>>() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onChanged(List<Person> people) {
                            listProgress.startAnimation(fadeOutAnim);
                            adapter.setPersonModels(people);
                            adapter.setListName(selecteedListName);
                            adapter.notifyDataSetChanged();

                            float totalPrice = 0;
                            int size = people.size() -1;
                            while (size >=0 ){
                                totalPrice += people.get(size).getCheckedGiftsPrice();
                                size -= 1;
                            }
                            if (selectedListBudget > 0) {
                                float progressPercent = totalPrice / selectedListBudget * 100;
                                progressBar.setProgress((int) progressPercent);
                                progressBar.setVisibility(View.VISIBLE);
                                listNameTextView.setText(selecteedListName);
                                listNameTextView.setVisibility(View.VISIBLE);
                                toolbarTitleTextView.setVisibility(View.GONE);
                                progressTitleTextView.setVisibility(View.VISIBLE);
                                progressTitleTextView.setText("Wykorzystany budżet: " + totalPrice + "/ " + selectedListBudget);
                                drawerLayout.closeDrawer(GravityCompat.START);
                            } else {
                                toolbarTitleTextView.setText(selecteedListName);
                                toolbarTitleTextView.setVisibility(View.VISIBLE);
                                listNameTextView.setVisibility(View.GONE);
                                progressBar.setVisibility(View.GONE);
                                progressTitleTextView.setVisibility(View.GONE);
                                progressBar.setVisibility(View.GONE);
                                drawerLayout.closeDrawer(GravityCompat.START);
                            }
                        }
                    });


            fabAddPerson.setVisibility(View.VISIBLE);
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

            //If user wants to add new list:
            if (code == 1) {
                mMainActivityViewModel.SetNewList(name, budget, 0);

                Log.d(TAG, "addNewGiftList: " + name + budget);
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        refreshSideMenu();
                    }
                }, 2 * 100);

            //If user wants to add new person
            } else if (code == 2){
                boolean doesExist = false;
                String list = String.valueOf(listNameTextView.getText());

                //Checking if person already exists in list of persons. If so, display toast
                int size = Objects.requireNonNull(mMainActivityViewModel.getPersonModelData().getValue()).size() - 1;
                while (size >= 0){
                    String mName = mMainActivityViewModel.getPersonModelData().getValue().get(size).getName();
                    if (mName.equals(name)){
                        doesExist = true;
                    }
                    size -= 1;
                }

                if (doesExist){
                    Toast.makeText(this, "Taka osoba już istnieje!", Toast.LENGTH_SHORT).show();
                }
                else {mMainActivityViewModel.addNewPerson(name,budget,list, 0 ,0, 0);}
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
        if (v == addNewListBigButton){
            addNewGiftList();
        }
        if (v == checkOutNewIdeasBigButton){
            Toast.makeText(this, "Funkcja jeszcze nie jest dostępna", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void shutDownActivity() {
        onStop();
    }

    @Override
    public void deletePerson(String personName) {
        String list = String.valueOf(listNameTextView.getText());

        final AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
        dialog.setMessage("Czy chcesz usunąć tą osobę ze swojej listy?")
                .setPositiveButton("Usuń", (paramDialogInterface, paramInt) -> mMainActivityViewModel.deletePerson(personName, list))
                .setNegativeButton("Anuluj", (paramDialogInterface, paramInt) -> { });
        dialog.show();

    }
}