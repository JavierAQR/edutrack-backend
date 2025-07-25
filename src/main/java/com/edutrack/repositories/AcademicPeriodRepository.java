package com.edutrack.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.edutrack.entities.AcademicPeriod;

public interface AcademicPeriodRepository extends JpaRepository<AcademicPeriod, Long> {
    
}