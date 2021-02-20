package com.example.prezentownik.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.prezentownik.models.Gift;
import com.example.prezentownik.models.Person;
import com.example.prezentownik.repositories.GiftRepository;
import com.example.prezentownik.repositories.ListRepository;
import com.example.prezentownik.repositories.PersonRepository;

import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;

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

    private MutableLiveData<List<Person>> selectedPersonModelData = new MutableLiveData<>();
    public LiveData<List<Person>> getSelectedPersonModelData() {return selectedPersonModelData;}

    private MutableLiveData<List<Gift>> giftModelData = new MutableLiveData<>();
    public LiveData<List<Gift>> getGiftModelData() {
        return giftModelData;
    }



    public void SetNewList (String name, float budget, int usedBudget){ listRepo.CreateNewGiftList(name, budget, usedBudget); }

    public void addNewPerson (String name, float budget, String list, int giftQuantity, int giftsBought, float checkedGiftsPrice){mRepo.addNewPerson(name, budget, giftQuantity, giftsBought, checkedGiftsPrice, list);}

    public void deletePerson (String personName, String list){mRepo.deleteGift(personName, list);}

    public void addNewGift (String giftName, float giftPrice, String list, String selectedPerson, boolean isBought){giftRepo.addNewGift(giftName, giftPrice, list, selectedPerson, isBought);}

    public void  deleteGift(String giftName, String list, String selectedPerson){giftRepo.deleteGift(giftName, list, selectedPerson);}

    public void updateGiftInfo (String giftName, int giftPrice, String list, String selectedPerson){}

    public void getSelectedPersonData (String list, String personName){mRepo.getSelectedPersonData(list, personName); }

    @Override
    public void getPersonDataAdded(List<Person> PersonModelList) {
        personModelData.setValue(PersonModelList);
    }

    @Override
    public void getSelectedPerson(List<Person> SelectedPersonModelList) {
        selectedPersonModelData.setValue(SelectedPersonModelList);
    }

    @Override
    public void getGiftDataAdded(List<Gift> GiftModelList) {
        giftModelData.setValue(GiftModelList);
    }

    @Override
    public void onError(Exception e) {

    }
}
