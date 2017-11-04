/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.util.profile;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-10-13 下午5:59
 * Description:
 */
public class ProfileDomainGenerator {

    public static final int KEY_DOMAIN_LEN = 6;

    //
    private static String[] KEY_SEQUENCE = {
            "a", "b", "0", "c", "d",
            "x", "y", "z",
            "1", "e", "f", "2", "g",
            "u", "v", "w",
            "h", "3", "i", "j", "4",
            "r", "s", "9", "t",
            "k", "l", "6",
            "o", "p", "8", "q",
            "m", "n", "7"
    };

    private static int STEP = 10;

    //
    private static Map<Integer, Integer> SWITCH_RULE = new HashMap<Integer, Integer>();

    static {
        //
        STEP = KEY_SEQUENCE.length;

        //
        SWITCH_RULE.put(0, 4);
        SWITCH_RULE.put(1, 0);
        SWITCH_RULE.put(2, 1);
        SWITCH_RULE.put(3, 2);
        SWITCH_RULE.put(4, 3);
    }

    public static String generateProfileDomain(long seq, int len) {
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
//        for (int i = 0; i < 10000; i++) {
//            System.out.println(generateProfileDomain(5, 7));
//        }

        System.out.println(t);
    }
}
