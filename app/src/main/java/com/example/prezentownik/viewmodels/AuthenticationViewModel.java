package com.example.prezentownik.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.prezentownik.models.User;
import com.example.prezentownik.repositories.AuthRepository;

public class AuthenticationViewModel extends AndroidViewModel {
    private AuthRepository authRepository;
    public LiveData<User> authenticatedUserLiveData;
    public LiveData<User> createdUserLiveData;

    public AuthenticationViewModel(Application application) {
        super(application);
        authRepository = new AuthRepository();
    }

    public void singInWithEmailAndPassword(String email, String password){
        authenticatedUserLiveData = authRepository.firebaseSignInWithEmailandPassword(email, password);
    }

    public void createNewUserWithEmailAndPassword(String email, String password){
        createdUserLiveData = authRepository.firebaseCreateNewUserWithEmailAndPassword(email, password);
    }
}
