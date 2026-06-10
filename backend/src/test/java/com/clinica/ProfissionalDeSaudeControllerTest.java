package com.clinica;

import com.clinica.controller.ProfissionalDeSaudeController;
import com.clinica.model.ProfissionalDeSaude;
import com.clinica.repository.ProfissionalDeSaudeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockbean.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * TESTES UNITÁRIOS - Profissionais De Saúde
 * Usa @WebMvcTest para testar apenas o controller isoladamente
 */
@WebMvcTest(ProfissionalDeSaudeController.class)
class ProfissionalDeSaudeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProfissionalDeSaudeRepository repository;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void deveCriarProfissionalDeSaudeComSucesso() throws Exception {
        ProfissionalDeSaude comp = new ProfissionalDeSaude();
        comp.setId(1L);
        comp.setNome("João da Silva");
        comp.setTelefone("912345678");
        comp.setEndereco("Rua Blá Blá, Bairro Dan Da Dan");
        comp.setCategoria(["Psicologo"]);

        when(repository.save(any(ProfissionalDeSaude.class))).thenReturn(comp);

        mockMvc.perform(post("/api/profissionaisDeSaude")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(comp)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("João da Silva"));
    }

    @Test
    void deveListarProfissionalDeSaudesOrdenados() throws Exception {
        ProfissionalDeSaude comp1 = new ProfissionalDeSaude();
        comp1.setId(1L);
        comp.setNome("Alberto");
        comp.setCategoria(["Psicologo"]);

        ProfissionalDeSaude comp2 = new ProfissionalDeSaude();
        comp2.setId(2L);
        comp.setNome("Beatriz");
        comp.setCategoria(["Psicologo"]);

        when(repository.findAllByOrderByNomeAsc())
                .thenReturn(Arrays.asList(comp1, comp2));

        mockMvc.perform(get("/api/profissionaisDeSaude"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Alberto"))
                .andExpect(jsonPath("$[1].nome").value("Beatriz"));
    }

    @Test
    void deveListarProfissionaisDeSaudeVazio() throws Exception {
        when(repository.findAllByOrderByNomeAsc()).thenReturn(Arrays.asList());

        mockMvc.perform(get("/api/profissionaisDeSaude"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void deveRetornar404ParaProfissionalDeSaudeInexistente() throws Exception {
        when(repository.findById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/profissionaisDeSaude/999"))
                .andExpect(status().isNotFound());
    }
}
