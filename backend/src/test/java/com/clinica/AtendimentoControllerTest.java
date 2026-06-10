package com.clinica;

import com.clinica.controller.AtendimentoController;
import com.clinica.model.Atendimento;
//import com.clinica.model.ProfissionalDeSaude;
import com.clinica.repository.AtendimentoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * TESTES UNITÁRIOS - Atendimentos
 * Usa @WebMvcTest para testar apenas o controller isoladamente
 */
@WebMvcTest(AtendimentoController.class)
class AtendimentoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AtendimentoRepository repository;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void deveCriarAtendimentoComSucesso() throws Exception {
        Atendimento comp = new Atendimento();
        comp.setId(1L);
        comp.setData(LocalDate.of(2024, 12, 15));
        comp.setHorario(LocalTime.of(14, 0));
        comp.setProblema_texto("Dor de cabeça");
        List<String> receitas = new ArrayList<String>();
        receitas.add("Remédio");
        comp.setReceita_saude(receitas);

        when(repository.save(any(Atendimento.class))).thenReturn(comp);

        mockMvc.perform(post("/api/atendimentos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(comp)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.problema_texto").value("Dor de cabeça"));
    }

    @Test
    void deveListarAtendimentosOrdenados() throws Exception {
        Atendimento comp1 = new Atendimento();
        comp1.setId(1L);
        comp1.setProblema_texto("Dor de barriga");
        comp1.setData(LocalDate.of(2024, 12, 15));
        comp1.setHorario(LocalTime.of(14, 0));
        List<String> receitas1 = new ArrayList<String>();
        receitas1.add("Remédio");
        comp1.setReceita_saude(receitas1);

        Atendimento comp2 = new Atendimento();
        comp2.setId(2L);
        comp2.setProblema_texto("Desconforto na coluna");
        comp2.setData(LocalDate.of(2024, 12, 15));
        comp2.setHorario(LocalTime.of(18, 0));
        List<String> receitas2 = new ArrayList<String>();
        receitas2.add("Atividades Mentais");
        comp2.setReceita_saude(receitas2);

        when(repository.findAllByOrderByDataAscHorarioAsc())
                .thenReturn(Arrays.asList(comp1, comp2));

        mockMvc.perform(get("/api/atendimentos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].problema_texto").value("Dor de barriga"))
                .andExpect(jsonPath("$[1].problema_texto").value("Desconforto na coluna"));
    }

    @Test
    void deveListarAtendimentosVazio() throws Exception {
        when(repository.findAllByOrderByDataAscHorarioAsc()).thenReturn(Arrays.asList());

        mockMvc.perform(get("/api/atendimentos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void deveRetornar404ParaAtendimentoInexistente() throws Exception {
        when(repository.findById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/atendimentos/999"))
                .andExpect(status().isNotFound());
    }
}
