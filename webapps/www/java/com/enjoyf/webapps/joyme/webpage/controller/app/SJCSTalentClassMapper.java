package com.enjoyf.webapps.joyme.webpage.controller.app;

import java.util.HashMap;
import java.util.Map;

/**
 * <p/>
 * Description:
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>ericliu</a>
 */
public class SJCSTalentClassMapper {
    private static Map<String, String> map = new HashMap<String, String>();

    static {
        map.put("zs", "g1");
        map.put("qs", "g1");
        map.put("sqs", "g1");

        map.put("dz", "g2");
        map.put("wdj", "g2");
        map.put("wz", "g2");
        map.put("ws", "g2");

        map.put("lr", "g3");
        map.put("gcs", "g3");
        map.put("yx", "g3");

        map.put("sg", "g4");
        map.put("yysr", "g4");
        map.put("wy", "g4");

        map.put("mss", "g5");
        map.put("hss", "g5");
        map.put("slfs", "g5");
    }

    public static String getGroupByTalentClass(String talentClass){
        return map.get(talentClass);
    }
}
