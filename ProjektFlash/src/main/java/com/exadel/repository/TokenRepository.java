package com.exadel.repository;

import com.exadel.model.Token;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TokenRepository extends MongoRepository<Token, String> {
    Token findBySeries(String series);
    Token findByUsername(String username);
}
