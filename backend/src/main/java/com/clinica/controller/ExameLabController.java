package com.clinica.controller;

import com.clinica.model.ExameLab;
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

    public ExameLabController(ExameLabRepository repository) {
        this.repository = repository;
    }

    // CREATE - Criar novo exame
    @PostMapping
    public ResponseEntity<ExameLab> criar(@Valid @RequestBody ExameLab exame) {
        ExameLab salvo = repository.save(exame);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }

    // READ - Listar todos os exames
    @GetMapping
    public ResponseEntity<List<ExameLab>> listar() {
        List<ExameLab> exames = repository.findAllByOrderByDataAscHoraAsc();
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
                    comp.setAtendimento(dados.getAtendimento());
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
