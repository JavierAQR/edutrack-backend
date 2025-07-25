package com.edutrack.controllers;

import com.edutrack.repositories.*;
import com.edutrack.entities.enums.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/admin/dashboard")
public class DashboardAdminController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private InstitutionRepository institutionRepository;
    @Autowired
    private AcademicLevelRepository academicLevelRepository;
    @Autowired
    private GradeRepository GradeRepository;
    @Autowired
    private CourseRepository courseRepository;

    @GetMapping("/counts")
    public Map<String, Long> getCounts() {
        long admins = userRepository.countByUserType(UserType.ADMIN);
        long directors = userRepository.countByUserType(UserType.DIRECTOR);
        long teachers = userRepository.countByUserType(UserType.TEACHER);
        long students = userRepository.countByUserType(UserType.STUDENT);
        long institutions = institutionRepository.count();
        long academicLevels = academicLevelRepository.count();
        long academicGrades = GradeRepository.count();
        long courses = courseRepository.count();

        return Map.of(
            "admins", admins,
            "directors", directors,
            "teachers", teachers,
            "students", students,
            "institutions", institutions,
            "academicLevels", academicLevels,
            "academicGrades", academicGrades,
            "courses", courses
        );
    }
}