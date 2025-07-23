package com.edutrack.repositories;

import com.edutrack.dto.response.StudentByGradeResponse;
import com.edutrack.entities.StudentProfile;
import com.edutrack.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StudentProfileRepository extends JpaRepository<StudentProfile, Long> {
    Optional<StudentProfile> findByUser(User user);

    Optional<StudentProfile> findByUserId(Long userId);

    List<StudentProfile> findByUser_Institution_Id(Long institutionId);

    boolean existsByUser(User user);

    boolean existsByUserId(Long userId);

    void deleteByUser(User user);

    @Query("""
                SELECT new com.edutrack.dto.response.StudentByGradeResponse(s.id, u.name, u.lastname)
                FROM StudentProfile s
                JOIN s.user u
                WHERE s.grade.id = :gradeId AND u.institution.id = :institutionId
            """)
    List<StudentByGradeResponse> findByGradeAndInstitution(@Param("gradeId") Long gradeId,
            @Param("institutionId") Long institutionId);
}
