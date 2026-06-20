package com.example.teamOfSuperheroes.repository;

import com.example.teamOfSuperheroes.model.Hero;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface HeroRepo extends MongoRepository<Hero, UUID> {
}
