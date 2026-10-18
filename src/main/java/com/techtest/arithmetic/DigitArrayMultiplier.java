package com.techtest.arithmetic;

final class DigitArrayMultiplier {

    private DigitArrayMultiplier() {
    }

    static int[] multiply(int[] left, int[] right) {
        int[] multiplicand = left.length >= right.length ? left : right;
        int[] multiplier = multiplicand == left ? right : left;
        int[] product = new int[multiplicand.length + multiplier.length];

        for (int shift = 0; shift < multiplier.length; shift++) {
            for (int repetition = 0; repetition < multiplier[shift]; repetition++) {
                addShifted(product, multiplicand, shift);
            }
        }
        return product;
    }

    private static void addShifted(int[] accumulator, int[] addend, int shift) {
        int carry = 0;
        for (int index = 0; index < addend.length || carry != 0; index++) {
            int position = shift + index;
            DigitSum sum = DigitSum.of(accumulator[position], index < addend.length ? addend[index] : 0, carry);
            accumulator[position] = sum.digit();
            carry = sum.carry();
        }
    }
}
