package com.example.demo.student;

import com.example.demo.student.exception.BadRequestException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;
    private StudentService underTest;



    @BeforeEach
    void setUp() {
        underTest = new StudentService(studentRepository);
    }

    @Test
    void canGetAllStudents() {
        //when
        underTest.getAllStudents();
        //then
        verify(studentRepository).findAll();
    }

    @Test
    void canAddStudent() {
        //given
        Student student = new Student(
                "Manu",
                "manu@gmail.com",
                Gender.MALE
        );
        //when
        underTest.addStudent(student);
        //then
        ArgumentCaptor<Student> studentArgumentCaptor
                = ArgumentCaptor.forClass(Student.class);
        verify(studentRepository)
                .save(studentArgumentCaptor.capture());

        Student capturedStudent = studentArgumentCaptor.getValue();

        assertThat(capturedStudent).isEqualTo(student);
    }

    @Test
    void willThrowErrorWhenEmailAlreadyExists() {
        //given
        Student student = new Student(
                "Manu",
                "manu@gmail.com",
                Gender.MALE
        );

        given(studentRepository.selectExistsEmail(student.getEmail()))
                .willReturn(true);

        //when
        //then

//        assertThatThrownBy(() ->underTest.addStudent(student))
//                .isInstanceOf(BadRequestException.class)
//                .hasMessageContaining("Email " + student.getEmail() + " taken");

        assertThatExceptionOfType(BadRequestException.class).isThrownBy(() -> { underTest.addStudent(student); })
                .withMessageContaining("Email " + student.getEmail() + " taken");

        verify(studentRepository, never()).save(student);
    }

    @Test
    @Disabled
    void deleteStudent() {
        //given
        Long studentId = 1L;
        //when
        underTest.deleteStudent(studentId);
        //then
        ArgumentCaptor<Long> studentIdArgumentCaptor
                = ArgumentCaptor.forClass(Long.class);
        verify(studentRepository)
                .deleteById(studentIdArgumentCaptor.capture());

        Long capturedStudentId = studentIdArgumentCaptor.getValue();

        assertThat(capturedStudentId).isEqualTo(studentId);

    }
}