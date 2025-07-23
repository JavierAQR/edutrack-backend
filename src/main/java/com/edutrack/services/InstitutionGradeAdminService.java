package com.edutrack.services;

import com.edutrack.entities.InstitutionGrade;

import java.util.List;

public interface InstitutionGradeAdminService {
    List<InstitutionGrade> getGradesForCurrentInstitutionAdmin();

    InstitutionGrade assignGrade(InstitutionGrade grade);

    InstitutionGrade updateGrade(Long id, InstitutionGrade grade);

    void deleteGrade(Long id);
}
