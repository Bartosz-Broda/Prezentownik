package com.example.prezentownik.repositories;

import androidx.annotation.NonNull;

import com.example.prezentownik.models.GiftList;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class TestRepository {
    FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
    private TestRepository.OnFirestoreTaskComplete onFirestoreTaskComplete;

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private CollectionReference listsRef = rootRef.collection("users").document(firebaseAuth.getUid()).collection("Giftlists");

    public TestRepository(OnFirestoreTaskComplete onFirestoreTaskComplete){
        this.onFirestoreTaskComplete = onFirestoreTaskComplete;
    }

    public void getListData(){
        listsRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    //                                      creates a list of data of type GiftList
                    onFirestoreTaskComplete.getListDataAdded(task.getResult().toObjects(GiftList.class));
                } else {
                    onFirestoreTaskComplete.onError(task.getException());
                }
            }
        });
    }

    //interface for sending data to viewmodel
    //I'm using it for my querysnapshot task from above
    public interface OnFirestoreTaskComplete {
        void getListDataAdded(List<GiftList> giftListModelList);
        void onError(Exception e);
    }

}
