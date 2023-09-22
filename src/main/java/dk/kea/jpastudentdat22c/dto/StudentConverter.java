package dk.kea.jpastudentdat22c.dto;

import dk.kea.jpastudentdat22c.model.Student;
import org.springframework.stereotype.Component;

@Component
public class StudentConverter {
    public Student toEntity(StudentDTO studentDTO){
        return new Student(
                studentDTO.id(),
                studentDTO.name(),
                studentDTO.bornDate(),
                studentDTO.bornTime()
        );
    }

    public StudentDTO toDTO(Student student){
        return new StudentDTO(
                student.getId(),
                student.getName(),
                student.getBornDate(),
                student.getBornTime()
        );
    }
}
