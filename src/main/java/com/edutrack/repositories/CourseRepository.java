package com.edutrack.repositories;

import com.edutrack.entities.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    boolean existsByNameAndGradeId(String name, Long gradeId);
}
