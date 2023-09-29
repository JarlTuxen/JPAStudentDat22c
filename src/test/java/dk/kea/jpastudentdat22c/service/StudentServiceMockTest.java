package dk.kea.jpastudentdat22c.service;

import dk.kea.jpastudentdat22c.dto.StudentConverter;
import dk.kea.jpastudentdat22c.dto.StudentDTO;
import dk.kea.jpastudentdat22c.exception.StudentNotFoundException;
import dk.kea.jpastudentdat22c.model.Student;
import dk.kea.jpastudentdat22c.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class StudentServiceMockTest {

    @Mock
    private StudentRepository mockedStudentRepository;

    //autowired dependencies in studentservice does not initialise when using mock
    //@InjectMocks
    StudentService studentService;

    @Autowired
    StudentConverter studentConverter;

    @BeforeEach
    void init(){
        Student s1 = new Student();
        s1.setId(1);
        s1.setName("Sigurd");
        s1.setBornDate(LocalDate.of(2010,11,12));
        s1.setBornTime(LocalTime.of(23, 59, 59));

        Student s2 = new Student(
                2,
                "Ida",
                LocalDate.of(2000,1,1),
                LocalTime.of(0, 0, 1)
        );
        List<Student> studentList = new ArrayList<>();
        studentList.add(s1);
        studentList.add(s2);

        Mockito.when(mockedStudentRepository.findAll()).thenReturn(studentList);
        Mockito.when(mockedStudentRepository.findById(1)).thenReturn(Optional.of(s1));
        Mockito.when(mockedStudentRepository.findById(42)).thenReturn(Optional.empty());

        studentService = new StudentService(mockedStudentRepository, studentConverter);

    }

    @Test
    void getAllStudents() {
        //Act
        List<StudentDTO> studentDTOList = studentService.getAllStudents();
        //Assert
        assertEquals("Sigurd", studentDTOList.get(0).name());
        assertEquals("Ida", studentDTOList.get(1).name());
    }

    @Test
    void getStudentById() {
        //Act
        StudentDTO studentDTO = studentService.getStudentById(1);
        //Assert
        assertEquals("Sigurd", studentDTO.name());
        assertThrows(StudentNotFoundException.class, () -> studentService.getStudentById(42));
    }

    @Test
    void createStudent() {
        //kald studentService.save og assertEquals på studentDTO - husk init Mockito.when.thenReturn
    }

    @Test
    void updateStudent() {
    }

    @Test
    void deleteStudentById() {
    }
}