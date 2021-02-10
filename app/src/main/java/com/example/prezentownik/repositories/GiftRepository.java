package com.example.prezentownik.repositories;

import android.util.Log;

import com.example.prezentownik.models.Gift;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.Objects;

import static android.content.ContentValues.TAG;
import static com.example.prezentownik.utils.HelperClass.logErrorMessage;

public class GiftRepository {
    private static GiftRepository instance;
    //private ArrayList<Person> dataset = new ArrayList<>();
    private GiftRepository.OnFirestoreTaskComplete onFirestoreTaskComplete;

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore rootRef = FirebaseFirestore.getInstance();

    public static GiftRepository getInstance(GiftRepository.OnFirestoreTaskComplete onFirestoreTaskComplete){
        if(instance == null){
            instance = new GiftRepository(onFirestoreTaskComplete);
        }
        return instance;
    }

    public GiftRepository(GiftRepository.OnFirestoreTaskComplete onFirestoreTaskComplete){
        this.onFirestoreTaskComplete = onFirestoreTaskComplete;
    }

    public void addNewGift(String personName, int personBudget, String list, String selectedPerson){
        Gift newGift = new Gift(personName, personBudget);
        CollectionReference giftsRef = rootRef.collection("users").document(Objects.requireNonNull(firebaseAuth.getUid()))
                .collection("Giftlists").document(list)
                .collection("Persons").document(selectedPerson)
                .collection("Gifts");
        DocumentReference giftRef = giftsRef.document(personName);

        giftRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if(!document.exists()){
                    giftRef.set(newGift).addOnCompleteListener(listCreationTask -> {
                        Log.d(TAG, "CreateNewGiftList: ELOELO4");
                        if (listCreationTask.isSuccessful()) {
                            Log.d(TAG, "CreateNewGiftList: ELOEL5");
                        } else {
                            logErrorMessage(listCreationTask.getException().getMessage() + "KURWA22");
                        }
                    });
                }
            }
        });
    }

    public void getGiftData(String list, String selectedPerson) {
        if (list != "") {

            final CollectionReference giftsRef = rootRef.collection("users").document(firebaseAuth.getUid())
                    .collection("Giftlists").document(list)
                    .collection("Persons").document(selectedPerson)
                    .collection("Gifts");

            giftsRef.addSnapshotListener((value, e) -> {
                        if (e != null) {
                            onFirestoreTaskComplete.onError(e);
                            Log.w(TAG, "Listen failed.", e);
                            return;
                        }
                        //                                      creates a list of data of type Person
                        onFirestoreTaskComplete.getGiftDataAdded(value.toObjects(Gift.class));
                    });
        }
    }

    //interface for sending data to viewmodel
    //I'm using it for my querysnapshot task from above
    public interface OnFirestoreTaskComplete {
        void getGiftDataAdded(List<Gift> PersonModelList);
        void onError(Exception e);
    }
}
