package com.edutrack.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.edutrack.entities.Institution;
import com.edutrack.entities.InstitutionGrade;
import com.edutrack.entities.User;
import com.edutrack.repositories.InstitutionGradeRepository;
import com.edutrack.repositories.UserRepository;
import com.edutrack.services.InstitutionAdminService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InstitutionAdminServiceImpl implements InstitutionAdminService {

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final InstitutionGradeRepository institutionGradeRepository;


    @Override
    public List<InstitutionGrade> getGradesForCurrentAdminInstitution() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Institution institution = user.getInstitution();
        if (institution == null) {
            throw new RuntimeException("El usuario no tiene institución asociada");
        }

        return institutionGradeRepository.findByInstitutionId(institution.getId());
    }

}
