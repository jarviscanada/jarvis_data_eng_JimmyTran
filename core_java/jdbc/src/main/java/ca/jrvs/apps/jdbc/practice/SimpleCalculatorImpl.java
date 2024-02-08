package ca.jrvs.apps.jdbc.practice;

public class SimpleCalculatorImpl implements SimpleCalculator {

    @Override
    public int add(int x, int y) {
        return x + y;
    }

    @Override
    public int subtract(int x, int y) {
        // TODO Auto-generated method stub
        return x - y;
    }

    @Override
    public int multiply(int x, int y) {
        // TODO Auto-generated method stub
        return x * y;
    }

    @Override
    public double divide(int x, int y) {
        // TODO Auto-generated method stub
        return x / y;
    }

    // @Override
    // public int power(int x, int y) {
    // // TODO Auto-generated method stub
    // return Math.pow(x, y);
    // }

    // @Override
    // public double abs(double x) {
    // // TODO Auto-generated method stub
    // return Math.abs(x);
    // }

}