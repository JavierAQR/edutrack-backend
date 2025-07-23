package com.edutrack.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.edutrack.entities.AssignmentSubmission;

public interface AssignmentSubmissionRepository extends JpaRepository<AssignmentSubmission, Long> {
    List<AssignmentSubmission> findByAssignmentId(Long assignmentId);

    Optional<AssignmentSubmission> findByAssignmentIdAndStudentId(Long assignmentId, Long studentId);

    @Query("SELECT AVG(s.grade) FROM AssignmentSubmission s WHERE s.student.id = :studentId AND s.assignment.section.id = :sectionId AND s.grade IS NOT NULL")
    Double findAverageGradeByStudentInSection(@Param("studentId") Long studentId, @Param("sectionId") Long sectionId);

    @Query("""
        SELECT s FROM AssignmentSubmission s
        WHERE s.student.id = :studentId
        AND s.assignment.section.id = :sectionId
    """)
    List<AssignmentSubmission> findByStudentIdAndSectionId(@Param("studentId") Long studentId, @Param("sectionId") Long sectionId);

}
