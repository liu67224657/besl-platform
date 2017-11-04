package com.enjoyf.webapps.joyme.webpage.controller.event;

import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  13-11-4 上午10:51
 * Description:
 */
class EventPageFactory {
    private static Map<String, EventWebModelBuilder> map = new HashMap<String, EventWebModelBuilder>();

    static {
        map.put("shuang11", new Shuang11EventBuilder());
    }

    public static EventWebModelBuilder getByCode(String code) {
        return map.get(code);
    }

}
