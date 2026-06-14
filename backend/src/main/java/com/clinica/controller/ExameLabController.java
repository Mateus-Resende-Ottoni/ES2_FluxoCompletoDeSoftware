package com.clinica.controller;

import com.clinica.model.ExameLab;
import com.clinica.repository.AtendimentoRepository;
import com.clinica.repository.ExameLabRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/examesLab")
@CrossOrigin(origins = "*")
public class ExameLabController {

    private final ExameLabRepository repository;
    private final AtendimentoRepository atendimentoRepository;

    public ExameLabController(ExameLabRepository repository,
                                AtendimentoRepository atendimentoRepository) {
        this.repository = repository;
        this.atendimentoRepository = atendimentoRepository;
    }

    // CREATE - Criar novo exame
    @PostMapping
    public ResponseEntity<ExameLab> criar(@Valid @RequestBody ExameLab exame) {
        if (exame.getAtendimento() != null && exame.getAtendimento().getId() != null) {
            atendimentoRepository.findById(exame.getAtendimento().getId())
                    .ifPresent(exame::setAtendimento);
        }

        ExameLab salvo = repository.save(exame);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }

    // READ - Listar todos os exames
    @GetMapping
    public ResponseEntity<List<ExameLab>> listar() {
        List<ExameLab> exames = repository.findAllByOrderByIdAsc();
        return ResponseEntity.ok(exames);
    }

    // READ - Buscar exame por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> buscar(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(null));
    }

    // UPDATE - Atualizar exame
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id,
                                       @Valid @RequestBody ExameLab dados) {
        return repository.findById(id)
                .map(comp -> {
                    comp.setDescricao(dados.getDescricao());
                    if (dados.getAtendimento() != null && dados.getAtendimento().getId() != null) {
                        atendimentoRepository.findById(dados.getAtendimento().getId())
                                .ifPresent(comp::setAtendimento);
                    } else {
                        comp.setAtendimento(null);
                    }
                    return ResponseEntity.ok(repository.save(comp));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE - Remover exame
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        return repository.findById(id)
                .map(comp -> {
                    repository.delete(comp);
                    return ResponseEntity.ok(Map.of("mensagem", "Exame removido com sucesso"));
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
