package com.example.ApiPanaderia.repository;

import com.example.ApiPanaderia.model.TokenSession;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenSessionRepository extends MongoRepository<TokenSession, String> {
}
