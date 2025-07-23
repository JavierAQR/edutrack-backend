package com.edutrack.services;

import java.util.List;

import com.edutrack.entities.InstitutionGrade;

public interface InstitutionAdminService {

    
    List<InstitutionGrade> getGradesForCurrentAdminInstitution();
}
