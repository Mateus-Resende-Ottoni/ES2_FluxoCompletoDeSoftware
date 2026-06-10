package com.clinica.model;

import jakarta.persistence.*;
//import jakarta.validation.constraints.NotBlank;
//import jakarta.validation.constraints.NotNull;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Data;

@Entity
@Table(name = "examesLab")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExameLab {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    // Ligação com Atendimento
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "atendimento_id")
    private Atendimento atendimento;

    
    //====================----------====================
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}
    //====================----------====================

    
    //====================----------====================
	public String getDescricao() {
		return this.descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
    //====================----------====================

    
    //====================----------====================
	public Atendimento getAtendimento() {
		return this.atendimento;
	}

	public void setAtendimento(Atendimento atendimento) {
		this.atendimento = atendimento;
	}
    //====================----------====================

}
