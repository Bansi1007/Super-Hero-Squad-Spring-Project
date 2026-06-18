package com.example.teamOfSuperheroes.service;

import com.example.teamOfSuperheroes.model.Hero;
import com.example.teamOfSuperheroes.model.HeroRequest;
import com.example.teamOfSuperheroes.repository.HeroRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.in;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class HeroServiceTest {

    @Mock
    HeroRepository heroRepository;

    @InjectMocks
    HeroService heroService;

    private Hero activeHero;
    private Hero inactiveHero;
    private List<Hero> heroList;

    @BeforeEach
    void setUp() {

        HeroRepository.heroMapDb.clear();
        HeroRepository.heroListDb.clear();

        activeHero = new Hero();
        activeHero.setId(UUID.randomUUID());
        activeHero.setName("Batman");
        activeHero.setLevel(50);
        activeHero.setPower("strength");
        activeHero.setIsActive(true);

        inactiveHero = new Hero();
        inactiveHero.setId(UUID.randomUUID());
        inactiveHero.setName("Superman");
        inactiveHero.setLevel(80);
        inactiveHero.setPower("flight");
        inactiveHero.setIsActive(false);

        heroList = new ArrayList<>(List.of(activeHero, inactiveHero));
    }

    @Test
    void shouldReturnAllHeroes() {
        // ARRANGE — tell fake repo what to return
        when(heroRepository.getHeros()).thenReturn(heroList);

        // ACT — call the real service method
        List<Hero> result = heroService.getHeroes();

        // ASSERT — check results
        assertThat(result).hasSize(2);
        assertThat(result).contains(activeHero, inactiveHero);

    }

    @Test
    void shouldReturnEmptyListWhenNoHeroes() {
        when(heroRepository.getHeros()).thenReturn(new ArrayList<>());
        List<Hero> result = heroService.getHeroes();
        assertThat(result).isEmpty();
    }

    @Test
    void shouldReturnOnlyActiveHeroes() {
        when(heroRepository.getHeros()).thenReturn(heroList);

        List<Hero> result = heroService.isActiveHeros();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName().equals("Batman"));
    }

    @Test
    void shouldReturnEmptyWhenNoActiveHeroes() {
        activeHero.setIsActive(false);
        when(heroRepository.getHeros()).thenReturn(heroList);

        List<Hero> result = heroService.isActiveHeros();

        assertThat(result).isEmpty();
    }

    @Test
    void shouldReturnOnlyInactiveHeroes() {
        when(heroRepository.getHeros()).thenReturn(heroList);
        List<Hero> result = heroService.isInActiveHeros();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName().equals("Superman"));
    }

    @Test
    void shouldReturnEmptyWhenNoInactiveHeroes() {
        inactiveHero.setIsActive(true);
        when(heroRepository.getHeros()).thenReturn(heroList);
        List<Hero> result = heroService.isInActiveHeros();
        assertThat(result).isEmpty();
    }

    @Test
    void shouldReturnHeroesById() {
        UUID uuid = activeHero.getId();
        Map<UUID, Hero> herosFromMap = new HashMap<>();
        herosFromMap.put(uuid, activeHero);

        when(heroRepository.getHerosFromMap()).thenReturn(herosFromMap);

        Hero result = heroService.getHerosByID(uuid);
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(uuid);


    }

    @Test
    void shouldReturnEmptyWhenNoHeroesById() {
        UUID uuid = UUID.randomUUID();
        Map<UUID, Hero> herosFromMap = new HashMap<>();

        when(heroRepository.getHerosFromMap()).thenReturn(herosFromMap);
        Hero result = heroService.getHerosByID(uuid);

        assertThat(result).isNull();
    }

    @Test
    void shouldReturnHeroesByName() {
        String name = "Batman";
        List<Hero> heroList1 = new ArrayList<>();
        heroList1.add(activeHero);

        when(heroRepository.getHeros()).thenReturn(heroList1);


        Hero result = heroService.getHerosByName(name);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo(name);

    }

    @Test
    void shouldReturnEmptyWhenNoHeroesByName() {
        String name = "X-man";
        activeHero.setName("Superman");
        List<Hero> heroList1 = new ArrayList<>();
        heroList1.add(activeHero);
        when(heroRepository.getHeros()).thenReturn(heroList1);
        Hero result = heroService.getHerosByName(name);
        assertThat(result).isNull();
    }

    @Test
    void shouldAddNewHero() {
        Hero result = heroService.addNewHero(activeHero);
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(activeHero.getId());
    }

    @Test
    void shouldUpdateHero() {
        UUID uuid = activeHero.getId();
        String power = "super vision";
        int level = 80;
        Hero updateData = new Hero();
        updateData.setPower(power);
        updateData.setLevel(level);

        Map<UUID, Hero> herosFromMap = new HashMap<>();
        herosFromMap.put(uuid, activeHero);
        when(heroRepository.getHerosFromMap()).thenReturn(herosFromMap);
        Hero result = heroService.updateHero(uuid, updateData);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(uuid);
        assertThat(result.getPower()).isEqualTo(power);
        assertThat(result.getLevel()).isEqualTo(level);
    }

    @Test
    void shouldReturnNullWhenNoHeroesToUpdate() {
        UUID uuid = UUID.randomUUID();
        Map<UUID, Hero> herosFromMap = new HashMap<>();
        when(heroRepository.getHerosFromMap()).thenReturn(herosFromMap);
        Hero result = heroService.updateHero(uuid, activeHero);
        assertThat(result).isNull();
    }

    @Test
    void shouldReturnToggleHero() {
        UUID uuid = activeHero.getId();
        Map<UUID, Hero> herosFromMap = new HashMap<>();
        herosFromMap.put(uuid, activeHero);

        when(heroRepository.getHerosFromMap()).thenReturn(herosFromMap);
        Hero result = heroService.toggleHero(uuid);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(activeHero.getId());
        assertThat(result.getIsActive()).isNotEqualTo(!activeHero.getIsActive());
    }

    @Test
    void shouldLevelUpHero() {
        UUID uuid = activeHero.getId();
        activeHero.setLevel(50);

        Map<UUID, Hero> herosFromMap = new HashMap<>();
        herosFromMap.put(uuid, activeHero);

        when(heroRepository.getHerosFromMap()).thenReturn(herosFromMap);
        Hero result = heroService.levelUpHero(uuid);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(uuid);
        assertThat(result.getLevel()).isEqualTo(51);
    }

    @Test
    void shouldDeleteHero() {
        UUID uuid = inactiveHero.getId();

        Map<UUID, Hero> herosFromMap = new HashMap<>();
        herosFromMap.put(activeHero.getId(), activeHero);
        herosFromMap.put(uuid, inactiveHero);

        when(heroRepository.getHerosFromMap()).thenReturn(herosFromMap);
        when(heroRepository.getHeros()).thenReturn(List.of(activeHero));
        List<Hero> result = heroService.deleteHero(uuid);

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0).getId()).isEqualTo(activeHero.getId());
    }

    @Test
    void shouldDeleteAllHeroesWhenNotActive() {
        List<Hero> herosList = new ArrayList<>();
        herosList.add(activeHero);
        herosList.add(inactiveHero);
        List<Hero> activehero = new ArrayList<>();
        activehero.add(activeHero);

        when(heroRepository.getHeros()).thenReturn(herosList).thenReturn(activehero);
        List<Hero> result = heroService.deleteAllHeroes();

        assertThat(result).isNotNull();
        assertThat(result.get(0).getIsActive()).isTrue();
        verify(heroRepository).removeHeroFromBothStructures(inactiveHero);
    }

    @Test
    void shouldReturnNullWhenNoHeroesToDelete() {
        List<Hero> herosList = new ArrayList<>();

        when(heroRepository.getHeros()).thenReturn(herosList);
        List<Hero> result = heroService.deleteAllHeroes();

        assertThat(result).isNull();
    }

    @Test
    void shouldReturnStrongestHero() {
        List<Hero> herosList = new ArrayList<>();

        activeHero.setLevel(50);
        inactiveHero.setLevel(90);
        herosList = List.of(activeHero, inactiveHero);

        when(heroRepository.getHeros()).thenReturn(herosList);
        Hero result = heroService.getStrongestHero();

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(inactiveHero.getId());
        assertThat(result.getLevel()).isEqualTo(90);
    }

    @Test
    void shouldReturnWeakestHero() {
        activeHero.setLevel(50);
        inactiveHero.setLevel(90);
        List<Hero> herosList = List.of(activeHero, inactiveHero);
        when(heroRepository.getHeros()).thenReturn(herosList);
        Hero result = heroService.getWeakestHero();
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(activeHero.getId());
        assertThat(result.getLevel()).isEqualTo(50);
    }

    @Test
    void shouldReturnSortedHeroes() {
        activeHero.setLevel(50);
        inactiveHero.setLevel(90);
        List<Hero> herosList = List.of(activeHero, inactiveHero);
        when(heroRepository.getHeros()).thenReturn(herosList);
        List<Hero> result = heroService.getSortedHeroes();
        assertThat(result).isNotNull();
        assertThat(result.get(0).getLevel()).isEqualTo(90);
        assertThat(result.get(1).getLevel()).isEqualTo(50);
    }

    @Test
    void shouldReturnSearchHero() {
        activeHero.setPower("super vision");
        inactiveHero.setPower("ultra strength");
        List<Hero> herosList = List.of(activeHero, inactiveHero);
        when(heroRepository.getHeros()).thenReturn(herosList);
        // List<Hero>result = heroService.searchHeroes("super");
        List<Hero> result = heroService.searchHeroes("strength");
        assertThat(result).isNotNull();
        //assertThat(result.get(0).getPower()).isEqualTo("super vision");
        //assertThat(result.get(0).getName()).isEqualTo("Batman");
        assertThat(result.get(0).getPower()).isEqualTo("ultra strength");
        assertThat(result.get(0).getName()).isEqualTo("Superman");
    }

    @Test
    void shouldReturnNullWhenPowersNotMatching() {
        activeHero.setPower("super vision");
        inactiveHero.setPower("ultra strength");
        List<Hero> herosList = List.of(activeHero, inactiveHero);
        when(heroRepository.getHeros()).thenReturn(herosList);
        List<Hero> result = heroService.searchHeroes("invisible");
        assertThat(result).isEmpty();
    }

    @Test
    void shouldThrowExceptionWhenPowersNull() {
        assertThatThrownBy(() -> heroService.searchHeroes("")).isInstanceOf(IllegalArgumentException.class).hasMessage("power is blank");
    }

    @Test
    void shouldReturnLevelRangeHeroes() {
        int minLevel = 30;
        int maxLevel = 50;
        activeHero.setLevel(40);
        inactiveHero.setLevel(70);
        List<Hero> herosList = List.of(activeHero, inactiveHero);
        when(heroRepository.getHeros()).thenReturn(herosList);
        List<Hero> result = heroService.getLevelRangeHeroes(minLevel, maxLevel);
        assertThat(result).isNotNull().hasSize(1).containsExactly(activeHero);
    }

    @Test
    void shouldThrowExceptionWhenNull() {
        activeHero.setLevel(60);
        inactiveHero.setLevel(70);
        when(heroRepository.getHeros()).thenReturn(null);
        assertThatThrownBy(() -> heroService.getLevelRangeHeroes(30, 50))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Hero Not Found");
    }

    @Test
    void ReturnHeroesCount() {
        List<Hero> herosList = List.of(activeHero, inactiveHero);
        when(heroRepository.getHeros()).thenReturn(herosList);
        String result = heroService.getHeroesCount();
        assertThat(herosList).hasSize(2);
        assertThat(result)
                .contains("Total Heroes-----2")
                .contains("Active Heroes-----1")
                .contains("Benched Heroes--1");

    }

    @Test
    void returnHeroesAverageLevel() {
        activeHero.setLevel(20);
        inactiveHero.setLevel(40);
        List<Hero> herosList = List.of(activeHero, inactiveHero);
        when(heroRepository.getHeros()).thenReturn(herosList);
        double result = heroService.getHeroesAverageLevel();
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(30);
    }

    @Test
    void returnbulkHeroes() {
        List<HeroRequest> requests = new ArrayList<>();

        List<Hero> result = heroService.bulkHeroes(requests);
        assertThat(result).isNotNull();
    }

    @Test
    void returnHeroesNames() {
        activeHero.setName("X-man");
        inactiveHero.setName("ant-man");
        List<Hero> herosList = List.of(activeHero, inactiveHero);
        when(heroRepository.getHeros()).thenReturn(herosList);
        List<String> result = heroService.getHeroesNames();
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(List.of("X-man", "ant-man"));
    }

    @Test
    void shouldRenameHero() {
        UUID uuid = activeHero.getId();
        String newName = "X-man";
        Map<UUID, Hero> heroMap = new HashMap<>();
        heroMap.put(uuid, activeHero);
        when(heroRepository.getHerosFromMap()).thenReturn(heroMap);
        Hero result = heroService.reNameHero(uuid, newName);
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo(newName);
    }

    @Test
    void shouldThrowExceptionWhenIdIsNull() {
        assertThatThrownBy(() -> heroService.reNameHero(null, null)).isInstanceOf(IllegalArgumentException.class).hasMessage("Name cannot be blank");
    }

    @Test
    void shouldReturnHeroesIds() {
        Map<UUID, Hero> heroMap = new HashMap<>();
        UUID activeHeroId = activeHero.getId();
        UUID inactiveHeroId = inactiveHero.getId();
        heroMap.put(activeHeroId, activeHero);
        heroMap.put(inactiveHeroId, inactiveHero);

        when(heroRepository.getHerosFromMap()).thenReturn(heroMap);
        List<UUID> result = heroService.getHeroesIds();
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(List.of(activeHeroId, inactiveHeroId));
    }

    @Test
    void shouldReturnExistsHero() {
        Map<UUID, Hero> heroMap = new HashMap<>();
        UUID uuid = activeHero.getId();
        activeHero.setId(uuid);
        heroMap.put(uuid, activeHero);
        when(heroRepository.getHerosFromMap()).thenReturn(heroMap);
        boolean result = heroService.existsHero(uuid);
        assertThat(result).isTrue();
    }

    @Test
    void shouldThrowExceptionWhenHeroIdIsNull() {
        assertThatThrownBy(() -> heroService.existsHero(null)).isInstanceOf(ResponseStatusException.class).hasMessageContaining("Hero Not Found");
    }

}
