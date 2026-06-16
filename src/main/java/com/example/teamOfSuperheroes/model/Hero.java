package com.example.teamOfSuperheroes.model;
import java.util.UUID;

public class Hero {
    UUID id;          // generated automatically, e.g. UUID.randomUUID()
    String name;         // e.g. "Iron Man"
    String power;        // e.g. "Energy blasts"
    int level;           // e.g. 1-100
    Boolean isActive;    // currently on duty?

    public Hero() {

    }

    public Hero(String name, String power, int level, Boolean isActive) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.power = power;
        this.level = level;
        this.isActive = isActive;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPower() {
        return power;
    }

    public int getLevel() {
        return level;
    }

    public Boolean isActive() {
        return isActive;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPower(String power) {
        this.power = power;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

}