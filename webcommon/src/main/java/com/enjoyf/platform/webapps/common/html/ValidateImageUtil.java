package com.enjoyf.platform.webapps.common.html;

import javax.servlet.http.HttpServletRequest;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  13-9-22 下午4:28
 * Description:
 */
public class ValidateImageUtil {

    public static  boolean checkImage(String imageCode, HttpServletRequest request) {
        if (request.getSession() == null) {
            return false;
        }

        if (request.getSession().getAttribute(ValidateImageGenerator.RANDOMCODEKEY) == null) {
            return false;
        }

        String imageCodeBySession = String.valueOf(request.getSession().getAttribute(ValidateImageGenerator.RANDOMCODEKEY));

        return imageCodeBySession.equalsIgnoreCase(imageCode);
    }
}
