package com.clinica;

import com.clinica.model.Atendimento;
import com.clinica.model.ExameLab;
import com.clinica.model.ProfissionalDeSaude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * TESTES DE INTEGRAÇÃO
 * Usa @SpringBootTest para carregar todo o contexto da aplicação
 * Testa a integração real entre Controller → Service → Repository → Banco
 * No CI, roda com PostgreSQL real via container
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class IntegracaoTest {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void deveExecutarFluxoCompletoAtendimento() throws Exception {
        // 1. CRIAR atendimento
        Atendimento atendimento = new Atendimento();
        atendimento.setData(LocalDate.of(2026, 9, 6));
        atendimento.setHorario(LocalTime.of(15, 0));
        atendimento.setProblema_texto("Dor de Cabeça");
        // O tipo da receita é dependente do profissional de saúde, então não definimos aqui

        MvcResult result = mockMvc.perform(post("/api/atendimentos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(atendimento)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data").value("2026-09-06"))
                .andReturn();

        Long id = objectMapper.readTree(result.getResponse().getContentAsString())
                .get("id").asLong();

        // 2. BUSCAR atendimento criado
        mockMvc.perform(get("/api/atendimentos/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value("2026-09-06"));

        // 3. ATUALIZAR atendimento
        atendimento.setData(LocalDate.of(2026, 9, 7));
        atendimento.setHorario(LocalTime.of(14, 0));

        mockMvc.perform(put("/api/atendimentos/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(atendimento)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value("2026-09-07"));

        // 4. DELETAR atendimento
        mockMvc.perform(delete("/api/atendimentos/" + id))
                .andExpect(status().isOk());
    }

    @Test
    void deveVincularAtendimentoAProfissional() throws Exception {
        // Criar profissional
        ProfissionalDeSaude profissional = new ProfissionalDeSaude();
        profissional.setNome("Médico da Silva");
        profissional.setTelefone("912348765");
        profissional.setEndereco("Casa 123");
        List<String> categorias = new ArrayList<String>();
        categorias.add("Médico");
        profissional.setCategoria(categorias);

        mockMvc.perform(post("/api/profissionaisDeSaude")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(profissional)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("Médico da Silva"));
                
        //Long profissionalId = objectMapper.readTree(
        //        profissionalResult.getResponse().getContentAsString()).get("id").asLong();

        // Criar atendimento
        Atendimento atendimento = new Atendimento();
        atendimento.setData(LocalDate.of(2025, 9, 6));
        atendimento.setHorario(LocalTime.of(15, 0));
        atendimento.setProblema_texto("Dor de Cabeça");
        List<String> receitas = new ArrayList<String>();
        for (int i = 0; i < (profissional.getCategoria()).size(); i++) {
                switch (profissional.getCategoria().get(i)) {
                        case "Psicólogo":
                                receitas.add("Atividades Mentais");
                                break;
                        case "Fisioterapeuta":
                                receitas.add("Atividade Física");
                                break;
                        case "Médico":
                                receitas.add("Remédio");
                                break;
                }
        }
        atendimento.setReceita_saude(receitas);
        atendimento.setProfissional(profissional);

        mockMvc.perform(post("/api/atendimentos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(atendimento)))
                .andExpect(status().isCreated());
    }

    @Test
    void deveVincularExameAAtendimento() throws Exception {
        // Criar atendimento
        Atendimento atendimento = new Atendimento();
        atendimento.setData(LocalDate.of(2025, 9, 6));
        atendimento.setHorario(LocalTime.of(15, 0));
        atendimento.setProblema_texto("Dor de Cabeça");

        mockMvc.perform(post("/api/atendimentos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(atendimento)))
                .andExpect(status().isCreated());

        // Criar exame
        ExameLab exame = new ExameLab();
        exame.setDescricao("Exame para dor de cabeça");
        exame.setAtendimento(atendimento);

        mockMvc.perform(post("/api/examesLab")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(exame)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.descricao").value("Exame para dor de cabeça"));


    }

}
