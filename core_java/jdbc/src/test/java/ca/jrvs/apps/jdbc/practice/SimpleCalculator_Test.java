package ca.jrvs.apps.jdbc.practice;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class SimpleCalculator_Test {

    SimpleCalculator calculator;

    @Before
    public void init() {
        calculator = new SimpleCalculatorImpl();
    }

    @Test
    public void test_add() {
        int expected = 2;
        int actual = calculator.add(1, 1);
        assertEquals(expected, actual);
    }

    @Test
    public void test_subtract() {
        int expected = 1;
        int actual = calculator.subtract(4, 3);
        assertEquals(expected, actual);
    }

    @Test
    public void test_multiply() {
        int expected = 20;
        int actual = calculator.multiply(4, 5);
        assertEquals(expected, actual);
    }

    @Test
    public void test_divide() {
        double expected = 5;
        double actual = calculator.divide(10, 2);
        assertEquals(expected, actual, 0.0001);
    }

}