package com.edutrack.repositories;

import java.util.List;

import com.edutrack.entities.AcademicLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.edutrack.entities.Grade;

@Repository
public interface GradeRepository extends JpaRepository<Grade, Long>{
    List<Grade> findByAcademicLevelId(Long academicLevelId);

    List<Grade> findByAcademicLevel_Name(String name);

    List<Grade> findByAcademicLevel(AcademicLevel academicLevel);

    @Query("SELECT g FROM Grade g JOIN InstitutionGrade ig ON g.id = ig.grade.id " +
            "WHERE g.academicLevel.id = :academicLevelId AND ig.institution.id = :institutionId")
    List<Grade> findByAcademicLevelIdAndInstitutionId(Long academicLevelId, Long institutionId);
}
