package com.edutrack.services;

import com.edutrack.dto.request.StudentCreateDTO;
import com.edutrack.dto.request.StudentUpdateDTO;
import com.edutrack.dto.StudentInfoDTO;
import com.edutrack.entities.*;
import com.edutrack.entities.enums.UserType;
import com.edutrack.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final UserRepository userRepository;
    private final StudentProfileRepository studentProfileRepository;
    private final GradeRepository gradeRepository;
    private final PasswordEncoder passwordEncoder;
    private final InstitutionRepository institutionRepository;

    public List<StudentInfoDTO> getAllStudents() {
        List<User> students = userRepository.findByUserType(UserType.STUDENT);
        return students.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public StudentInfoDTO getStudentById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Estudiante no encontrado"));

        if (user.getUserType() != UserType.STUDENT) {
            throw new RuntimeException("El usuario no es un estudiante");
        }

        return convertToDTO(user);
    }

    @Transactional
    public StudentInfoDTO createStudent(StudentCreateDTO studentDTO) {
        User user = new User();
        user.setUsername(studentDTO.getUsername());
        user.setName(studentDTO.getName());
        user.setLastname(studentDTO.getLastname());
        user.setEmail(studentDTO.getEmail());
        user.setPassword(passwordEncoder.encode(studentDTO.getPassword()));
        user.setBirthdate(studentDTO.getBirthdate());
        user.setEnabled(true);

        // Buscar la institución por ID
        Institution institution = institutionRepository.findById(studentDTO.getInstitutionId())
                .orElseThrow(() -> new RuntimeException("Institución no encontrada"));
        user.setInstitution(institution);

        user.setUserType(UserType.STUDENT);

        User savedUser = userRepository.save(user);

        StudentProfile profile = new StudentProfile();
        profile.setUser(savedUser);

        if (studentDTO.getGradeId() != null) {
            Grade grade = gradeRepository.findById(studentDTO.getGradeId())
                    .orElseThrow(() -> new RuntimeException("Grado no encontrado"));
            profile.setGrade(grade);
        }

        studentProfileRepository.save(profile);

        return convertToDTO(savedUser);
    }

    @Transactional
    public StudentInfoDTO updateStudent(Long id, StudentUpdateDTO studentDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Estudiante no encontrado"));

        user.setName(studentDTO.getName());
        user.setLastname(studentDTO.getLastname());
        user.setEmail(studentDTO.getEmail());
        user.setBirthdate(studentDTO.getBirthdate());
        user.setEnabled(studentDTO.getEnabled());

        StudentProfile profile = studentProfileRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Perfil de estudiante no encontrado"));

        if (studentDTO.getGradeId() != null) {
            Grade grade = gradeRepository.findById(studentDTO.getGradeId())
                    .orElseThrow(() -> new RuntimeException("Grado no encontrado"));
            profile.setGrade(grade);
        } else {
            profile.setGrade(null);
        }

        userRepository.save(user);
        studentProfileRepository.save(profile);

        return convertToDTO(user);
    }

    @Transactional
    public void deleteStudent(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Estudiante no encontrado"));

        studentProfileRepository.deleteByUser(user);
        userRepository.delete(user);
    }

    private StudentInfoDTO convertToDTO(User user) {
        StudentProfile profile = studentProfileRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Perfil de estudiante no encontrado"));

        return StudentInfoDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .name(user.getName())
                .lastname(user.getLastname())
                .email(user.getEmail())
                .birthdate(user.getBirthdate().toString())
                .enabled(user.getEnabled())
                .userType(user.getUserType().toString())
                .gradeId(profile.getGrade() != null ? profile.getGrade().getId() : null)
                .gradeName(profile.getGrade() != null ? profile.getGrade().getName() : null)
                .academicLevel(profile.getGrade() != null && profile.getGrade().getAcademicLevel() != null
                        ? profile.getGrade().getAcademicLevel().getName()
                        : null)
                .build();
    }

}
