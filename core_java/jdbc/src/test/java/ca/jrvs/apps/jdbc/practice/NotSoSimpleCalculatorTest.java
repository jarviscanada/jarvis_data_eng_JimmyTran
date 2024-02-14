package ca.jrvs.apps.jdbc.practice;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class NotSoSimpleCalculatorTest {
	
	NotSoSimpleCalculator calc;
	
	@Mock
	SimpleCalculator mockSimpleCalc;
	
	@BeforeEach
	void init() {
		calc = new NotSoSimpleCalculatorImpl(mockSimpleCalc);
	}

	@Test
	void test_power() {
		int expected = 4;
        int actual = calc.power(2, 2);
        assertEquals(expected, actual);
	}
	
	@Test
	void test_abs() {

        when(mockSimpleCalc.multiply(anyInt(), anyInt())).thenReturn(10);
		//This test will currently fail
		//Consider if the provided logic in NotSoSimpleCalcualtorImpl is correct
		//Consider if you need to add anything to this test case (hint: you do)
		int expected = 10;
		int actual = calc.abs(10);
		assertEquals(expected, actual);
	}

	@Test
	void test_sqrt() {
		double expected = 2;
        double actual = calc.sqrt(4);
		assertEquals(expected, actual, 0.01);
	}

}