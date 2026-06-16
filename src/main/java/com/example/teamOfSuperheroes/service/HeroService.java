package com.example.teamOfSuperheroes.service;

import com.example.teamOfSuperheroes.model.Hero;
import com.example.teamOfSuperheroes.repository.HeroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Service
public class HeroService {

    @Autowired
    HeroRepository heroRepository;

    public List<Hero> getHeroes() {
        return heroRepository.getHeros();
    }

    public List<Hero> isActiveHeros() {
        return heroRepository.getHeros().stream().filter(Hero::isActive).toList();
    }

    public List<Hero> isInActiveHeros() {
        List<Hero> inactiveHeroes = new ArrayList<>();
        for (Hero hero : heroRepository.getHeros()) {
            if (!hero.isActive()) {
                inactiveHeroes.add(hero);
            }
        }
        return inactiveHeroes;
        //return heroRepository.getHeros().stream().filter(Predicate.not(Hero::isActive)).toList();

    }

    public Hero getHerosByID(UUID uuid) {
        Map<UUID, Hero> herosFromMap = heroRepository.getHerosFromMap();
        Hero hero = herosFromMap.get(uuid);
        return hero;
    }

    public Hero getHerosByName(String name) {
        if (name == null) {
            return null;
        }
        for (Hero hero : heroRepository.getHeros()) {
            if (name.equalsIgnoreCase(hero.getName())) {
                return hero;
            }
        }
        return null;

    }

    public Hero addNewHero(Hero hero) {
        HeroRepository.addHeroToBothStructures(hero);
        return hero;
    }

    public Hero updateHero(UUID uuid, Hero heroUpdateData) {
        Map<UUID, Hero> herosFromMap = heroRepository.getHerosFromMap();
        Hero currentHero = herosFromMap.get(uuid);
        if (currentHero != null) {
            currentHero.setLevel(heroUpdateData.getLevel());
            currentHero.setPower(heroUpdateData.getPower());
            return currentHero;
        }
        return null;
    }

    public Hero toggleHero(UUID id) {
        Map<UUID, Hero> herosFromMap = heroRepository.getHerosFromMap();
        Hero toggleHero = herosFromMap.get(id);
        if (toggleHero != null) {
            if (!toggleHero.isActive()) {
                toggleHero.setActive(true);
                return toggleHero;
            } else {
                toggleHero.setActive(false);
                return toggleHero;
            }
        }
        return null;
    }

    public Hero levelUpHero(UUID id) {
        Map<UUID, Hero> herosFromMap = heroRepository.getHerosFromMap();
        Hero levelUpHero = herosFromMap.get(id);
        if (levelUpHero != null && levelUpHero.getLevel() < 100) {
            levelUpHero.setLevel(levelUpHero.getLevel() + 1);
            return levelUpHero;
        }
        return null;
    }

    public String deleteHero(UUID id) {
        Map<UUID, Hero> herosFromMap = heroRepository.getHerosFromMap();
        List<Hero> herosList = heroRepository.getHeros();
        Hero deleteHero = herosFromMap.get(id);
        if (deleteHero != null && !isActiveHeros().contains(deleteHero)) {
            herosList.remove(id);
            herosList.removeIf(hero -> hero.getId().equals(id));
            return "remove---" + deleteHero.getName();
        }
        return null;
    }

    public List<Hero> deleteAllHeroes() {
        Map<UUID, Hero> herosFromMap = heroRepository.getHerosFromMap();
        List<Hero> herosList = heroRepository.getHeros();
        for (Hero hero : herosList) {
            if (!hero.isActive()) {
                herosList.remove(hero);
                herosFromMap.remove(hero.getId());
                return herosList;
            }
        }
        return null;
    }

    public Hero getStrongestHero() {
        List<Hero> herosList = heroRepository.getHeros();
        Hero strongestHero = herosList.stream().max(Comparator.comparing(Hero::getLevel)).orElse(null);
        return strongestHero;
    }

    public Hero getWeakestHero() {
        List<Hero> herosList = heroRepository.getHeros();
        Hero WeakestHero = herosList.stream().min(Comparator.comparing(Hero::getLevel)).orElse(null);
        return WeakestHero;
    }

    public List<Hero> getSortedHeroes() {
        List<Hero>heroList = heroRepository.getHeros();
        List<Hero> SortedHeroes = heroList.stream().sorted(Comparator.comparing(Hero::getLevel,Comparator.reverseOrder())).toList();
        return SortedHeroes;
    }

    public List<Hero> searchHeroes(String power) {
        List<Hero> searchHeroes = heroRepository.getHeros();
        List<Hero> havingPower = new ArrayList<>();
        if (searchHeroes != null) {
            for (Hero hero : searchHeroes) {
                if (hero.getPower().contains(power.toLowerCase(Locale.ROOT))) {
                    havingPower.add(hero);
                    return havingPower;
                }
            }
        }
        return null;
    }
}
