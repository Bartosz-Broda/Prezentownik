package com.example.prezentownik;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prezentownik.MainActivity;
import com.example.prezentownik.R;
import com.example.prezentownik.models.User;
import com.example.prezentownik.viewmodels.AuthenticationViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button registerButton;
    private TextView textViewSignin;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    private AuthenticationViewModel registerViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() != null){
            //goes to the profile activity, because user is already logged in
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }

        initUI();
        initViewModel();

    }

    private void initUI(){
        progressDialog = new ProgressDialog (this);
        registerButton = findViewById(R.id.buttonRegister);
        editTextEmail = findViewById(R.id.adresEmail);
        editTextPassword = findViewById(R.id.haslo);
        textViewSignin = findViewById(R.id.textView);
        registerButton.setOnClickListener(this);
        textViewSignin.setOnClickListener(this);
    }

    private void initViewModel(){
        registerViewModel = new ViewModelProvider(this).get(AuthenticationViewModel.class);
    }

    private void registerUser(){
        final String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        //checking if the password or email is empty
        if(TextUtils.isEmpty(email)){
            //no email passed by user
            Toast.makeText(this, "Podaj swój adres email", Toast.LENGTH_SHORT).show();
            //Stops the function execution
            return;
        }
        if(TextUtils.isEmpty(password)){
            //no password passed by user
            Toast.makeText(this, "Musisz podać hasło", Toast.LENGTH_SHORT).show();
            return;
        }
        if(password.length() < 8){
            Toast.makeText(this, "Hasło musi mieć co najmniej 8 znaków", Toast.LENGTH_SHORT).show();
            return;
        }

        //Everything OK, showing progress dialog at first
        progressDialog.setMessage("Rejestrowanie...");
        progressDialog.show();

        //Registering new user
        registerViewModel.createNewUserWithEmailAndPassword(email, password);
        registerViewModel.createdUserLiveData.observe(this, user -> {
            if (user.isCreated){
                progressDialog.dismiss();
                finish();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }

            if (user.error != null){
                progressDialog.dismiss();
                Toast.makeText(this, user.error, Toast.LENGTH_SHORT).show();
            }

        });


    }

    @Override
    public void onClick(View view) {
        if(view == registerButton){
            registerUser();
        }
        else if (view == textViewSignin){
            //opens login activity here
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

    }
}
