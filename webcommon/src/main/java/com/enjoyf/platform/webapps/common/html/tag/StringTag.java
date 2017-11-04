package com.enjoyf.platform.webapps.common.html.tag;

import com.enjoyf.platform.props.hotdeploy.HotdeployConfigFactory;
import com.enjoyf.platform.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * <p/>
 * Description:
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>liuhao</a>
 */
public class StringTag {
    private static Logger logger = LoggerFactory.getLogger(StringTag.class);

    public static String subStr(String s, int length, String suffix) {
        if (StringUtil.isEmpty(suffix)) {
            return StringUtil.length(s) > length ? StringUtil.subString(s, length) : s;
        } else {
            return StringUtil.length(s) > length ? StringUtil.subString(s, length - suffix.length()) + suffix : s;
        }
    }

    public static String subString(String s, int length) {
        return StringUtil.length(s) > length ? StringUtil.subString(s, length) : s;
    }


    public static String encodeUrl(String s, String encoding) {
        try {
            s = URLEncoder.encode(s, encoding);
        } catch (UnsupportedEncodingException e) {
            logger.error(" encodeUrl UnsupportedEncodingException ", e);
        }
        return s;
    }

    public static String decodeUrl(String s, String encoding) {
        try {
            s = URLDecoder.decode(s, encoding);
        } catch (UnsupportedEncodingException e) {
            logger.error(" decodeUrl UnsupportedEncodingException ", e);
        }
        return s;
    }

}
