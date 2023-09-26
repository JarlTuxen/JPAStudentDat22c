package dk.kea.jpastudentdat22c.service;

import dk.kea.jpastudentdat22c.dto.StudentConverter;
import dk.kea.jpastudentdat22c.dto.StudentDTO;
import dk.kea.jpastudentdat22c.exception.StudentNotFoundException;
import dk.kea.jpastudentdat22c.model.Student;
import dk.kea.jpastudentdat22c.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StudentService {
    private final StudentRepository studentRepository;
    private final StudentConverter studentConverter;

    @Autowired
    public StudentService(
            StudentRepository studentRepository,
            StudentConverter studentConverter
    ){
      this.studentRepository = studentRepository;
      this.studentConverter = studentConverter;
    }

    public List<StudentDTO> getAllStudents(){
        List<Student> students = studentRepository.findAll();
        return students.stream()
                .map(studentConverter::toDTO)
                .collect(Collectors.toList());
    }

    public StudentDTO getStudentById(int id){
        Optional<Student> optionalStudent = studentRepository.findById(id);
        if (optionalStudent.isPresent()){
            return studentConverter.toDTO(optionalStudent.get());
        } else {
            throw new StudentNotFoundException("Student not found with id: " + id);
        }
    }

    public StudentDTO createStudent(StudentDTO studentDTO){
        Student studentToSave = studentConverter.toEntity(studentDTO);
        //ensure it's a create
        studentToSave.setId(0);
        Student savedStudent = studentRepository.save(studentToSave);
        return studentConverter.toDTO(savedStudent);
    }

    public StudentDTO updateStudent(int id, StudentDTO studentDTO){
        Optional<Student> existingStudent = studentRepository.findById(id);
        if (existingStudent.isPresent()){
            Student studentToUpdate = studentConverter.toEntity(studentDTO);
            //ensure it's the id from the path that is updated
            studentToUpdate.setId(id);
            Student savedStudent = studentRepository.save(studentToUpdate);
            return studentConverter.toDTO(savedStudent);
        } else {
            throw new StudentNotFoundException("Student not found with id: " + id);
        }
    }
}
