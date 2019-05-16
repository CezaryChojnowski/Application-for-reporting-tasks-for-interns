package com.exadel.repository;
;
import com.exadel.model.Contact;
import com.exadel.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
     User findByLogin(String login);

     @Query("{'contact.email': ?0}")
     List<User> findByEmail(String email);
}
