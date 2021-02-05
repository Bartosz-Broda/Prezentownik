package com.example.prezentownik.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.prezentownik.models.GiftList;
import com.example.prezentownik.models.Person;
import com.example.prezentownik.repositories.ListRepository;
import com.example.prezentownik.repositories.PersonRepository;

import java.util.List;

import static android.content.ContentValues.TAG;

public class MainActivityViewModel extends ViewModel {

    private MutableLiveData<List<Person>> mPersons;
    private MutableLiveData<List<String>> giftLists;
    private PersonRepository mRepo;
    private ListRepository listRepo;

    public void init(){
        if(mPersons != null){
            return;
        }
        mRepo = PersonRepository.getInstance();
        mPersons = mRepo.getPersons();

        listRepo = ListRepository.getInstance();
    }

    public LiveData<List<Person>> getPerson(){
        return mPersons;
    }

    public LiveData<List<String>> getMyLists(){
        Log.d(TAG, "getMyLists: wywaołane");
        giftLists = listRepo.retrieveMyLists();
        return giftLists; }

    public void SetNewList (String name, int budget){
        listRepo.CreateNewGiftList(name, budget);
    }

}
