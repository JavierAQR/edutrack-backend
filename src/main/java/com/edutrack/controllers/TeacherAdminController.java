package com.edutrack.controllers;

import com.edutrack.dto.ApiResponse;
import com.edutrack.dto.request.TeacherCreateDTO;
import com.edutrack.dto.request.TeacherUpdateDTO;
import com.edutrack.dto.TeacherInfoDTO;
import com.edutrack.services.TeacherService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/teachers")
public class TeacherAdminController {

    private final TeacherService teacherService;

    public TeacherAdminController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAllTeachers() {
        List<TeacherInfoDTO> teachers = teacherService.getAllTeachers();
        return ResponseEntity.ok(new ApiResponse("Lista de profesores obtenida", teachers));
    }

    @PostMapping
    public ResponseEntity<ApiResponse> createTeacher(@RequestBody @Valid TeacherCreateDTO teacherDTO) {
        TeacherInfoDTO createdTeacher = teacherService.createTeacher(teacherDTO);
        return ResponseEntity.ok(new ApiResponse("Profesor creado exitosamente", createdTeacher));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateTeacher(
            @PathVariable Long id,
            @RequestBody @Valid TeacherUpdateDTO teacherDTO) {
        TeacherInfoDTO updatedTeacher = teacherService.updateTeacher(id, teacherDTO);
        return ResponseEntity.ok(new ApiResponse("Profesor actualizado exitosamente", updatedTeacher));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteTeacher(@PathVariable Long id) {
        teacherService.deleteTeacher(id);
        return ResponseEntity.ok(new ApiResponse("Profesor eliminado exitosamente"));
    }
}