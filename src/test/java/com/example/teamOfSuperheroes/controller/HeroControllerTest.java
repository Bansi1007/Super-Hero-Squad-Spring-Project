package com.example.teamOfSuperheroes.controller;

import com.example.teamOfSuperheroes.model.Hero;
import com.example.teamOfSuperheroes.service.HeroService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(HeroController.class)
public class HeroControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    HeroService heroService;

    @Autowired
    ObjectMapper objectMapper;


    private Hero activeHero;
    private Hero inactiveHero;
    private List<Hero> heroList;


    @BeforeEach
    void setUp() {
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
    void shouldReturnAllHeroes() throws Exception {

        when(heroService.getHeroes()).thenReturn(heroList);
        mockMvc.perform(get("/heroes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].name").value("Batman"))
                .andExpect(jsonPath("$[1].name").value("Superman"));
    }

    @Test
    void shouldReturnEmptyListWhenNoHeroes() throws Exception {
        when(heroService.getHeroes()).thenReturn(new ArrayList<>());
        mockMvc.perform(get("/heroes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());

    }

    @Test
    void shouldReturnActiveHeroes() throws Exception {
        when(heroService.isActiveHeros()).thenReturn(List.of(activeHero));
        mockMvc.perform(get("/heroes/active"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Batman"));
    }

    @Test
    void shouldReturnInactiveHeroes() throws Exception {
        when(heroService.isInActiveHeros()).thenReturn(List.of(inactiveHero));
        mockMvc.perform(get("/heroes/benched"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Superman"));
    }

    @Test
    void shouldReturnHeroById() throws Exception {
        UUID uuid = activeHero.getId();
        when(heroService.getHerosByID(uuid)).thenReturn(activeHero);
        mockMvc.perform(get("/heroes/" + uuid))
                .andExpect(status().isFound())
                .andExpect(jsonPath("$.name").value("Batman"));
    }

    @Test
    void shouldReturnNotFoundWhenHeroNotFound() throws Exception {
        when(heroService.getHerosByID(UUID.randomUUID())).thenReturn(null);
        mockMvc.perform(get("/heroes/" + UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnHeroByName() throws Exception {
        String name = "Batman";
        activeHero.setName(name);
        when(heroService.getHerosByName(name)).thenReturn(activeHero);
        mockMvc.perform(get("/heroes/by-name/" + name))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(name));
    }

    @Test
    void shouldReturnNotFoundWhenHeroNameNotFoundByName() throws Exception {
        when(heroService.getHerosByName("")).thenReturn(null);
        mockMvc.perform(get("/heroes/by-name/" + " "))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldAddHero() throws Exception {
        Hero activeHero = new Hero();
        when(heroService.addNewHero(activeHero)).thenReturn(activeHero);
        mockMvc.perform(post("/heroes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(activeHero)))
                .andExpect(status().isCreated());
    }

    @Test
    void updateHero() throws Exception {
        UUID id = UUID.randomUUID();

        Hero activeHero = new Hero();
        activeHero.setId(id);
        activeHero.setName("Batman");
        activeHero.setLevel(50);
        activeHero.setPower("strength");
        when(heroService.updateHero(id, activeHero)).thenReturn(activeHero);
        mockMvc.perform(put("/heroes/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(activeHero)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Batman"))
                .andExpect(jsonPath("$.level").value(50))
                .andExpect(jsonPath("$.power").value("strength"));
    }


}
