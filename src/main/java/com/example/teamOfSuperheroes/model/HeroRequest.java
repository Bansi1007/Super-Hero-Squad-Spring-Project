package com.example.teamOfSuperheroes.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HeroRequest {


    String name;
    String power;
    int level;
    Boolean isActive;


}
