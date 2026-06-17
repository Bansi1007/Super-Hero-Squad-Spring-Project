package com.example.teamOfSuperheroes.service;

import com.example.teamOfSuperheroes.model.Hero;
import com.example.teamOfSuperheroes.model.HeroRequest;
import com.example.teamOfSuperheroes.repository.HeroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class HeroService {

    @Autowired
    HeroRepository heroRepository;

    public List<Hero> getHeroes() {
        return heroRepository.getHeros();
    }

    public List<Hero> isActiveHeros() {
        return heroRepository.getHeros().stream().filter(Hero::getIsActive).toList();
    }

    public List<Hero> isInActiveHeros() {
        List<Hero> inactiveHeroes = new ArrayList<>();
        for (Hero hero : heroRepository.getHeros()) {
            if (!hero.getIsActive()) {
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
        if (StringUtils.isBlank(name)) {
            return null;
        }
        for (Hero hero : heroRepository.getHeros()) {
            if (StringUtils.equalsIgnoreCase(name, hero.getName())) {
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
            herosFromMap.put(uuid, currentHero);
            return currentHero;
        }
        return null;
    }

    public Hero toggleHero(UUID id) {
        Hero toggleHero = heroRepository.getHerosFromMap().get(id);
        if (toggleHero == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Hero Not Found");
        }
        toggleHero.setIsActive(!toggleHero.getIsActive());
        System.out.println("MAP value:  " + heroRepository.getHerosFromMap().get(id).getIsActive());
        System.out.println("LIST value: " + heroRepository.getHeros().stream().filter(h -> h.getId().equals(id))
                .findFirst().get().getIsActive());
        return toggleHero;
    }

    public Hero levelUpHero(UUID id) {
        Hero levelUpHero = heroRepository.getHerosFromMap().get(id);
        if (levelUpHero == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Hero Not Found");
        }
        if (levelUpHero.getLevel() >= 100) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Level Exceeded");
        }
        levelUpHero.setLevel(levelUpHero.getLevel() + 1);
        return levelUpHero;
    }

    public List<Hero> deleteHero(UUID id) {
        if (id == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "id is null");
        }
        Hero heroToDelete = heroRepository.getHerosFromMap().get(id);
        if (heroToDelete == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "id is not valid");
        }
        if (isActiveHeros().contains(heroToDelete)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Cannot delete an active hero");
        }
        heroRepository.removeHeroFromBothStructures(heroToDelete);
        return heroRepository.getHeros();
    }

    public List<Hero> deleteAllHeroes() {
        List<Hero> herosList = heroRepository.getHeros();
        for (Hero hero : herosList) {
            if (!hero.getIsActive()) {
                heroRepository.removeHeroFromBothStructures(hero);
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
        List<Hero> heroList = heroRepository.getHeros();
        List<Hero> SortedHeroes = heroList.stream().sorted(Comparator.comparing(Hero::getLevel, Comparator.reverseOrder())).toList();
        return SortedHeroes;
    }

    public List<Hero> searchHeroes(String power) {
        if (StringUtils.isBlank(power)) {
            throw new IllegalArgumentException("power is blank");
        }
        List<Hero> searchHeroes = heroRepository.getHeros();
        List<Hero> havingPower = new ArrayList<>();
        if (searchHeroes != null) {
            for (Hero hero : searchHeroes) {
                if (hero.getPower().contains(power.trim().toLowerCase(Locale.ROOT))) {
                    havingPower.add(hero);
                    return havingPower;
                }
            }
        }
        return null;
    }

    public List<Hero> getLevelRangeHeroes(int min, int max) {
        List<Hero> levelRangeHeroes = heroRepository.getHeros();
        if (levelRangeHeroes == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Hero Not Found");
        }
        return levelRangeHeroes.stream().filter(hero -> hero.getLevel() >= min && hero.getLevel() <= max).collect(Collectors.toList());
    }


    public String getHeroesCount() {
        int totalHerosCount = heroRepository.getHeros().size();
        int activeHero = isActiveHeros().size();
        int benched = isInActiveHeros().size();

        return "Total Heroes-----" + totalHerosCount + "   Active Heroes-----" + activeHero + "    Benched Heroes--" + benched;
    }

    //17
    public double getHeroesAverageLevel() {
        List<Hero> totalHerosCount = heroRepository.getHeros();
        double average = totalHerosCount.stream().mapToInt(hero -> hero.getLevel()).average().orElse(0.0);
        return average;
    }

    public List<Hero> bulkHeroes(List<HeroRequest> requests) {
        List<Hero> newHeroes = new ArrayList<>();
        for (HeroRequest request : requests) {
            Hero hero = new Hero(request);
            newHeroes.add(hero);
            HeroRepository.addHeroToBothStructures(hero);
        }
        return newHeroes;
    }

    public List<String> getHeroesNames() {
        List<Hero> heroesList = heroRepository.getHeros();
        List<String> heroesNames = new ArrayList<>();
        for (Hero hero : heroesList) {
            heroesNames.add(hero.getName());
        }

        return heroesNames;
    }

    public Hero reNameHero(UUID id, String newName) {
        if (StringUtils.isBlank(newName)) {
            throw new IllegalArgumentException("Name cannot be blank");
        }
        Map<UUID, Hero> heroesFromMap = heroRepository.getHerosFromMap();
        Hero currentHero = heroesFromMap.get(id);
        if (currentHero == null) {
            throw new IllegalArgumentException("Hero not found with ID: " + id) {
            };
        }
        currentHero.setName(newName);
        heroesFromMap.put(id, currentHero);
        return currentHero;
    }

    public List<UUID> getHeroesIds() {
        Map<UUID, Hero> heroesFromMap = heroRepository.getHerosFromMap();
        List<UUID> heroesIds = heroesFromMap.keySet().stream().collect(Collectors.toList());
        return heroesIds;
    }

    public Boolean existsHero(UUID id) {
        Map<UUID, Hero> heroesFromMap = heroRepository.getHerosFromMap();
        Boolean heroesIds = heroesFromMap.containsKey(id);
        if (id == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Hero Not Found");
        }
        if (heroesIds) {
            return true;
        }
        return false;
    }
}
