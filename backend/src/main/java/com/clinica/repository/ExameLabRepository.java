package com.agenda.repository;

import com.agenda.model.ExameLab;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ExameLabRepository extends JpaRepository<ExameLab, Long> {

    List<ExameLab> findAllByOrderByAtendimento_idAsc();

    List<ExameLab> findByAtendimento_id(Int atendimento_id);

}
