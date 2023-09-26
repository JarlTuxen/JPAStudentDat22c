package dk.kea.jpastudentdat22c.repository;

import dk.kea.jpastudentdat22c.model.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

@DataJpaTest
class StudentRepositoryTest {

    @Autowired
    StudentRepository studentRepository;

    @BeforeEach
    void init(){
        Student s1 = new Student(
                42,
                "Tim",
                LocalDate.of(2023, 9, 26),
                LocalTime.of(13, 12, 55)
        );

        Student s2 = new Student(
                666,
                "The Beast",
                LocalDate.of(0, 12, 24),
                LocalTime.of(0, 0, 0)
        );
        studentRepository.save(s2);
        studentRepository.save(s1);
    }

    @Test
    void testFindByName(){
        List<Student> studentList = studentRepository.findAllByName("Tim");
        assertEquals(1, studentList.size());
    }

    @Test
    void findAllByName() {
        //Act
        List<Student> studentList = studentRepository.findAll();
        //Assert
        assertEquals(2, studentList.size());
        assertThat(studentList, containsInAnyOrder(
                hasProperty("name", is("The Beast")),
                hasProperty("name", is("Tim"))
        ));
    }
}