package com.enjoyf.platform.webapps.common.multimedia;

import com.enjoyf.platform.util.StringUtil;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * <p/>
 * Description:
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>liuhao</a>
 */
public class VideoProvider {
    private static Map<String, VideoProvider> map = new HashMap<String, VideoProvider>();

    public static final VideoProvider PROVIDER_YOUKU = new VideoProvider("youku");
    public static final VideoProvider PROVIDER_KU6 = new VideoProvider("ku6");
    public static final VideoProvider PROVIDER_TUDOU = new VideoProvider("tudou");
    public static final VideoProvider PROVIDER_56 = new VideoProvider("56");
    public static final VideoProvider PROVIDER_SINA = new VideoProvider("sina");
    public static final VideoProvider PROVIDER_SOHU = new VideoProvider("sohu");
    public static final VideoProvider PROVIDER_BILIBILI = new VideoProvider("bilibili");
    public static final VideoProvider PROVIDER_AIPAI = new VideoProvider("aipai");

    private String code;


    private VideoProvider(String c) {
        code = c.toLowerCase();

        map.put(code, this);
    }

    public String getCode() {
        return code;
    }

    @Override
    public int hashCode() {
        return code.hashCode();
    }

    @Override
    public String toString() {
        return "ImageSize: tag=" + this.code;
    }

    @Override
    public boolean equals(Object obj) {
        if ((obj == null) || !(obj instanceof VideoProvider)) {
            return false;
        }

        return code.equalsIgnoreCase(((VideoProvider) obj).getCode());
    }

    public static VideoProvider getByCode(String c) {
        if (StringUtil.isEmpty(c)) {
            return null;
        }

        return map.get(c.toLowerCase());
    }

    public static Collection<VideoProvider> getAll() {
        return map.values();
    }
}
