package com.edutrack.repositories;

import java.util.List;

import com.edutrack.entities.AcademicLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.edutrack.entities.Grade;

@Repository
public interface GradeRepository extends JpaRepository<Grade, Long>{
    List<Grade> findByAcademicLevelId(Long academicLevelId);

    List<Grade> findByAcademicLevel_Name(String name);

    List<Grade> findByAcademicLevel(AcademicLevel academicLevel);
}
