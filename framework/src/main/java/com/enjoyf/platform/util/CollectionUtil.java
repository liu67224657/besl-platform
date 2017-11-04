package com.enjoyf.platform.util;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * @author <a href=mailto:yinpy@platform.com>Yin Pengyi</a>
 */
public class CollectionUtil {


    public static <T> boolean isEmpty(Collection<T> c) {
        return c == null || c.isEmpty();
    }

    public static <T> boolean isEmpty(T[] t) {
        return t == null || t.length == 0;
    }

    public static <K, V> boolean isEmpty(Map<K, V> m) {
        return m == null || m.isEmpty();
    }

    public static Map<String, String> putIfValueNotNull(Map<String, String> m, String key, String value) {
        if (!StringUtil.isEmpty(value)) {
            m.put(key, value);
        }
        return m;
    }

    public static String join(Collection<String> c, String split) {
        StringBuffer strBuf = new StringBuffer();

        if (c != null) {
            int i = 1;
            int size = c.size();

            for (String s : c) {
                if (i < size) {
                    strBuf.append(s).append(split);
                } else {
                    strBuf.append(s);
                }

                i++;
            }
        }

        return strBuf.toString();
    }

    public static <T> T getRandomObj(List<T> list) {
        if (isEmpty(list)) {
            return null;
        }
        if (list.size() == 1) {
            return list.get(0);
        }

        return list.get(Math.abs(new Random().nextInt()) % (list.size()));
    }
}
