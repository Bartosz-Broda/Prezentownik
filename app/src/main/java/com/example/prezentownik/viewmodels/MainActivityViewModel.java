package com.example.prezentownik.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.prezentownik.models.Gift;
import com.example.prezentownik.models.Person;
import com.example.prezentownik.repositories.GiftRepository;
import com.example.prezentownik.repositories.ListRepository;
import com.example.prezentownik.repositories.PersonRepository;

import java.util.List;

public class MainActivityViewModel extends ViewModel implements PersonRepository.OnFirestoreTaskComplete, GiftRepository.OnFirestoreTaskComplete {

    private MutableLiveData<List<Gift>> mPersons;
    private MutableLiveData<List<String>> giftLists;
    private PersonRepository mRepo;
    private ListRepository listRepo;
    private GiftRepository giftRepo;

    public void init(){
        listRepo = ListRepository.getInstance();
    }

    public void initPersons(String list){
        mRepo = PersonRepository.getInstance(this);
        mRepo.getPersonData(list);
    }

    public void initGifts(String list, String selectedPerson){
        giftRepo = GiftRepository.getInstance(this);
        giftRepo.getGiftData(list, selectedPerson);
    }

    private MutableLiveData<List<Person>> personModelData = new MutableLiveData<>();
    public LiveData<List<Person>> getPersonModelData() {
        return personModelData;
    }

    private MutableLiveData<List<Gift>> giftModelData = new MutableLiveData<>();
    public LiveData<List<Gift>> getGiftModelData() {
        return giftModelData;
    }



    public void SetNewList (String name, int budget){
        listRepo.CreateNewGiftList(name, budget);
    }

    public void addNewPerson (String name, int budget, String list){mRepo.addNewPerson(name, budget, list);}

    @Override
    public void getPersonDataAdded(List<Person> PersonModelList) {
        personModelData.setValue(PersonModelList);
    }

    @Override
    public void getGiftDataAdded(List<Gift> GiftModelList) {
        giftModelData.setValue(GiftModelList);
    }

    @Override
    public void onError(Exception e) {

    }
}
