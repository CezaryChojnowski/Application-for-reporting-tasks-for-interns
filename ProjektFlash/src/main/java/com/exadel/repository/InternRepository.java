package com.exadel.repository;

import com.exadel.model.Intern;
import com.exadel.model.Task;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;


public interface InternRepository extends MongoRepository<Intern, String> {

    @Query("{ '_id': ?0 }")
    Intern findByid(ObjectId id);

    @Query("{ '_id': ?0 }")
    List<Task> findTasksByid(ObjectId id);

    @Query(" { 'email': ?0}")
    Intern findTasksByEmail(String email);

}
