package com.edutrack.repositories;

import com.edutrack.entities.InstitutionGrade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InstitutionGradeRepository extends JpaRepository<InstitutionGrade, Long> {

    List<InstitutionGrade> findByInstitutionId(Long institutionId);

    Optional<InstitutionGrade> findByInstitutionIdAndGradeId(Long institutionId, Long gradeId);
}