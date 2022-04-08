package com.example.demo.student;

import com.example.demo.student.exception.BadRequestException;
import com.example.demo.student.exception.StudentNotFoundException;
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
import static org.mockito.Mockito.*;

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

        when(studentRepository.selectExistsEmail(student.getEmail())).thenReturn(true);


//        given(studentRepository.selectExistsEmail(student.getEmail()))
//                .willReturn(true);

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
    void canDeleteStudent() {
        //given
        Long studentId = 1L;
        given(studentRepository.existsById(studentId))
                .willReturn(true);
        //when

        underTest.deleteStudent(studentId);

        //then
        ArgumentCaptor<Long> studentIdArgumentCaptor
                = ArgumentCaptor.forClass(Long.class);
        verify(studentRepository)
                .deleteById(studentIdArgumentCaptor.capture());

        Long capturedId = studentIdArgumentCaptor.getValue();
        assertThat(capturedId).isEqualTo(studentId);
    }

    @Test
    void throwsErrorWhenIdDoesNotExist() {
        //given
        Long studentId = 1L;

        given(studentRepository.existsById(studentId))
                .willReturn(false);

        //then
        //when

        assertThatExceptionOfType(StudentNotFoundException.class).isThrownBy(() -> {
                    underTest.deleteStudent(studentId);
                })
                .withMessageContaining("Student with id " + studentId + " does not exists");

        verify(studentRepository, never()).deleteById(studentId);
    }
}