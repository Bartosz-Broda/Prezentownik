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
        //TODO: TUTAJ ODWOLANIE DO KONKRETNEJ LISTY I Z NIEJ BRAC LUDZI
        dataset.add(
                new Person("Wojtek", 200,"Zakupiono 1 z 3 prezentów")
        );
        dataset.add(
                new Person("Halina", 150,"Zakupiono 2 z 4 prezentów")
        );
        dataset.add(
                new Person("Lenka", 300, "Zakupiono 1 z 1 prezentów")
        );
        dataset.add(
                new Person("Horacy", 75, "Zakupiono 0 z 3 prezentów")
        );
        dataset.add(
                new Person("Anka",200, "Zakupiono 1 z 2 prezentów")
        );
    }
}
