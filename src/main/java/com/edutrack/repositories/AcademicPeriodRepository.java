package com.edutrack.repositories;

import com.edutrack.entities.AcademicPeriod;
import com.edutrack.entities.Institution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AcademicPeriodRepository extends JpaRepository<AcademicPeriod, Long> {
    List<AcademicPeriod> findByInstitution(Institution institution);
    List<AcademicPeriod> findByInstitutionAndActive(Institution institution, Boolean active);
}