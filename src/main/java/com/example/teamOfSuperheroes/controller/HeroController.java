package com.example.teamOfSuperheroes.controller;

import ch.qos.logback.core.util.StringUtil;
import com.example.teamOfSuperheroes.model.Hero;
import com.example.teamOfSuperheroes.model.HeroRequest;
import com.example.teamOfSuperheroes.service.HeroService;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
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
    public ResponseEntity<?> getHeroByName(@PathVariable String name) {
        var heroByName = heroService.getHerosByName(name.trim());
        if (heroByName != null) {
            return ResponseEntity.ok(heroByName);
        } else {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("No hero found with name: " + name.trim());
        }
    }

    @PostMapping("/heroes")
    public ResponseEntity<Hero> addNewHero(@RequestBody HeroRequest heroRequest) {
        Hero hero = new Hero(heroRequest);
        Hero addNewHero = heroService.addNewHeroToMongo(hero);
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

    @GetMapping("/heroes/search")
    public ResponseEntity<?> searchHeroes(@RequestParam String power) {
        try {
            List<Hero> searchHeroes = heroService.searchHeroes(power);
            return ResponseEntity.ok(searchHeroes);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/heroes/level-range")
    public List<Hero> getLevelRangeHeroes(@RequestParam int min, @RequestParam int max) {
        List<Hero> getLevelRangeHeroes = heroService.getLevelRangeHeroes(min, max);
        return getLevelRangeHeroes;
    }

    @GetMapping("/heroes/count")
    public String getHeroesCount() {
        String heroCount = heroService.getHeroesCount();
        return heroCount;
    }

    @GetMapping("/heroes/average-level")
    public double getHeroesAverageLevel() {
        double avgLevel = heroService.getHeroesAverageLevel();
        return avgLevel;
    }

    @PostMapping("/heroes/bulk")
    public List<Hero> bulkHeroes(@RequestBody List<HeroRequest> requests) {
        return heroService.bulkHeroes(requests);
    }

    @GetMapping("/heroes/names")
    public List<String> getHeroesNames() {
        List<String> heroNames = heroService.getHeroesNames();
        return heroNames;
    }

    @PutMapping("/heroes/{id}/rename")
    public ResponseEntity<?> reNameHero(@PathVariable UUID id, @RequestParam String newName) {
        try {
            Hero renameHero = heroService.reNameHero(id, newName);
            if (renameHero == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(renameHero);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/heroes/ids")
    public List<UUID> getHeroesIds() {
        List<UUID> ids = heroService.getHeroesIds();
        return ids;
    }

    @GetMapping("/heroes/exists/{id}")
    public ResponseEntity<Boolean> existsHero(@PathVariable UUID id) {
        Boolean existsHero = heroService.existsHero(id);
        return new ResponseEntity<>(existsHero, HttpStatus.OK);
    }
}
