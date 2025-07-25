package com.edutrack.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.edutrack.entities.Institution;

public interface InstitutionRepository extends JpaRepository<Institution, Long> {
    
}
