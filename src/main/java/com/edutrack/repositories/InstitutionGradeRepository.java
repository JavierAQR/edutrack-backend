package com.edutrack.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.edutrack.entities.InstitutionGrade;

@Repository
public interface InstitutionGradeRepository extends JpaRepository<InstitutionGrade, Long> {

    List<InstitutionGrade> findByInstitutionId(Long id);
}
