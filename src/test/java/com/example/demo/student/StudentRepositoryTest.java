package com.example.demo.student;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.*;

class StudentRepositoryTest {

    @Autowired
    private StudentRepository underTest;

    @Test
    void itShouldCheckIfStudentExistsEmail() {
     //given
        Student student = new Student(
                "Manu",
                "manu@gmail.com",
                Gender.MALE
        );
        underTest.save(student);

     //when
        boolean expected = underTest.selectExistsEmail("manu@gmail.com");

     //then
        assertThat(expected).isTrue();

    }
}