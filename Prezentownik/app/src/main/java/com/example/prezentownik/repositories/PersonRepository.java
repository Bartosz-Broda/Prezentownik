package com.example.prezentownik.repositories;

import androidx.lifecycle.MutableLiveData;

import com.example.prezentownik.models.Person;

import java.util.ArrayList;
import java.util.List;

public class PersonRepository {
    private static PersonRepository instance;
    private ArrayList<Person> dataset = new ArrayList<>();

    public static PersonRepository getInstance(){
        if(instance == null){
            instance = new PersonRepository();
        }
        return instance;
    }

    // Pretend to get data from Internet or something
    public MutableLiveData<List<Person>> getPersons(){
        setPersons();

        MutableLiveData<List<Person>> data = new MutableLiveData<>();
        data.setValue(dataset);
        return data;
    }

    private void setPersons(){
        dataset.add(
                new Person("Wojtek","Zakupiono 1 z 3 prezentów")
        );
        dataset.add(
                new Person("Halina","Zakupiono 2 z 4 prezentów")
        );
        dataset.add(
                new Person("Lenka","Zakupiono 1 z 1 prezentów")
        );
        dataset.add(
                new Person("Horacy","Zakupiono 0 z 3 prezentów")
        );
        dataset.add(
                new Person("Anka","Zakupiono 1 z 2 prezentów")
        );
    }
}
