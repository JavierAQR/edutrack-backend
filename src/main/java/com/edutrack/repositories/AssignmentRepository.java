package com.edutrack.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.edutrack.entities.Assignment;

public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
    List<Assignment> findBySectionId(Long sectionId);

    @Query("""
                SELECT a FROM Assignment a
                WHERE a.section.id = :sectionId
                AND a.id NOT IN (
                    SELECT s.assignment.id FROM AssignmentSubmission s
                    WHERE s.student.id = :studentId
                )
            """)
    List<Assignment> findPendingAssignmentsForStudent(@Param("sectionId") Long sectionId,
            @Param("studentId") Long studentId);
}
