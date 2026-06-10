package com.clinica.controller;

import com.clinica.model.Atendimento;
import com.clinica.repository.AtendimentoRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.time.LocalDate;
//import java.time.LocalTime;
//import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/atendimentos")
@CrossOrigin(origins = "*")
public class AtendimentoController {

    private final AtendimentoRepository repository;

    public AtendimentoController(AtendimentoRepository repository) {
        this.repository = repository;
    }

    // CREATE - Criar novo atendimento
    @PostMapping
    public ResponseEntity<Atendimento> criar(@Valid @RequestBody Atendimento atendimento) {
        Atendimento salvo = repository.save(atendimento);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }

    // READ - Listar todos os atendimentos
    @GetMapping
    public ResponseEntity<List<Atendimento>> listar() {
        List<Atendimento> atendimentos = repository.findAllByOrderByDataAscHorarioAsc();
        return ResponseEntity.ok(atendimentos);
    }

    // READ - Buscar atendimento por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> buscar(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(null));
    }

    // READ - Buscar atendimento por data
    @GetMapping("/{data}")
    public ResponseEntity<?> buscarData(@PathVariable LocalDate data) {
        List<Atendimento> atendimentos = repository.findByData(data);
        if (atendimentos.size() > 0) {
            return ResponseEntity.ok(atendimentos);
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // UPDATE - Atualizar atendimento
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id,
                                       @Valid @RequestBody Atendimento dados) {
        return repository.findById(id)
                .map(comp -> {
                    comp.setData(dados.getData());
                    comp.setHorario(dados.getHorario());
                    comp.setProblema_texto(dados.getProblema_texto());
                    comp.setReceita_saude(dados.getReceita_saude());
                    comp.setProfissional(dados.getProfissional());
                    return ResponseEntity.ok(repository.save(comp));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE - Remover atendimento
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        return repository.findById(id)
                .map(comp -> {
                    repository.delete(comp);
                    return ResponseEntity.ok(Map.of("mensagem", "Atendimento removido com sucesso"));
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
