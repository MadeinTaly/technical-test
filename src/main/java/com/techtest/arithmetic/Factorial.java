package com.techtest.arithmetic;

import java.util.stream.LongStream;

public final class Factorial {

    private Factorial() {
    }

    public static ArrayInteger of(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("Factorial is undefined for negative numbers: " + n);
        }
        return LongStream.rangeClosed(2, n)
                .mapToObj(ArrayInteger::valueOf)
                .reduce(ArrayInteger.ONE, ArrayInteger::multiply);
    }
}
