package com.clinica.controller;

import com.clinica.model.ProfissionalDeSaude;
import com.clinica.repository.ProfissionalDeSaudeRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/profissionaisDeSaude")
@CrossOrigin(origins = "*")
public class ProfissionalDeSaudeController {

    private final ProfissionalDeSaudeRepository repository;

    public ProfissionalDeSaudeController(ProfissionalDeSaudeRepository repository) {
        this.repository = repository;
    }

    // CREATE - Criar novo profissional
    @PostMapping
    public ResponseEntity<ProfissionalDeSaude> criar(@Valid @RequestBody ProfissionalDeSaude profissional) {
        ProfissionalDeSaude salvo = repository.save(profissional);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }

    // READ - Listar todos os profissionais
    @GetMapping
    public ResponseEntity<List<ProfissionalDeSaude>> listar() {
        List<ProfissionalDeSaude> profissionais = repository.findAllByOrderByDataAscHoraAsc();
        return ResponseEntity.ok(profissionais);
    }

    // READ - Buscar profissional por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> buscar(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(null));
    }

    // READ - Buscar profissional por Nome
    @GetMapping("/{nome}")
    public ResponseEntity<?> buscar(@PathVariable String nome) {
        return repository.findByNomeContainingIgnoreCase(nome)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(null));
    }

    // READ - Buscar profissional por Categoria
    @GetMapping("/{categoria}")
    public ResponseEntity<?> buscar(@PathVariable String categoria) {
        return repository.findByCategoria(categoria)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(null));
    }

    // UPDATE - Atualizar profissional
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id,
                                       @Valid @RequestBody ProfissionalDeSaude dados) {
        return repository.findById(id)
                .map(comp -> {
                    comp.setNome(dados.getNome());
                    comp.setTelefone(dados.getTelefone());
                    comp.setEndereco(dados.getEndereco());
                    comp.setCategoria(dados.getCategoria());
                    return ResponseEntity.ok(repository.save(comp));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE - Remover profissional
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        return repository.findById(id)
                .map(comp -> {
                    repository.delete(comp);
                    return ResponseEntity.ok(Map.of("mensagem", "Profissional De Saude removido com sucesso"));
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
