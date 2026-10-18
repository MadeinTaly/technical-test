package com.techtest.arithmetic;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigInteger;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FactorialTest {

    private static final String FACTORIAL_OF_100 =
            "93326215443944152681699238856266700490715968264381621468592963895217599993229915608941463976156518286253697920827223758251185210916864000000000000000000000000";

    @Test
    void computesFactorialOfOneHundred() {
        ArrayInteger factorial = Factorial.of(100);

        assertEquals(FACTORIAL_OF_100, factorial.toString());
        assertEquals(158, factorial.digits().length);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 5, 10, 20, 21, 50, 150})
    void matchesBigInteger(int n) {
        BigInteger expected = IntStream.rangeClosed(2, n)
                .mapToObj(BigInteger::valueOf)
                .reduce(BigInteger.ONE, BigInteger::multiply);

        assertEquals(expected.toString(), Factorial.of(n).toString());
    }

    @Test
    void rejectsNegativeInput() {
        assertThrows(IllegalArgumentException.class, () -> Factorial.of(-1));
    }
}
