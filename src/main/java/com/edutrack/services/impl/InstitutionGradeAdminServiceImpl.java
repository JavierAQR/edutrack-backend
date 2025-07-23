package com.edutrack.services.impl;

import com.edutrack.entities.Institution;
import com.edutrack.entities.InstitutionGrade;
import com.edutrack.entities.User;
import com.edutrack.repositories.InstitutionGradeRepository;
import com.edutrack.repositories.UserRepository;
import com.edutrack.services.InstitutionGradeAdminService;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InstitutionGradeAdminServiceImpl implements InstitutionGradeAdminService {

    @Autowired
    private UserRepository userRepository;

    @Autowired  
    private final InstitutionGradeRepository institutionGradeRepository;


    private Institution getCurrentAdminInstitution() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
    
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    
        Institution institution = user.getInstitution();
        if (institution == null) {
            throw new RuntimeException("El usuario no tiene institución asignada");
        }
    
        return institution;
    }

    @Override
    public List<InstitutionGrade> getGradesForCurrentInstitutionAdmin() {
        Institution institution = getCurrentAdminInstitution();
        return institutionGradeRepository.findByInstitutionId(institution.getId());
    }

    @Override
    public InstitutionGrade assignGrade(InstitutionGrade grade) {
        Institution institution = getCurrentAdminInstitution();
        grade.setInstitution(institution);
        return institutionGradeRepository.save(grade);
    }

    @Override
    public InstitutionGrade updateGrade(Long id, InstitutionGrade grade) {
        InstitutionGrade existing = institutionGradeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Asignación no encontrada"));
        grade.setId(existing.getId());
        grade.setInstitution(existing.getInstitution());
        return institutionGradeRepository.save(grade);
    }

    @Override
    public void deleteGrade(Long id) {
        institutionGradeRepository.deleteById(id);
    }
}
