package com.example.prezentownik.repositories;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.prezentownik.models.GiftList;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;
import static com.example.prezentownik.utils.HelperClass.logErrorMessage;

public class ListRepository {

    private static ListRepository instance;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
    private CollectionReference listsRef = rootRef.collection("users").document(firebaseAuth.getUid()).collection("Giftlists");
    private ArrayList<String> dataset = new ArrayList<>();

    public static ListRepository getInstance(){
        if(instance == null){
            instance = new ListRepository();
        }
        return instance;
    }


    public MutableLiveData<GiftList> CreateNewGiftList(String listName, int listBudget) {

        MutableLiveData<GiftList> newListMutableLiveData = new MutableLiveData<>();

        GiftList newList = new GiftList(listName, listBudget);
        DocumentReference listRef = listsRef.document(listName);

        listRef.get().addOnCompleteListener(listTask -> {
            if (listTask.isSuccessful()) {
                DocumentSnapshot document = listTask.getResult();
                if (!document.exists()) {
                    listRef.set(newList).addOnCompleteListener(listCreationTask -> {
                        Log.d(TAG, "CreateNewGiftList: ELOELO4");
                        if (listCreationTask.isSuccessful()) {
                            Log.d(TAG, "CreateNewGiftList: ELOEL5");
                            newList.isCreated = true;
                            newListMutableLiveData.setValue(newList);
                        } else {
                            logErrorMessage(listCreationTask.getException().getMessage() + "KURWA22");
                            newList.error = listCreationTask.getException().getMessage();
                            newListMutableLiveData.setValue(newList);
                        }
                    });
                } else {
                    newListMutableLiveData.setValue(newList);
                }
            } else {
                logErrorMessage(listTask.getException().getMessage() + "KURAW11");
                newList.error = listTask.getException().getMessage();
                newListMutableLiveData.setValue(newList);
            }

        });
        Log.d(TAG, "CreateNewGiftList: siemano" + newListMutableLiveData);
        return newListMutableLiveData;

    }

    public MutableLiveData<List<String>> retrieveMyLists() {
        Log.d(TAG, "retrieveMyLists: Wywołane");
        setLists();
        Log.d(TAG, "retrieveMyLists: HEEk" + dataset);

        MutableLiveData<List<String>> data = new MutableLiveData<>();
        data.setValue(dataset);
        return data;
    }


    private void setLists() {
        listsRef.get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Log.d(TAG, document.getId() + " => " + document.getData());
                    dataset.add(document.getId());
                    Log.d(TAG, "onComplete: HEEEEEE" + dataset);
                }
            }else{
                logErrorMessage(task.getException().getMessage() + "Nie tak");
            }
        });

    }
}
