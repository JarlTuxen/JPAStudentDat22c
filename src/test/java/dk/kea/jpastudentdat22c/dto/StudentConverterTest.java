package dk.kea.jpastudentdat22c.dto;

import dk.kea.jpastudentdat22c.model.Student;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class StudentConverterTest {

    @Autowired
    StudentConverter studentConverter;

    //Arrange
    StudentDTO studentDTOtest = new StudentDTO(
            42,
            "Neja",
            LocalDate.of(1444, 9, 14),
            LocalTime.of(12, 54, 17)
    );

    Student studenttest = new Student(
            42,
            "Neja",
            LocalDate.of(1444, 9, 14),
            LocalTime.of(12, 54, 17)
    );

    @Test
    void toEntityTest() {
        //Act
        Student resultStudent = studentConverter.toEntity(studentDTOtest);

        //Assert
        assertEquals(studentDTOtest.id(), resultStudent.getId());
    }

    @Test
    void toDTO() {
        //Act
        StudentDTO resultStudentDTO = studentConverter.toDTO(studenttest);
        //assert
        assertEquals(studenttest.getId(), resultStudentDTO.id());
    }
}