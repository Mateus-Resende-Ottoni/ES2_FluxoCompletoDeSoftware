package com.clinica.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.Type;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

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
    @Type(org.hibernate.type.StringArrayType.class)
    @Column(columnDefinition = "text[]")
    private String[] receita_saude;

    // Ligação com ProfissionalDeSaúde
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "profissional_id")
    private ProfissionalDeSaude profissional;

    //====================----------====================
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
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
	public String[] getReceita_saude() {
		return this.receita_saude;
	}

	public void setReceita_saude(String[] receita_saude) {
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
