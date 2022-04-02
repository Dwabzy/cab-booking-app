package com.gitlab.jspragadeesh.cabbookingapp.repositories;

import com.gitlab.jspragadeesh.cabbookingapp.models.Person;
import org.springframework.data.mongodb.core.MongoAdminOperations;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends MongoRepository<Person, String> {
    Person findByEmail(String email);
}
