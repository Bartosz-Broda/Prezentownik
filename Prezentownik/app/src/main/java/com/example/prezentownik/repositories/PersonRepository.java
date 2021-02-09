package com.example.prezentownik.repositories;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.prezentownik.models.GiftList;
import com.example.prezentownik.models.Person;
import com.example.prezentownik.models.Person;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.content.ContentValues.TAG;
import static com.example.prezentownik.utils.HelperClass.logErrorMessage;

public class PersonRepository {
    private static PersonRepository instance;
    //private ArrayList<Person> dataset = new ArrayList<>();
    private PersonRepository.OnFirestoreTaskComplete onFirestoreTaskComplete;

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore rootRef = FirebaseFirestore.getInstance();

    public static PersonRepository getInstance(PersonRepository.OnFirestoreTaskComplete onFirestoreTaskComplete){
        if(instance == null){
            instance = new PersonRepository(onFirestoreTaskComplete);
        }
        return instance;
    }

    public PersonRepository(PersonRepository.OnFirestoreTaskComplete onFirestoreTaskComplete){
        this.onFirestoreTaskComplete = onFirestoreTaskComplete;
    }

    public void addNewPerson(String personName, int personBudget, String list){
        Person newPerson = new Person(personName, personBudget);
        CollectionReference personsRef = rootRef.collection("users").document(Objects.requireNonNull(firebaseAuth.getUid())).collection("Giftlists").document(list).collection("Persons");
        DocumentReference personRef = personsRef.document(personName);

        personRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if(!document.exists()){
                    personRef.set(newPerson).addOnCompleteListener(listCreationTask -> {
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

    public void getPersonData(String list) {
        if (list != "") {

            final CollectionReference personsRef = rootRef.collection("users").document(firebaseAuth.getUid()).collection("Giftlists").document(list).collection("Persons");
            personsRef
                    .addSnapshotListener((value, e) -> {
                        if (e != null) {
                            onFirestoreTaskComplete.onError(e);
                            Log.w(TAG, "Listen failed.", e);
                            return;
                        }
                        //                                      creates a list of data of type Person
                        onFirestoreTaskComplete.getPersonDataAdded(value.toObjects(Person.class));

                        //List<String> cities = new ArrayList<>();
                        /*for (QueryDocumentSnapshot doc : value) {
                            if (doc.get("name") != null) {
                                cities.add(doc.getString("name"));
                            }
                        }*/
                        //Log.d(TAG, "Current cites in CA: " + cities);
                    });


            //--------------------------------------------------------

            /*CollectionReference personsRef = rootRef.collection("users").document(firebaseAuth.getUid()).collection("Giftlists").document(list).collection("Persons");
            personsRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        //                                      creates a list of data of type Person
                        onFirestoreTaskComplete.getPersonDataAdded(task.getResult().toObjects(Person.class));
                    } else {
                        onFirestoreTaskComplete.onError(task.getException());
                    }
                }
            });*/
        }
    }

    //interface for sending data to viewmodel
    //I'm using it for my querysnapshot task from above
    public interface OnFirestoreTaskComplete {
        void getPersonDataAdded(List<Person> PersonModelList);
        void onError(Exception e);
    }

    /*// Pretend to get data from Internet or something
    public MutableLiveData<List<Person>> getPersons(){
        setPersons();

        MutableLiveData<List<Person>> data = new MutableLiveData<>();
        data.setValue(dataset);
        return data;
    }


    private void setPersons(){

        dataset.add(
                new Person("Wojtek", 200)
        );
        dataset.add(
                new Person("Halina", 150)
        );
        dataset.add(
                new Person("Lenka", 300)
        );
        dataset.add(
                new Person("Horacy", 75)
        );
        dataset.add(
                new Person("Anka",200)
        );
    }*/
}