package com.edutrack.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.edutrack.entities.AcademicLevel;
import com.edutrack.repositories.AcademicLevelRepository;

@Service
public class AcademicLevelService {
    
     @Autowired
    private AcademicLevelRepository academicLevelRepository;

    public List<AcademicLevel> getAll() {
        return academicLevelRepository.findAll();
    }

    public Optional<AcademicLevel> getById(Long id) {
        return academicLevelRepository.findById(id);
    }

    public AcademicLevel create(AcademicLevel level) {
        return academicLevelRepository.save(level);
    }

    public AcademicLevel update(Long id, AcademicLevel level) {
        return academicLevelRepository.findById(id).map(existing -> {
            existing.setName(level.getName());
            return academicLevelRepository.save(existing);
        }).orElseThrow(() -> new RuntimeException("AcademicLevel no encontrado con ID: " + id));
    }

    public void delete(Long id) {
        academicLevelRepository.deleteById(id);
    }
}
