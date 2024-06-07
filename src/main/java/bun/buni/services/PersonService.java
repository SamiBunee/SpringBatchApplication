package bun.buni.services;

import bun.buni.entities.Person;

import java.util.List;

public interface PersonService {
    void saveAll(List<Person> personList);
}
