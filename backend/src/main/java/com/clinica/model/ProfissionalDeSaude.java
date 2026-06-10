package com.clinica.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
//import jakarta.validation.constraints.NotNull;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Entity
@Table(name = "profissionaisDeSaude")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfissionalDeSaude {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome é obrigatório")
    @Column(length = 100, nullable = false)
    private String nome;

    @Column(length = 20)
    private String telefone;

    @Column(length = 200)
    private String endereco;

    //Categoria[]
    @ElementCollection
    @CollectionTable(
        name = "profissional_categorias",
        joinColumns = @JoinColumn(name = "profissional_id")
    )
    @Column(name = "categoria")
    private List<String> categoria;

    
    //====================----------====================
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}
    //====================----------====================

    
    //====================----------====================
	public String getNome() {
		return this.nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
    //====================----------====================

    
    //====================----------====================
	public String getTelefone() {
		return this.telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}
    //====================----------====================

    
    //====================----------====================
	public String getEndereco() {
		return this.endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}
    //====================----------====================

    
    //====================----------====================
	public List<String> getCategoria() {
		return this.categoria;
	}

	public void setCategoria(List<String> categoria) {
		this.categoria = categoria;
	}
    //====================----------====================

}
