package dk.kea.jpastudentdat22c.service;

import dk.kea.jpastudentdat22c.dto.StudentConverter;
import dk.kea.jpastudentdat22c.dto.StudentDTO;
import dk.kea.jpastudentdat22c.exception.StudentNotFoundException;
import dk.kea.jpastudentdat22c.model.Student;
import dk.kea.jpastudentdat22c.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

@SpringBootTest
class StudentServiceMockTest {

    @Mock
    private StudentRepository mockedStudentRepository;

    //autowired dependencies in studentservice does not initialise when using InjectMocks
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

        // Define the behavior using thenAnswer
        Mockito.when(mockedStudentRepository.save(ArgumentMatchers.any(Student.class))).thenAnswer(new Answer<Student>() {
            @Override
            public Student answer(InvocationOnMock invocation) throws Throwable {
                // Extract the student object passed as an argument to the save method
                Object[] arguments = invocation.getArguments();
                if (arguments.length > 0 && arguments[0] instanceof Student) {
                    Student studentToSave = (Student) arguments[0];
                    //repository skal returnere studentobject med næste ledige id = 3
                    studentToSave.setId(3);
                    return studentToSave;
                } else {
                    // Handle the case where the argument is not a Student (optional)
                    throw new IllegalArgumentException("Invalid argument type");
                }
            }
        });

// Now you can use your mocked StudentRepository for testing

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
        StudentDTO resultStudentDTO = studentService.createStudent(studentConverter.toDTO(
                new Student(
                0,
                "Hugo",
                LocalDate.of(2000,1,1),
                LocalTime.of(0, 0, 1)
        )));
        assertEquals(3, resultStudentDTO.id());
    }

    @Test
    void updateStudent() {
        //kald studentService.save og assertEquals på studentDTO - husk init Mockito.when.thenReturn
        //lav test for eksisterende og ikke eksisterende student (assertThrows)
    }

    @Test
    void deleteStudentById() {
        //kald studentService.delete
        //test at der kommer StudentNotFoundException for ikke-eksisterende student
    }
}