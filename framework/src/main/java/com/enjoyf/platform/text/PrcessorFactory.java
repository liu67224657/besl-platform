package com.enjoyf.platform.text;

import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  13-9-27 下午3:38
 * Description:
 */
public class PrcessorFactory {
    private static Map<Integer, JsonItemProcessStragy> map = new HashMap<Integer, JsonItemProcessStragy>();

    static {
        map.put(TextJsonItem.IMAGE_TYPE, new JsonItemImageProcessStragy());
        map.put(TextJsonItem.TEXT_TYPE, new JsonItemTextProcessStragy());
    }

    public static TextJsonItem precessByType(TextJsonItem textJsonItem) {
        JsonItemProcessStragy stragy = map.get(textJsonItem.getType());
        if (stragy == null) {
            return textJsonItem;
        }

        return stragy.processorText(textJsonItem);
    }
}
