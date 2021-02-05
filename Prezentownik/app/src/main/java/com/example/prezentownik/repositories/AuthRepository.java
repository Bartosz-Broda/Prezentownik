package com.example.prezentownik.repositories;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.prezentownik.RegisterActivity;
import com.example.prezentownik.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;
import java.util.concurrent.Executor;

import static android.content.ContentValues.TAG;
import static com.example.prezentownik.utils.HelperClass.logErrorMessage;

public class AuthRepository {
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
    private CollectionReference usersRef = rootRef.collection("users");

    public MutableLiveData<User> firebaseSignInWithEmailandPassword(String email, String password) {
        MutableLiveData<User> authenticatedUserMutableLiveData = new MutableLiveData<>();
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(authTask -> {
            if (authTask.isSuccessful()){

                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if(firebaseUser != null){
                    String uid = firebaseUser.getUid();
                    String eMail = firebaseUser.getEmail();
                    User user = new User(eMail, uid);
                    authenticatedUserMutableLiveData.setValue(user);
                } else {
                    logErrorMessage(Objects.requireNonNull(authTask.getException()).getMessage());
                }
            }
        });
        Log.d(TAG, "firebaseSignInWithEmailandPassword: "+ authenticatedUserMutableLiveData);
        return authenticatedUserMutableLiveData;
    }

    public MutableLiveData<User> firebaseCreateNewUserWithEmailAndPassword(String email, String password){
        MutableLiveData<User> newUserMutableLiveData = new MutableLiveData<>();

    //TODO SKONCZYC TO. ZWRACA TO MA DANE USERA (CHYBA)

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        //user successfully registered and logged in
                        //we will start profile activity here and display a toast
                        //We will store user email in database
                        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

                        User authenticatedUser = new User(email, firebaseUser.getUid());

                        DocumentReference uidRef = usersRef.document(Objects.requireNonNull(firebaseUser.getUid()));

                        uidRef.get().addOnCompleteListener(uidTask -> {
                            if (uidTask.isSuccessful()) {
                                DocumentSnapshot document = uidTask.getResult();
                                if (!document.exists()) {
                                    uidRef.set(authenticatedUser).addOnCompleteListener(userCreationTask -> {
                                        if (userCreationTask.isSuccessful()) {
                                            authenticatedUser.isCreated = true;
                                            newUserMutableLiveData.setValue(authenticatedUser);
                                        } else {
                                            logErrorMessage(userCreationTask.getException().getMessage() + "KURWA");
                                            authenticatedUser.error = task.getException().getMessage();
                                            newUserMutableLiveData.setValue(authenticatedUser);
                                        }
                                    });
                                } else {
                                    newUserMutableLiveData.setValue(authenticatedUser);
                                }
                            } else {
                                logErrorMessage(uidTask.getException().getMessage() + "KURAW1");
                                authenticatedUser.error = task.getException().getMessage();
                                newUserMutableLiveData.setValue(authenticatedUser);
                            }
                        });

                    } else {
                        User authenticatedUser = new User(email, password);
                        logErrorMessage(task.getException().getMessage()+ "KURWA3");
                        authenticatedUser.error = task.getException().getMessage();
                        newUserMutableLiveData.setValue(authenticatedUser);
                        }
                });
        return newUserMutableLiveData;
    }
}