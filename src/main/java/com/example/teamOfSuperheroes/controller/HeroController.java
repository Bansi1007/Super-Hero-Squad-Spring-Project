package com.example.teamOfSuperheroes.controller;

import com.example.teamOfSuperheroes.model.Hero;
import com.example.teamOfSuperheroes.service.HeroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
public class HeroController {

    @Autowired
    HeroService heroService;

    @GetMapping("/heroes")
    public List<Hero> getHeroes() {
        return heroService.getHeroes();
    }

    @GetMapping("/heroes/active")
    public List<Hero> getActiveHeroes() {
        return heroService.isActiveHeros();
    }

    @GetMapping("/heroes/benched")
    public List<Hero> getInActiveHeros() {
        return heroService.isInActiveHeros();
    }

    @GetMapping("/heroes/{id}")
    public ResponseEntity<Hero> getHero(@PathVariable UUID id) {
        var hero = heroService.getHerosByID(id);
        if (hero != null) {
            return new ResponseEntity<>(hero, HttpStatus.FOUND);
        } else {
            return new ResponseEntity<Hero>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/heroes/by-name/{name}")
    public ResponseEntity<Hero> getHeroByName(@PathVariable String name) {
        var heroByName = heroService.getHerosByName(name);
        if (heroByName != null) {
            return new ResponseEntity<>(heroByName, HttpStatus.FOUND);
        } else {
            return new ResponseEntity<Hero>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/heroes")
    public ResponseEntity<Hero> addNewHero(@RequestBody Hero hero) {
        Hero addNewHero = heroService.addNewHero(hero);
        return new ResponseEntity<>(addNewHero, HttpStatus.CREATED);
    }

    @PutMapping("/heroes/{id}")
    public ResponseEntity<Hero> updateHero(@PathVariable UUID id, @RequestBody Hero hero) {
        Hero currentHero = heroService.updateHero(id, hero);
        if (currentHero != null) {
            return new ResponseEntity<>(currentHero, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping("/heroes/{id}/toggle")
    public ResponseEntity<Hero> toggleHero(@PathVariable UUID id) {
        Hero toggleHero = heroService.toggleHero(id);
        if (toggleHero != null) {
            return new ResponseEntity<>(toggleHero, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    @PatchMapping("/heroes/{id}/level-up")
    public ResponseEntity<Hero> levelUpHero(@PathVariable UUID id) {
        Hero levelUpHero = heroService.levelUpHero(id);
        if (levelUpHero != null) {
            return new ResponseEntity<>(levelUpHero, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/heroes/{id}")
    public ResponseEntity<?> deleteHero(@PathVariable UUID id) {
        try {
            List<Hero> updatedList = heroService.deleteHero(id);
            if (updatedList == null) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("Hero ID does not exist");
            }
            return ResponseEntity.ok(updatedList);
        } catch (ResponseStatusException e) {
            return ResponseEntity
                    .status(e.getStatusCode())
                    .body(e.getReason());
        }
    }

    @DeleteMapping("/heroes")
    public List<Hero> deleteAllHeroes() {
        List<Hero> deleteAllHeroes = heroService.deleteAllHeroes();
        return deleteAllHeroes;
    }

    @GetMapping("/heroes/strongest")
    public Hero getStrongestHeroes() {
        Hero getStrongestHero = heroService.getStrongestHero();
        return getStrongestHero;
    }

    @GetMapping("/heroes/weakest")
    public Hero getWeakestHeroes() {
        Hero getWeakestHero = heroService.getWeakestHero();
        return getWeakestHero;
    }

    @GetMapping("/heroes/sorted")
    public List<Hero> getSortedHeroes() {
        List<Hero> getSortedHeroes = heroService.getSortedHeroes();
        return getSortedHeroes;
    }

    //14
    @GetMapping("/heroes/search")
    public List<Hero> searchHeroes(@RequestParam String power) {
        List<Hero> searchHeroes = heroService.searchHeroes(power);
        return searchHeroes;
    }
}
