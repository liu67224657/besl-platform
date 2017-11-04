/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.serv.event;

import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.reflect.ReflectUtil;
import com.google.common.base.Strings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-11-26 下午4:22
 * Description:
 */
public class PageViewConfigureTest {
    //the location id and it's regex map, used to get the location id and the refer id.
    //the map will reload from db fix
    private Map<String, Pattern> locationIdRegexMap = new HashMap<String, Pattern>();


    //
    public PageViewConfigureTest() {
        reload();
    }

    public void reload() {
        //get all the location from db.
        List<String> regexes = new ArrayList<String>();

        regexes.add("^/registerpage");
        regexes.add("^/profile/favorite");
        regexes.add("^/home/");
        regexes.add("^/security/pwd/forgot");
        regexes.add("^/home|^/$");
        for (int i = 0; i < 1000; i++) {
            regexes.add(i + "regiext");
        }

        //set to cache.
        if (!CollectionUtil.isEmpty(regexes)) {
            int i = 0;

            for (String regex : regexes) {
                if (!Strings.isNullOrEmpty(regex)) {
                    //put it into map.
                    locationIdRegexMap.put(String.valueOf(i), Pattern.compile(regex));
                }
                i++;
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

    public static void main(String[] args) {
        PageViewConfigureTest c = new PageViewConfigureTest();
        long t = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            c.getLocationIdByUrl(args[0]);
        }
        System.out.println(">>>>>> " + (System.currentTimeMillis() - t));
    }
}
