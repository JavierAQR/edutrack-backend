package com.edutrack.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.edutrack.entities.Section;

@Repository
public interface SectionRepository extends JpaRepository<Section, Long> {
    List<Section> findByInstitutionId(Long institutionId);
    List<Section> findByStudents_Id(Long studentId);
    List<Section> findByTeacherId(Long teacherId);
}
