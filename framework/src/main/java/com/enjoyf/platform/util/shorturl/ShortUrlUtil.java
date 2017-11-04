/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.util.shorturl;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-10-13 下午5:59
 * Description:
 */
public class ShortUrlUtil {

    //
    private static String[] KEY_SEQUENCE = {
            "a", "A", "b", "B", "0", "c", "C", "d", "D",
            "x", "X", "y", "Y", "z", "Z",
            "1", "e", "E", "f", "F", "2", "g", "G",
            "u", "U", "v", "V", "w", "W",
            "h", "H", "3", "i", "I", "j", "J", "4",
            "r", "R", "s", "S", "9", "t", "T",
            "k", "K", "l", "L", "6",
            "o", "O", "p", "P", "8", "q", "Q",
            "m", "M", "n", "N", "7"
    };

    private static int STEP = 10;

    //
    private static Map<Integer, Integer> SWITCH_RULE = new HashMap<Integer, Integer>();

    static {
        //
        STEP = KEY_SEQUENCE.length;

        //
        SWITCH_RULE.put(0, 3);
        SWITCH_RULE.put(1, 0);
        SWITCH_RULE.put(2, 1);
        SWITCH_RULE.put(3, 2);
    }

    public static String generateShortUrl(long seq, int len) {
        StringBuffer returnValue = new StringBuffer();

        //split
        List<Integer> stepValues = getStepValues(seq);

        if (len > stepValues.size()) {
            for (int i = stepValues.size(); i < len; i++) {
                stepValues.add(0);
            }
        }

        //switch
        List<Integer> switchStepValues = new ArrayList<Integer>();

        switchStepValues.addAll(stepValues);

        for (int i = 0; i < stepValues.size(); i++) {
            int switchIdex = i;
            if (SWITCH_RULE.containsKey(i)) {
                switchIdex = SWITCH_RULE.get(i);
            }

            switchStepValues.set(switchIdex, stepValues.get(i));
        }

        //fill full value.
        for (int i = switchStepValues.size(); i > 0; i--) {
            returnValue.append(KEY_SEQUENCE[switchStepValues.get(i - 1)]);
        }

        return returnValue.toString();
    }

    private static List<Integer> getStepValues(long seq) {
        List<Integer> returnValue = new ArrayList<Integer>();

        //
        long left = seq;
        int v = 0;

        do {
            v = (int) (left % STEP);
            left = left / STEP;

            returnValue.add(v);
        } while (left > 0);

        return returnValue;
    }

    public static void main(String[] args) {
        long t = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            System.out.println(generateShortUrl(i, 5));
        }

        System.out.println(System.currentTimeMillis() - t);
    }
}
