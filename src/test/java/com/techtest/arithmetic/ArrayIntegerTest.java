package com.techtest.arithmetic;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigInteger;
import java.util.Random;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ArrayIntegerTest {

    @Test
    void multipliesExampleFromSpecification() {
        ArrayInteger product = ArrayInteger.of("15").multiply(ArrayInteger.of("2"));

        assertArrayEquals(new int[] {0, 3}, product.digits());
        assertEquals("30", product.toString());
    }

    @ParameterizedTest
    @CsvSource({
            "0, 0",
            "0, 987654321",
            "1, 1",
            "9, 9",
            "99, 99",
            "999, 1",
            "12345, 6789",
            "-15, 2",
            "15, -2",
            "-15, -2",
            "-7, 0",
            "18446744073709551615, 18446744073709551615",
            "31415926535897932384626433832795028841971, 27182818284590452353602874713527"
    })
    void matchesBigInteger(String left, String right) {
        assertProductMatchesBigInteger(left, right);
    }

    @Test
    void matchesBigIntegerOnRandomOperands() {
        Random random = new Random(42);
        IntStream.range(0, 500).forEach(round -> assertProductMatchesBigInteger(
                randomInteger(random), randomInteger(random)));
    }

    @Test
    void isCommutative() {
        ArrayInteger left = ArrayInteger.of("123456789");
        ArrayInteger right = ArrayInteger.of("-98765");

        assertEquals(left.multiply(right), right.multiply(left));
    }

    @Test
    void normalizesRepresentation() {
        assertAll(
                () -> assertEquals(ArrayInteger.ZERO, ArrayInteger.of("-000")),
                () -> assertFalse(ArrayInteger.of("-0").isNegative()),
                () -> assertEquals("42", ArrayInteger.of("+00042").toString()),
                () -> assertArrayEquals(new int[] {2, 4}, ArrayInteger.of("0042").digits()),
                () -> assertEquals(ArrayInteger.of("42").hashCode(), ArrayInteger.of("042").hashCode()));
    }

    @Test
    void exposesDefensiveCopyOfDigits() {
        ArrayInteger number = ArrayInteger.of("15");

        number.digits()[0] = 9;

        assertEquals("15", number.toString());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "-", "+", "12a", "1.5", " 1", "--1", "+-1"})
    void rejectsNonIntegers(String value) {
        assertThrows(NumberFormatException.class, () -> ArrayInteger.of(value));
    }

    @Test
    void digitSumRejectsOperandsOutsideSingleDigitRange() {
        assertThrows(IllegalArgumentException.class, () -> DigitSum.of(10, 0, 0));
        assertThrows(IllegalArgumentException.class, () -> DigitSum.of(0, -1, 0));
        assertThrows(IllegalArgumentException.class, () -> DigitSum.of(0, 0, 2));
        assertEquals(new DigitSum(9, 1), DigitSum.of(9, 9, 1));
    }

    private static void assertProductMatchesBigInteger(String left, String right) {
        String expected = new BigInteger(left).multiply(new BigInteger(right)).toString();

        assertEquals(expected, ArrayInteger.of(left).multiply(ArrayInteger.of(right)).toString(),
                () -> left + " x " + right);
    }

    private static String randomInteger(Random random) {
        BigInteger magnitude = new BigInteger(random.nextInt(1, 256), random);
        return (random.nextBoolean() ? magnitude : magnitude.negate()).toString();
    }
}
