package com.example.teamOfSuperheroes.controller;

import com.example.teamOfSuperheroes.model.Hero;
import com.example.teamOfSuperheroes.service.HeroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
public class HeroController {

    @Autowired
    HeroService heroService;

    @GetMapping("/GET/heroes")
    public List<Hero> getHeroes() {
        return heroService.getHeroes();
    }

    @GetMapping("GET/heroes/active")
    public List<Hero> getActiveHeroes() {
        return heroService.isActiveHeros();
    }

    @GetMapping("GET/heroes/benched")
    public List<Hero> getInActiveHeros() {
        return heroService.isInActiveHeros();
    }

    @GetMapping("GET/heroes/{id}")
    public ResponseEntity<Hero> getHero(@PathVariable UUID id) {
        var hero = heroService.getHerosByID(id);
        if (hero != null) {
            return new ResponseEntity<>(hero, HttpStatus.FOUND);
        } else {
            return new ResponseEntity<Hero>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("GET/heroes/by-name/{name}")
    public Hero getHeroByName(@PathVariable String name) {
        var heroByName = heroService.getHerosByName(name);
        if (heroByName != null) {
            return heroByName;
        } else {
            return null;
        }
    }

    @PostMapping("POST/heroes")
    public ResponseEntity<Hero> addNewHero(@RequestBody Hero hero) {
        Hero addNewHero = heroService.addNewHero(hero);
        return new ResponseEntity<>(addNewHero, HttpStatus.CREATED);
    }

    @PutMapping("PUT/heroes/{id}")
    public Hero updateHero(@PathVariable UUID id, @RequestBody Hero hero) {
        Hero currentHero = heroService.updateHero(id, hero);
        return currentHero;
    }

    @PatchMapping("PATCH/heroes/{id}/toggle")
    public ResponseEntity<Hero> toggleHero(@PathVariable UUID id) {
        Hero toggleHero = heroService.toggleHero(id);
        return new ResponseEntity<>(toggleHero, HttpStatus.OK);
    }

    @PatchMapping("PATCH /heroes/{id}/level-up")
    public Hero levelUpHero(@PathVariable UUID id) {
        Hero levelUpHero = heroService.levelUpHero(id);
        return levelUpHero;
    }

    @DeleteMapping("DELETE/heroes/{id}")
    public String deleteHero(@PathVariable UUID id) {
        String deleteHero = heroService.deleteHero(id);
        return deleteHero;
    }

    @DeleteMapping("DELETE/heroes")
    public List<Hero> deleteAllHeroes() {
        List<Hero> deleteAllHeroes = heroService.deleteAllHeroes();
        return deleteAllHeroes;
    }
    @GetMapping("GET /heroes/strongest")
    public Hero getStrongestHeroes() {
        Hero getStrongestHero = heroService.getStrongestHero();
        return getStrongestHero;
    }

    @GetMapping("GET /heroes/weakest")
    public Hero getWeakestHeroes() {
        Hero getWeakestHero = heroService.getWeakestHero();
        return getWeakestHero;
    }

    @GetMapping("GET /her   oes/sorted")
    public List<Hero> getSortedHeroes() {
        List<Hero> getSortedHeroes = heroService.getSortedHeroes();
        return getSortedHeroes;
    }

    //14
    @GetMapping("GET/heroes/search")
    public List<Hero> searchHeroes(@RequestParam String power) {
        List<Hero> searchHeroes = heroService.searchHeroes(power);
        return searchHeroes;
    }
}
