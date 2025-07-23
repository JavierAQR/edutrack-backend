package com.edutrack.repositories;

import com.edutrack.entities.Course;
import com.edutrack.entities.Grade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    boolean existsByNameAndGradeId(String name, Long gradeId);

    List<Course> findByGrade(Grade grade);

    // Método para buscar cursos por nombre
    List<Course> findByNameContainingIgnoreCase(String name);

    List<Course> findByGradeId(Long gradeId);
}
