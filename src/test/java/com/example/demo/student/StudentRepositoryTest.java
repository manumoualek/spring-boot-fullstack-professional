package com.example.demo.student;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
class StudentRepositoryTest {

    @Autowired
    private StudentRepository underTest;

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }

    @Test
    void itShouldCheckIfStudentEmailExists() {
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

    @Test
    void itShouldCheckIfStudentEmailDoesNotExist() {
        //given
        String email = "manu@gmail.com";

        //when
        boolean expected = underTest.selectExistsEmail(email);

        //then
        assertThat(expected).isFalse();

    }
}