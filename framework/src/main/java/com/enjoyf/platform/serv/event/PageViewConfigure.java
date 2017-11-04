/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.serv.event;

import com.enjoyf.platform.db.event.EventHandler;
import com.enjoyf.platform.service.event.pageview.PageViewLocation;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.HTTPUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.reflect.ReflectUtil;
import com.google.common.base.Strings;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-11-26 下午4:22
 * Description:
 */
public class PageViewConfigure {
    //the location id and it's regex map, used to get the location id and the refer id.
    //the map will reload from db fix
    private Map<String, Pattern> locationIdRegexMap = new HashMap<String, Pattern>();

    private EventHandler handler;

    //
    public PageViewConfigure(EventHandler handler) {
        this.handler = handler;

        reload();
    }

    public void reload() {
        //get all the location from db.
        List<PageViewLocation> locations = null;
        try {
            locations = handler.queryAllPageViewLocations();
        } catch (Exception e) {
            //
            GAlerter.lab("PageViewConfigure call the event handler to queryAllPageViewLocations error.", e);
        }

        //set to cache.
        if (!CollectionUtil.isEmpty(locations)) {
            for (PageViewLocation location : locations) {
                if (!Strings.isNullOrEmpty(location.getLocationUrlRegex())) {
                    //put it into map.
                    locationIdRegexMap.put(location.getLocationId(), Pattern.compile(location.getLocationUrlRegex()));
                }
            }
        }
    }

    public String getLocationIdByUrl(String url) {
        //
        String returnValue = null;

        //
        if (Strings.isNullOrEmpty(url)) {
            return returnValue;
        }

        //remove the hostname.
        url = HTTPUtil.getURIQueryString(url);

        //
        for (Map.Entry<String, Pattern> entry : locationIdRegexMap.entrySet()) {
            if (entry.getValue().matcher(url).find()) {
                returnValue = entry.getKey();

                break;
            }
        }

        return returnValue;
    }

    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
