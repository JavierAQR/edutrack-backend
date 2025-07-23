package com.edutrack.controllers;

import com.edutrack.entities.InstitutionGrade;
import com.edutrack.services.InstitutionAdminService;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/institution-admin")
@RequiredArgsConstructor
public class InstitutionAdminController {

    @Autowired
    private final InstitutionAdminService institutionAdminService;

    @GetMapping("/grades")
    public List<InstitutionGrade> getGradesForCurrentInstitution() {
        return institutionAdminService.getGradesForCurrentAdminInstitution();
    }
}
