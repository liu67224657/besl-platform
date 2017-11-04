package com.enjoyf.platform.text;

import com.enjoyf.platform.util.StringUtil;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * <p/>
 * Description:图片的尺寸
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>liuhao</a>
 */
public class ImageSize {
    private static Map<String, ImageSize> map = new HashMap<String, ImageSize>();

    public static final ImageSize IMAGE_SIZE_M = new ImageSize("_M");

    public static final ImageSize IMAGE_SIZE_S = new ImageSize("_S");
    public static final ImageSize IMAGE_SIZE_SS = new ImageSize("_SS");
    //the forward type

    private String code;

    public ImageSize(String c) {
        code = c;

        map.put(c.toLowerCase(), this);
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
        if ((obj == null) || !(obj instanceof ImageSize)) {
            return false;
        }

        return code.equalsIgnoreCase(((ImageSize) obj).getCode());
    }

    public static ImageSize getByCode(String c) {
        if (StringUtil.isEmpty(c)) {
            return null;
        }

        return map.get(c.toLowerCase());
    }

    public static Collection<ImageSize> getAll() {
        return map.values();
    }
}
