package com.clinica;

import com.clinica.controller.ExameLabController;
import com.clinica.model.ExameLab;
//import com.clinica.model.Atendimento;
import com.clinica.repository.ExameLabRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * TESTES UNITÁRIOS - ExameLabs
 * Usa @WebMvcTest para testar apenas o controller isoladamente
 */
@WebMvcTest(ExameLabController.class)
class ExameLabControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ExameLabRepository repository;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void deveCriarExameLabComSucesso() throws Exception {
        ExameLab comp = new ExameLab();
        comp.setId(1L);
        comp.setDescricao("Dor de cabeça");

        when(repository.save(any(ExameLab.class))).thenReturn(comp);

        mockMvc.perform(post("/api/examesLab")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(comp)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.descricao").value("Dor de cabeça"));
    }

    @Test
    void deveListarExameLabsOrdenados() throws Exception {
        ExameLab comp1 = new ExameLab();
        comp1.setId(1L);
        comp1.setDescricao("Dor de barriga");

        ExameLab comp2 = new ExameLab();
        comp2.setId(2L);
        comp2.setDescricao("Desconforto na coluna");

        when(repository.findAllByOrderByIdAsc())
                .thenReturn(Arrays.asList(comp1, comp2));

        mockMvc.perform(get("/api/examesLab"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].descricao").value("Dor de barriga"))
                .andExpect(jsonPath("$[1].descricao").value("Desconforto na coluna"));
    }

    @Test
    void deveListarExamesLabVazio() throws Exception {
        when(repository.findAllByOrderByIdAsc()).thenReturn(Arrays.asList());

        mockMvc.perform(get("/api/examesLab"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void deveRetornar404ParaExameLabInexistente() throws Exception {
        when(repository.findById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/examesLab/999"))
                .andExpect(status().isNotFound());
    }
}
