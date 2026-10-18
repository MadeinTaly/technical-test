package com.techtest.arithmetic;

record DigitSum(int digit, int carry) {

    private static final int BASE = 10;
    private static final int MAX_DIGIT = 9;

    static DigitSum of(int left, int right, int carry) {
        requireDigit(left);
        requireDigit(right);
        if (carry != 0 && carry != 1) {
            throw new IllegalArgumentException("carry must be 0 or 1: " + carry);
        }
        int sum = left + right + carry;
        return sum < BASE ? new DigitSum(sum, 0) : new DigitSum(sum - BASE, 1);
    }

    private static void requireDigit(int value) {
        if (value < 0 || value > MAX_DIGIT) {
            throw new IllegalArgumentException("not a single digit: " + value);
        }
    }
}
