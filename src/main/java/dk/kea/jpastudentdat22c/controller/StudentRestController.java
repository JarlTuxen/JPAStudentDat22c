package dk.kea.jpastudentdat22c.controller;

import dk.kea.jpastudentdat22c.dto.StudentConverter;
import dk.kea.jpastudentdat22c.dto.StudentDTO;
import dk.kea.jpastudentdat22c.model.Student;
import dk.kea.jpastudentdat22c.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
public class StudentRestController {

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    StudentConverter studentConverter;

    @GetMapping("/students")
    public List<StudentDTO> getAllStudents(){
       List<Student> studentList = studentRepository.findAll();
       List<StudentDTO> studentDTOList = new ArrayList<>();
       studentList.forEach(s -> {
           studentDTOList.add(studentConverter.toDTO(s));
       });
       return studentDTOList;
    }

    @PostMapping("/student")
    @ResponseStatus(HttpStatus.CREATED)
    public StudentDTO postStudent(@RequestBody StudentDTO studentDTO){
        Student student = studentConverter.toEntity(studentDTO);
        student.setId(0); //sikr at ny student oprettes - undgå overskrivning af eksisterende
        studentRepository.save(student);
        System.out.println(student);
        return studentConverter.toDTO(student);
    }

    @GetMapping("/student/{id}")
    public ResponseEntity<StudentDTO> getStudentById(@PathVariable("id") int id){
        Optional<Student> optionalStudent = studentRepository.findById(id);
        if (optionalStudent.isPresent()){
            Student student = optionalStudent.get();
            return ResponseEntity.ok(studentConverter.toDTO(student));
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/student/{id}")
    public ResponseEntity<StudentDTO> putStudent(@PathVariable("id") int id, @RequestBody StudentDTO studentDTO){
        Optional<Student> optionalStudent = studentRepository.findById(id);
        if (optionalStudent.isPresent()){
            Student student = studentConverter.toEntity(studentDTO);
            student.setId(id); //sikr at id fra path bruges til update
            studentRepository.save(student);
            //return new ResponseEntity<>(student, HttpStatus.OK);
            return ResponseEntity.ok(studentConverter.toDTO(student));
        }
        else {
            //return new ResponseEntity<>(new Student(), HttpStatus.NOT_FOUND);
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/student/{id}")
    public ResponseEntity<String> deleteStudent(@PathVariable("id") int id){
        Optional<Student> optionalStudent = studentRepository.findById(id);
        if (optionalStudent.isPresent()){
            studentRepository.deleteById(id);
            return ResponseEntity.ok("Student deleted");
        }
        else {
            //return new ResponseEntity<>(new Student(), HttpStatus.NOT_FOUND);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Student not found");
        }
    }

    @GetMapping("/students/{name}")
    public List<Student> getAllStudentsByName(@PathVariable String name){
        return studentRepository.findAllByName(name);
    }

}
