package dk.kea.jpastudentdat22c.controller;

import dk.kea.jpastudentdat22c.model.Student;
import dk.kea.jpastudentdat22c.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
public class StudentRestController {

    @Autowired
    StudentRepository studentRepository;

    @GetMapping("/students")
    public List<Student> getAllStudents(){
       List<Student> lst = studentRepository.findAll();
       return lst;
    }

    @PostMapping("/student")
    @ResponseStatus(HttpStatus.CREATED)
    public Student postStudent(@RequestBody Student student){
        student.setId(0); //sikr at ny student oprettes - undgå overskrivning af eksisterende
        System.out.println(student);
        return studentRepository.save(student);
    }

    @GetMapping("/student/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable("id") int id){
        Optional<Student> optionalStudent = studentRepository.findById(id);
        if (optionalStudent.isPresent()){
            return ResponseEntity.ok(optionalStudent.get());
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/student/{id}")
    public ResponseEntity<Student> putStudent(@PathVariable("id") int id, @RequestBody Student student){
        Optional<Student> optionalStudent = studentRepository.findById(id);
        if (optionalStudent.isPresent()){
            student.setId(id); //sikr at id fra path bruges til update
            studentRepository.save(student);
            //return new ResponseEntity<>(student, HttpStatus.OK);
            return ResponseEntity.ok(student);
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
