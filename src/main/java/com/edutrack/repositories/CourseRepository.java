package com.edutrack.repositories;

import com.edutrack.entities.Course;
import com.edutrack.entities.Grade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    boolean existsByNameAndGradeId(String name, Long gradeId);

    List<Course> findByGrade(Grade grade);
    List<Course> findByGradeId(Long gradeId);

    @Query("SELECT c FROM Course c JOIN InstitutionGrade ig ON c.grade.id = ig.grade.id " +
            "WHERE ig.institution.id = :institutionId")
    List<Course> findByInstitutionId(Long institutionId);

    boolean existsByNameAndGradeIdAndIdNot(String name, Long gradeId, Long courseId);
}
