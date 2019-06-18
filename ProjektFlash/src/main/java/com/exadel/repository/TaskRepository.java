package com.exadel.repository;

import com.exadel.model.Intern;
import com.exadel.model.Task;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TaskRepository extends MongoRepository<Task, String> {

}
