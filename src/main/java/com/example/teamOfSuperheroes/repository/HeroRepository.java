package com.example.teamOfSuperheroes.repository;

import com.example.teamOfSuperheroes.model.Hero;
import org.springframework.stereotype.Repository;

import java.util.*;

import static javax.swing.UIManager.put;

@Repository
public class HeroRepository {
    static List<Hero> heroListDb = new ArrayList<>();        // ordered squad roster
    static Map<UUID, Hero> heroMapDb = new HashMap<>();

    static {
        addHeroToBothStructures(new Hero("Iron Man", "Energy blasts", 85, true));
        addHeroToBothStructures(new Hero("Spider-Man", "Web-slinging", 60, true));
        addHeroToBothStructures(new Hero("Hulk", "Super strength", 95, false));
    }

    static {
        put(heroListDb.get(0).getId(), heroListDb.get(0));
        put(heroListDb.get(1).getId(), heroListDb.get(1));
        put(heroListDb.get(2).getId(), heroListDb.get(2));
    }

    public static void addHeroToBothStructures(Hero hero) {
        heroListDb.add(hero);
        heroMapDb.put(hero.getId(), hero);
    }

    public List<Hero> getHeros() {
        return heroListDb;
    }

    public Map<UUID, Hero> getHerosFromMap() {
        return heroMapDb;
    }


}

