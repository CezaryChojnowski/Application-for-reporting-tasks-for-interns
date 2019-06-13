package com.exadel.repository;

import com.exadel.model.Intern;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface InternRepository extends MongoRepository<Intern, String> {

}
