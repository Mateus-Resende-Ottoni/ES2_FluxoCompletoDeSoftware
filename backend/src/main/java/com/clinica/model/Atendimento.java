package com.clinica.model;

import jakarta.persistence.*;
//import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;
import java.time.LocalTime;
//import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "atendimentos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Atendimento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Data é obrigatória")
    private LocalDate data;

    private LocalTime horario;

    @Column(columnDefinition = "TEXT")
    private String problema_texto;

    //Receita_Saude[]
    @ElementCollection
    @CollectionTable(
        name = "atendimento_receitas",
        joinColumns = @JoinColumn(name = "atendimento_id")
    )
    @Column(name = "receita_saude")
    private List<String> receita_saude;

    // Ligação com ProfissionalDeSaúde
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "profissional_id")
    private ProfissionalDeSaude profissional;

    //====================----------====================
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}
    //====================----------====================

    
    //====================----------====================
	public LocalDate getData() {
		return this.data;
	}

	public void setData(LocalDate data) {
		this.data = data;
	}
    //====================----------====================

    
    //====================----------====================
	public LocalTime getHorario() {
		return this.horario;
	}

	public void setHorario(LocalTime horario) {
		this.horario = horario;
	}
    //====================----------====================

    
    //====================----------====================
	public String getProblema_texto() {
		return this.problema_texto;
	}

	public void setProblema_texto(String problema_texto) {
		this.problema_texto = problema_texto;
	}
    //====================----------====================

    
    //====================----------====================
	public List<String> getReceita_saude() {
		return this.receita_saude;
	}

	public void setReceita_saude(List<String> receita_saude) {
		this.receita_saude = receita_saude;
	}
    //====================----------====================

    
    //====================----------====================
	public ProfissionalDeSaude getProfissional() {
		return this.profissional;
	}

	public void setProfissional(ProfissionalDeSaude profissional) {
		this.profissional = profissional;
	}
    //====================----------====================

}
