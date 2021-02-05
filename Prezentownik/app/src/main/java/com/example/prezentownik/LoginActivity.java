package com.example.prezentownik;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prezentownik.viewmodels.AuthenticationViewModel;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "LOGIN";
    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button signInButton;
    private TextView registerTextView;
    private TextView resetPassword;

    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    private AuthenticationViewModel loginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        progressDialog = new ProgressDialog (this);
        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() != null){
            //goes to the main activity, because user is already logged in
            Log.d(TAG, "onCreate: Login:" + firebaseAuth.getCurrentUser());
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }
        initUI();
        initLoginViewModel();

    }

    private void initUI(){
        editTextEmail = findViewById(R.id.signInEmail);
        editTextPassword = findViewById(R.id.signInPassword);
        signInButton = findViewById(R.id.signInButton);
        registerTextView = findViewById(R.id.registerTextView);
        resetPassword = findViewById(R.id.resetPasswordTextView);

        signInButton.setOnClickListener(this);
        registerTextView.setOnClickListener(this);
        resetPassword.setOnClickListener(this);
    }

    private void initLoginViewModel(){
        loginViewModel = new ViewModelProvider(this).get(AuthenticationViewModel.class);
    }

    private void signInUser(){
        String email = editTextEmail.getText().toString().trim();
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

        //Everything OK, showing progress dialog at first
        progressDialog.setMessage("Logowanie...");
        progressDialog.show();

        //TODO TUTAJ GDZIES ODWOLAC SIE DO LOGOWANIA W Viewmodelu
        loginViewModel.singInWithEmailAndPassword(email, password);
        loginViewModel.authenticatedUserLiveData.observe(this, authenticatedUser -> {
            if (authenticatedUser.uid != null){
                progressDialog.dismiss();
                finish();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }else{
                progressDialog.dismiss();
            }
        });

    }


    @Override
    public void onClick(View v) {
        if(v == signInButton){
            //starting sign in method
            signInUser();
        }
        if(v == registerTextView){
            //going to the register activity
            finish();
            startActivity(new Intent(this, RegisterActivity.class));
        }
        if(v==resetPassword){
            finish();
            startActivity(new Intent(this, ResetPasswordActivity.class));
        }

    }
}
