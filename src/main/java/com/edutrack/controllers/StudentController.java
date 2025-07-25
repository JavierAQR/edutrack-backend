package com.edutrack.controllers;


// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// import com.edutrack.dto.request.StudentRegisterDTO;
// import com.edutrack.entities.StudentDetails;
// import com.edutrack.services.StudentService;

// import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {

    // private final StudentService studentService;
    
    // @PostMapping()
    // public ResponseEntity<StudentDetails> registerStudent(@Valid @RequestBody StudentRegisterDTO studentDTO) {
    //     StudentDetails student = studentService.createStudent(studentDTO);
    //     return ResponseEntity.status(HttpStatus.CREATED).body(student);
    // }
    
    // @Autowired
    // private StudentService studentService;

    // @GetMapping()
    // @Transactional(readOnly = true)
    // public List<Student> findAllStudents(){
    //     return this.studentService.findAll();
    // }

    // @PostMapping()
    // @Transactional
    // public Student save(@RequestBody Student student){
    //     return this.studentService.save(student);
    // }

    // @GetMapping("/{id}")
    // @Transactional(readOnly = true)
    // public Student getStudentById(@PathVariable Long id){
    //     return this.studentService.findById(id);
    // }

    // @PutMapping("/{id}")
    // @Transactional
    // public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Student student){
    //     Optional<Student> s = Optional.of(this.studentService.findById(id));
    //     if(s.isPresent()){
    //         Student newStudent = s.get();
    //         newStudent.setName(student.getName());
    //         newStudent.setLastname(student.getLastname());
    //         newStudent.setEmail(student.getEmail());
    //         newStudent.setActive(student.getActive());
    //         newStudent.setPassword(student.getPassword());
    //         newStudent.setBirthdate(student.getBirthdate());
    //         newStudent.setInstitution(student.getInstitution());
    //         newStudent.setAcademicLevel(student.getAcademicLevel());
    //         newStudent.setAverageGrade(student.getAverageGrade());
             
    //         return ResponseEntity.status(HttpStatus.CREATED).body(this.studentService.update(id, newStudent));
    //     }

    //     return ResponseEntity.notFound().build();
    // }

    // @DeleteMapping("/{id}")
    // @Transactional
    // public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
    //     studentService.delete(id); 
    //     return ResponseEntity.noContent().build();
    // }

}
