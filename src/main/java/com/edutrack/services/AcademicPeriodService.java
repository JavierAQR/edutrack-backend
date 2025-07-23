package com.edutrack.services;

import com.edutrack.entities.AcademicPeriod;
import com.edutrack.repositories.AcademicPeriodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AcademicPeriodService {

    @Autowired
    private AcademicPeriodRepository academicPeriodRepository;

    public List<AcademicPeriod> findAll() {
        return academicPeriodRepository.findAll();
    }

    public Optional<AcademicPeriod> findById(Long id) {
        return academicPeriodRepository.findById(id);
    }

    public AcademicPeriod save(AcademicPeriod academicPeriod) {
        return academicPeriodRepository.save(academicPeriod);
    }

    public void deleteById(Long id) {
        academicPeriodRepository.deleteById(id);
    }
}