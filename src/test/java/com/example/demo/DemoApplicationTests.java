package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

class DemoApplicationTests {

	Calculator underTest = new Calculator();

	@Test
	void itShouldAddNumber() {
		// given
		int numberOne = 20;
		int numberTwo = 30;

		// when
		int result = underTest.add(numberOne, numberTwo);

		//then


	}

	static class Calculator{
		int add(int a, int b){
			return a + b;
		}
	}

}
