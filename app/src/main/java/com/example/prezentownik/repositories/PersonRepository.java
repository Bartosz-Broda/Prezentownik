package com.example.prezentownik.repositories;

import android.util.Log;

import com.example.prezentownik.models.Gift;
import com.example.prezentownik.models.Person;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

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
        //if(instance == null){
            instance = new PersonRepository(onFirestoreTaskComplete);
       // }
        return instance;
    }

    public PersonRepository(PersonRepository.OnFirestoreTaskComplete onFirestoreTaskComplete){
        this.onFirestoreTaskComplete = onFirestoreTaskComplete;
    }

    public void addNewPerson(String personName, int personBudget, int giftsQuantity, int giftsBought, float checkedGiftsPrice, String list){
        Person newPerson = new Person(personName, personBudget, giftsQuantity, giftsBought, checkedGiftsPrice);
        CollectionReference personsRef = rootRef.collection("users").document(Objects.requireNonNull(firebaseAuth.getUid()))
                .collection("Giftlists").document(list)
                .collection("Persons");
        DocumentReference personRef = personsRef.document(personName);

        personRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                //if(!document.exists()){
                    personRef.set(newPerson).addOnCompleteListener(listCreationTask -> {
                        Log.d(TAG, "CreateNewGiftList: ");
                        if (listCreationTask.isSuccessful()) {
                            Log.d(TAG, "CreateNewGiftList: ");
                        } else {
                            logErrorMessage(listCreationTask.getException().getMessage());
                        }
                    });
               // }
            }
        });
    }

    public void deleteGift(String personName, String list){
        CollectionReference personsRef = rootRef.collection("users").document(Objects.requireNonNull(firebaseAuth.getUid()))
                .collection("Giftlists").document(list)
                .collection("Persons");
        DocumentReference personRef = personsRef.document(personName);

        personRef.delete();
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
                        assert value != null;
                        onFirestoreTaskComplete.getPersonDataAdded(value.toObjects(Person.class));
                    });
        }
    }

    public void getSelectedPersonData(String list, String name) {
        if (list != "") {
            final CollectionReference personsRef = rootRef.collection("users").document(firebaseAuth.getUid()).collection("Giftlists").document(list).collection("Persons");
            personsRef
                    .whereEqualTo("name", name)
                    .addSnapshotListener((value, e) -> {
                        if (e != null) {
                            onFirestoreTaskComplete.onError(e);
                            Log.w(TAG, "Listen failed.", e);
                            return;
                        }
                        //                                      creates a list of data of type Person
                        onFirestoreTaskComplete.getSelectedPerson(value.toObjects(Person.class));
                    });
        }
    }

    //interface for sending data to viewmodel
    //I'm using it for my querysnapshot task from above
    public interface OnFirestoreTaskComplete {
        void getPersonDataAdded(List<Person> PersonModelList);
        void getSelectedPerson (List<Person> SelectedPersonModelList);
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