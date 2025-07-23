package com.edutrack.controllers;

import com.edutrack.dto.ApiResponse;
import com.edutrack.dto.request.StudentCreateDTO;
import com.edutrack.dto.request.StudentUpdateDTO;
import com.edutrack.dto.StudentInfoDTO;
import com.edutrack.services.StudentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/students")
public class StudentAdminController {

    private final StudentService studentService;

    public StudentAdminController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAllStudents() {
        List<StudentInfoDTO> students = studentService.getAllStudents();
        return ResponseEntity.ok(new ApiResponse("Lista de estudiantes obtenida", students));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getStudentById(@PathVariable Long id) {
        StudentInfoDTO student = studentService.getStudentById(id);
        return ResponseEntity.ok(new ApiResponse("Estudiante obtenido exitosamente", student));
    }

    @PostMapping
    public ResponseEntity<ApiResponse> createStudent(@RequestBody @Valid StudentCreateDTO studentDTO) {
        StudentInfoDTO createdStudent = studentService.createStudent(studentDTO);
        return ResponseEntity.ok(new ApiResponse("Estudiante creado exitosamente", createdStudent));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateStudent(
            @PathVariable Long id,
            @RequestBody @Valid StudentUpdateDTO studentDTO) {
        StudentInfoDTO updatedStudent = studentService.updateStudent(id, studentDTO);
        return ResponseEntity.ok(new ApiResponse("Estudiante actualizado exitosamente", updatedStudent));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.ok(new ApiResponse("Estudiante eliminado exitosamente"));
    }
}
