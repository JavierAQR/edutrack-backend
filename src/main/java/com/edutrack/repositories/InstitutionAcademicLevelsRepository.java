package com.edutrack.repositories;

import java.util.List;
import java.util.Optional;

import com.edutrack.entities.AcademicLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.edutrack.entities.InstitutionAcademicLevels;

@Repository
public interface InstitutionAcademicLevelsRepository extends JpaRepository<InstitutionAcademicLevels, Long>  {
    List<InstitutionAcademicLevels> findByInstitutionId(Long institutionId);
    Optional<InstitutionAcademicLevels> findByAcademicLevelId(Long academicLevelId);
    @Query("SELECT DISTINCT ial.academicLevel FROM InstitutionAcademicLevels ial WHERE ial.institution.id = :institutionId")
    List<AcademicLevel> findAcademicLevelsByInstitutionId(Long institutionId);
}
