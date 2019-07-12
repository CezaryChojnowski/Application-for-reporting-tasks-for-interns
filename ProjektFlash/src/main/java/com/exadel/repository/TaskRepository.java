package com.exadel.repository;

import com.exadel.model.Task;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;


public interface TaskRepository extends MongoRepository<Task, String> {

    @Query("{ '_id': ?0 }")
    Task findTaskByid(ObjectId id);
}
