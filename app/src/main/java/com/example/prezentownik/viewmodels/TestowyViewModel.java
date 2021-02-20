package com.example.prezentownik.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.prezentownik.models.GiftList;
import com.example.prezentownik.repositories.TestRepository;

import java.util.List;

public class TestowyViewModel extends ViewModel implements TestRepository.OnFirestoreTaskComplete {
    private TestRepository testRepo;

    public void init(){
        testRepo = TestRepository.getInstance(this);
        testRepo.getListData();
    }

    private MutableLiveData<List<GiftList>> testListModelData = new MutableLiveData<>();

    public LiveData<List<GiftList>> getTestListModelData() {
        return testListModelData;
    }



    @Override
    public void getListDataAdded(List<GiftList> giftListModelList) {
        testListModelData.setValue(giftListModelList);
    }

    @Override
    public void onError(Exception e) {

    }
}
