package com.techtest.arithmetic;

import java.util.Arrays;
import java.util.Objects;

public final class ArrayInteger {

    public static final ArrayInteger ZERO = new ArrayInteger(false, new int[] {0});
    public static final ArrayInteger ONE = new ArrayInteger(false, new int[] {1});

    private final boolean negative;
    private final int[] digits;

    private ArrayInteger(boolean negative, int[] digits) {
        this.digits = stripLeadingZeros(digits);
        this.negative = negative && !isZero(this.digits);
    }

    public static ArrayInteger of(String value) {
        Objects.requireNonNull(value, "value");
        boolean negative = value.startsWith("-");
        String magnitude = negative || value.startsWith("+") ? value.substring(1) : value;
        if (magnitude.isEmpty()) {
            throw new NumberFormatException("Not an integer: '%s'".formatted(value));
        }

        int[] digits = new int[magnitude.length()];
        for (int index = 0; index < digits.length; index++) {
            char symbol = magnitude.charAt(magnitude.length() - 1 - index);
            if (symbol < '0' || symbol > '9') {
                throw new NumberFormatException("Not an integer: '%s'".formatted(value));
            }
            digits[index] = symbol - '0';
        }
        return new ArrayInteger(negative, digits);
    }

    public static ArrayInteger valueOf(long value) {
        return of(Long.toString(value));
    }

    public ArrayInteger multiply(ArrayInteger other) {
        return new ArrayInteger(negative != other.negative, DigitArrayMultiplier.multiply(digits, other.digits));
    }

    public int[] digits() {
        return digits.clone();
    }

    public boolean isNegative() {
        return negative;
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof ArrayInteger that && negative == that.negative && Arrays.equals(digits, that.digits);
    }

    @Override
    public int hashCode() {
        return Objects.hash(negative, Arrays.hashCode(digits));
    }

    @Override
    public String toString() {
        StringBuilder text = new StringBuilder(digits.length + 1);
        if (negative) {
            text.append('-');
        }
        for (int index = digits.length - 1; index >= 0; index--) {
            text.append((char) ('0' + digits[index]));
        }
        return text.toString();
    }

    private static int[] stripLeadingZeros(int[] digits) {
        int length = digits.length;
        while (length > 1 && digits[length - 1] == 0) {
            length--;
        }
        return Arrays.copyOf(digits, length);
    }

    private static boolean isZero(int[] digits) {
        return digits.length == 1 && digits[0] == 0;
    }
}
