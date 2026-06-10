package com.clinica.repository;

import com.clinica.model.ExameLab;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ExameLabRepository extends JpaRepository<ExameLab, Long> {

    List<ExameLab> findAllByOrderByIdAsc();

    List<ExameLab> findAllByOrderByAtendimento_idAsc();

    List<ExameLab> findByAtendimento_id(int atendimento_id);

}
