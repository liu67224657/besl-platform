package com.enjoyf.platform.webapps.common.html.tag;

import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.webapps.common.util.WebUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p/>
 * Description: 用户头像标签类
 * </p>
 *
 * @author: zx
 */
public class IconTag {

    private static Logger logger = LoggerFactory.getLogger(IconTag.class);
    //    private static String defaultURL = "" + WebUtil.getUrlLib() + "/static/theme/default/img/default.jpg";
    private static String boyURL = "" + WebUtil.getUrlLib() + "/static/theme/default/img/head_boy_";
    private static String girlURL = "" + WebUtil.getUrlLib() + "/static/theme/default/img/head_girl_";
    private static String defaultURL = "" + WebUtil.getUrlLib() + "/static/theme/default/img/head_is_";

    /**
     * 大头像
     */
    public static final String HEAD_ICON_SIZE_B = "b";
    /**
     * 中头像
     */
    public static final String HEAD_ICON_SIZE_M = "m";
    /**
     * 小头像
     */
    public static final String HEAD_ICON_SIZE_S = "s";
    /**
     * 超小头像
     */
    public static final String HEAD_ICON_SIZE_SS = "ss";


    /**
     * 取大头像 相对路径 /r00x/2011/07/xx/xxxxxxxxxx.xxx
     *
     * @param furl 路径
     * @return String
     */
    public static String praseIcon(String furl, String sex, String size) {
        return ImageURLTag.parseUserCenterHeadIcon(furl, sex, "m", true);
//        String sReURL = defaultURL + HEAD_ICON_SIZE_M + ".jpg";
//
//        if (StringUtil.isEmpty(furl)) {
//            if (StringUtil.isEmpty(sex)) {
//                return getUserHeadUrl(defaultURL, size);
//            } else if ("0".equals(sex) || sex == "0") {
//                return getUserHeadUrl(girlURL, size);
//            } else {
//                return getUserHeadUrl(boyURL, size);
//            }
//        }
//
//        try {
//            if (furl != null && !"".equals(furl)) {
//                String exname = furl.substring(furl.lastIndexOf(".")); //.jpg
//                String sNametou = furl.substring(0, furl.lastIndexOf(".")); //   /image/2011/07/12/12C14AB9D1DCC4FF4989D5930E45317A_M
//                if (sNametou.endsWith("_S") || sNametou.endsWith("_M") || sNametou.endsWith("_SS")) {
//                    if (sNametou.lastIndexOf("_") != -1)
//                        sNametou = sNametou.substring(0, sNametou.lastIndexOf("_"));//   /image/2011/07/12/12C14AB9D1DCC4FF4989D5930E45317A
//                }
//                sReURL = sNametou + (StringUtil.isEmpty(size)?"":("_"+size)) + exname;
//            }
//        } catch (Exception e) {
//            logger.error(e.getMessage(), e);
//        }
//        return sReURL;
    }


    /**
     * 根据用户所传参数获得指定大小头像
     *
     * @param url          图片地址
     * @param headIconSize 头像大小
     * @return
     */
    private static String getUserHeadUrl(String url, String headIconSize) {

        if (HEAD_ICON_SIZE_SS.equals(headIconSize) || HEAD_ICON_SIZE_SS == headIconSize) {
            return url + HEAD_ICON_SIZE_SS + ".jpg";
        } else if (HEAD_ICON_SIZE_S.equals(headIconSize) || HEAD_ICON_SIZE_S == headIconSize) {
            return url + HEAD_ICON_SIZE_S + ".jpg";
        } else if (HEAD_ICON_SIZE_M.equals(headIconSize) || HEAD_ICON_SIZE_M == headIconSize) {
            return url + HEAD_ICON_SIZE_M + ".jpg";
        } else if (HEAD_ICON_SIZE_B.equals(headIconSize) || HEAD_ICON_SIZE_B == headIconSize) {
            return url + HEAD_ICON_SIZE_B + ".jpg";
        } else {
            return url + HEAD_ICON_SIZE_M + ".jpg";
        }
    }

}
