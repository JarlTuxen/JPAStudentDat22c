package dk.kea.jpastudentdat22c.service;

import dk.kea.jpastudentdat22c.dto.StudentDTO;
import dk.kea.jpastudentdat22c.exception.StudentNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class StudentServiceTest {

    @Autowired
    StudentService studentService;

    @Test
    void getAllStudents() {
        //Act
        List<StudentDTO> studentDTOList = studentService.getAllStudents();
        //Assert
        assertEquals(1, studentDTOList.size());
    }

    @Test
    void getStudentById() {
        StudentDTO studentDTO = studentService.getStudentById(1);
        assertEquals("Sigurd", studentDTO.name());
        assertThrows(StudentNotFoundException.class, () -> studentService.getStudentById(27));
    }
}