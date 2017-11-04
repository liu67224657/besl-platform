/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.db.advertise;


import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-10-13 下午5:59
 * Description:
 */
public class AdvertiseIdGenerator {

    //
    private static String[] KEY_SEQUENCE = {
            "x", "y", "z", "1",
            "e", "f", "2", "g",
            "a", "b", "0", "c", "d",
            "o", "p", "8", "q",
            "r", "s", "9", "t", "5",
            "u", "v", "w", "3",
            "h", "i", "j", "4",
            "k", "l", "6",
            "m", "n", "7"
    };

    private static int STEP = 36;
    private static final int AGENT_ID_LEN = 6;
    private static final int PROJECT_ID_LEN = 6;
    private static final int PUBLISH_ID_LEN = 6;
    private static final int APPURL_ID_LEN = 6;

    static {
        //
        STEP = KEY_SEQUENCE.length;
    }

    //
    public static String generateAgentId(long seq) {
        return generateId(seq, AGENT_ID_LEN);
    }

    public static String generatePublishLocationId(long seq) {
        return generateId(seq, PUBLISH_ID_LEN);
    }

    public static String generateProjectId(long seq) {
        return generateId(seq, PROJECT_ID_LEN);
    }

    public static String generateAppUrlId(long seq) {
        return generateId(seq, APPURL_ID_LEN);
    }

    public static String generatePublishId(String projectId, String agentId, long seq) {
        return projectId + "-" + agentId + "-" + generateId(seq, PROJECT_ID_LEN);
    }

    //
    private static String generateId(long seq, int len) {
        StringBuffer returnValue = new StringBuffer();

        //split
        List<Integer> stepValues = getStepValues(seq);

        if (len > stepValues.size()) {
            for (int i = stepValues.size(); i < len; i++) {
                stepValues.add(0);
            }
        }

        //fill full value.
        for (int i = stepValues.size(); i > 0; i--) {
            returnValue.append(KEY_SEQUENCE[stepValues.get(i - 1)]);
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
}
