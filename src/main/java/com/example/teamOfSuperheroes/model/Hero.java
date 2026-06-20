package com.example.teamOfSuperheroes.model;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Document(collection = "Hero")
public class Hero {
    UUID id;          // generated automatically, e.g. UUID.randomUUID()
    String name;         // e.g. "Iron Man"
    String power;        // e.g. "Energy blasts"
    int level;           // e.g. 1-100
    Boolean isActive;    // currently on duty?



    public Hero(String name, String power, int level, Boolean isActive) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.power = power;
        this.level = level;
        this.isActive = isActive;
    }

    public Hero(HeroRequest request){
        this.id = UUID.randomUUID();
        this.name = request.getName();
        this.power = request.getPower();
        this.level = request.getLevel();
        this.isActive = request.getIsActive();
    }
}