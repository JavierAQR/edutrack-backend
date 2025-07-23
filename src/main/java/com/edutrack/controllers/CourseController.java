package com.edutrack.controllers;

import com.edutrack.dto.request.CourseDTO;
import com.edutrack.entities.*;
import com.edutrack.repositories.CourseRepository;
import com.edutrack.services.CourseService;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseRepository courseRepository;

   @Autowired
    private CourseService courseService;

    @GetMapping
    public List<CourseDTO> getAll() {
        return courseService.getAll();
    }

    @GetMapping("/{id}")
    public Course getById(@PathVariable Long id) {
        return courseService.getById(id);
    }

    @PostMapping
    public Course create(@RequestBody CourseDTO dto) {
        return courseService.create(dto);
    }

    @PutMapping("/{id}")
    public Course update(@PathVariable Long id, @RequestBody CourseDTO dto) {
        return courseService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        courseService.delete(id);
    }

    @GetMapping("/by-grade/{gradeId}")
    public List<Course> getCoursesByGrade(@PathVariable Long gradeId) {
    return courseRepository.findByGradeId(gradeId);
}
}
