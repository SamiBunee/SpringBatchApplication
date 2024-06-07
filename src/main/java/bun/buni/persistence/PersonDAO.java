package bun.buni.persistence;

import bun.buni.entities.Person;
import org.springframework.data.repository.CrudRepository;

public interface PersonDAO extends CrudRepository <Person, Long> {
}
