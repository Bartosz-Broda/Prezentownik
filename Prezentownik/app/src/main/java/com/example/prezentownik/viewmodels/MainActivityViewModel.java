package com.example.prezentownik.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.prezentownik.models.GiftList;
import com.example.prezentownik.models.Person;
import com.example.prezentownik.repositories.ListRepository;
import com.example.prezentownik.repositories.PersonRepository;
import com.example.prezentownik.repositories.TestRepository;

import java.util.List;

import static android.content.ContentValues.TAG;

public class MainActivityViewModel extends ViewModel implements PersonRepository.OnFirestoreTaskComplete {

    private MutableLiveData<List<Person>> mPersons;
    private MutableLiveData<List<String>> giftLists;
    private PersonRepository mRepo;
    private ListRepository listRepo;

    public void init(){
        //if(personModelData != null){
          //  return;
        //}
        //mPersons = mRepo.getPersons();
        listRepo = ListRepository.getInstance();
    }

    public void initPersons(String list){
        mRepo = PersonRepository.getInstance(this);
        mRepo.getPersonData(list);
    }

    /*public LiveData<List<Person>> getPerson(){
        return mPersons;
    }*/

    /*public LiveData<List<String>> getMyLists(){
        Log.d(TAG, "getMyLists: wywaołane");
        giftLists = listRepo.retrieveMyLists();
        return giftLists; }*/
    private MutableLiveData<List<Person>> personModelData = new MutableLiveData<>();

    public LiveData<List<Person>> getPersonModelData() {
        return personModelData;
    }

    //private PersonRepository personRepository= new PersonRepository(this);

    public void SetNewList (String name, int budget){
        listRepo.CreateNewGiftList(name, budget);
    }

    public void addNewPerson (String name, int budget, String list){mRepo.addNewPerson(name, budget, list);}

    @Override
    public void getPersonDataAdded(List<Person> PersonModelList) {
        personModelData.setValue(PersonModelList);
    }

    @Override
    public void onError(Exception e) {

    }
}
