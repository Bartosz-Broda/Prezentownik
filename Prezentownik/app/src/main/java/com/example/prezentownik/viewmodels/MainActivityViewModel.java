package com.example.prezentownik.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.prezentownik.models.Person;
import com.example.prezentownik.repositories.PersonRepository;

import java.util.List;

public class MainActivityViewModel extends ViewModel {

    private MutableLiveData<List<Person>> mPersons;
    private PersonRepository mRepo;

    public void init(){
        if(mPersons != null){
            return;
        }
        mRepo = PersonRepository.getInstance();
        mPersons = mRepo.getPersons();
    }

    public LiveData<List<Person>> getPerson(){
        return mPersons;
    }

}
