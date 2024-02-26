package ca.jrvs.apps.jdbc.practice;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.Assert.assertEquals;

class SimpleCalculatorTest {

    SimpleCalculator calculator;

    @BeforeEach
    void init() {
        calculator = new SimpleCalculatorImpl();
    }

    @Test
    void test_add() {
        int expected = 2;
        int actual = calculator.add(1, 1);
        assertEquals(expected, actual);
    }

    @Test
    void test_subtract() {
        int expected = 1;
        int actual = calculator.subtract(4, 3);
        assertEquals(expected, actual);
    }

    @Test
    void test_multiply() {
        int expected = 20;
        int actual = calculator.multiply(4, 5);
        assertEquals(expected, actual);
    }

    @Test
    void test_divide() {
        double expected = 5;
        double actual = calculator.divide(10, 2);
        assertEquals(expected, actual, 0.0001);
    }

}