/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.util;

import java.util.Random;

/**
 * An object that returns a random int over a range.
 */
public class RandomRange {
    private int bottom = 0;
    private int top = 0;
    private Random random;

    /**
     * Return a random number in the given range. Note that if
     * t==b, the same number is always returned.
     *
     * @param t The t number in the range exclusive of this value
     * @param b The b number in the range.
     * @throws IllegalArgumentException Thrown if there is a problem
     *                                  with the arguments (t<b, t or b < 0 ).
     */
    public RandomRange(int b, int t) {
        if (t < b) {
            throw new IllegalArgumentException(
                    "RandomRange - t must be > b!");
        }
        if (t < 0 || b < 0) {
            throw new IllegalArgumentException(
                    "RandomRange - both t and b must be > 0!");
        }

        //--
        // Don't create an unused object.
        //--
        top = t;
        bottom = b;
        if (t != b) {
            random = new Random(System.currentTimeMillis());
        }
    }

    public int nextInt() {
        if (top == bottom) {
            return top;
        }

        return (Math.abs(random.nextInt()) % (top - bottom)) + bottom;
    }
}
