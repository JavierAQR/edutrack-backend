package com.edutrack.repositories;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.edutrack.entities.PrecioInstitution;

public interface PrecioInstitutionRepository extends JpaRepository<PrecioInstitution, Long> {
    List<PrecioInstitution> findByInstitutionId(Long institutionId);
}
