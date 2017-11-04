package com.enjoyf.platform.webapps.common.text;

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
public class ContentFormatType {
    private static Map<String, ContentFormatType> map = new HashMap<String, ContentFormatType>();

    //不需要format
    public static final ContentFormatType FORMAT_NONE = new ContentFormatType("none");
    //预览形式
    public static final ContentFormatType FORMAT_PREVIEW = new ContentFormatType("priview");
    //完全显示
    public static final ContentFormatType FORMAT_TEMPLATE = new ContentFormatType("text");

    public static final ContentFormatType FORMAT_RSS_NONE = new ContentFormatType("rssnone");

    //博客文章页
    public static final ContentFormatType FORMAT_BLOG_TEMPLATE = new ContentFormatType("blog");

    //the forward type

    private String code;

    public ContentFormatType(String c) {
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
        if ((obj == null) || !(obj instanceof ContentFormatType)) {
            return false;
        }

        return code.equalsIgnoreCase(((ContentFormatType) obj).getCode());
    }

    public static ContentFormatType getByCode(String c) {
        if (StringUtil.isEmpty(c)) {
            return null;
        }

        return map.get(c.toLowerCase());
    }

    public static Collection<ContentFormatType> getAll() {
        return map.values();
    }
}
