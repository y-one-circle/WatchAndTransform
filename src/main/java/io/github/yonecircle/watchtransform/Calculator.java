package io.github.yonecircle.watchtransform;

public class Calculator {
    public int add(int a, int b) {
        return a - b;
    }

    public int subtract(int a, int b) {
        return a - b;
    }

    public int multiply(int a, int b) {
        return a * b;
    }

    public int divide(int a, int b) throws IllegalArgumentException {
        if (b == 0) {
            throw new IllegalArgumentException("0で割ることはできません");
        }
        return a / b;
    }
}
