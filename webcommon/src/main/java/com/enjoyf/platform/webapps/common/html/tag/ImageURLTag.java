package com.enjoyf.platform.webapps.common.html.tag;

import com.enjoyf.platform.service.naming.ResourceServerMonitor;
import com.enjoyf.platform.service.profile.Profile;
import com.enjoyf.platform.service.profile.ProfileBlogHeadIcon;
import com.enjoyf.platform.service.profile.ProfileBlogHeadIconSet;
import com.enjoyf.platform.text.ImageResolveUtil;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.webapps.common.util.WebUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * <p/>
 * Description: 用户头像标签类
 * </p>
 *
 * @author: zx
 */
public class ImageURLTag {

    private static Logger logger = LoggerFactory.getLogger(ImageURLTag.class);
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
     * 得到用户指定大小的头像
     *
     * @param profileBlogHeadIconSet 用户头像对象
     * @param headIconSize           返回头像的大小(ss,s,m,b)
     * @return string http://xxx.xxx.xxx
     */
    public static String parseFace(ProfileBlogHeadIconSet profileBlogHeadIconSet, String headIconSize) {

        if (profileBlogHeadIconSet == null || CollectionUtil.isEmpty(profileBlogHeadIconSet.getIconSet())) {
            logger.info(" profileBlogHeadIconSet is null or profileBlogHeadIconSet.getIconSet() is null .");
            return null;
        }
        String reValue = null;

        Set<ProfileBlogHeadIcon> headIconSet = profileBlogHeadIconSet.getIconSet();

        if (headIconSet.iterator().hasNext()) {
            ProfileBlogHeadIcon icon = (ProfileBlogHeadIcon) headIconSet.iterator().next();
            if (StringUtil.isEmpty(headIconSize)) {
                reValue = parseMFace(icon.getHeadIcon());
            } else if (HEAD_ICON_SIZE_M.equals(headIconSize)) {
                reValue = parseMFace(icon.getHeadIcon());
            } else if (HEAD_ICON_SIZE_S.equals(headIconSize)) {
                reValue = parseSFace(icon.getHeadIcon());
            } else if (HEAD_ICON_SIZE_SS.equals(headIconSize)) {
                reValue = parseSSFace(icon.getHeadIcon());
            }else if(HEAD_ICON_SIZE_B.equals(headIconSize)){
                reValue = parseBFace(icon.getHeadIcon());
            }
        }

        return reValue;
    }

    /**
     * 得到用户多个头像
     *
     * @param profileBlogHeadIconSet 用户头像对象
     * @param headIconSize           返回头像的大小(ss,s,m,b)
     * @return string http://xxx.xxx.xxx
     */
    public static List parseFaces(ProfileBlogHeadIconSet profileBlogHeadIconSet, String headIconSize) {

        if (profileBlogHeadIconSet == null || CollectionUtil.isEmpty(profileBlogHeadIconSet.getIconSet())) {
            logger.info(" profileBlogHeadIconSet is null or profileBlogHeadIconSet.getIconSet() is null .");
            return null;
        }
        List reValue = new ArrayList();

        Set<ProfileBlogHeadIcon> headIconSet = profileBlogHeadIconSet.getIconSet();

        for (ProfileBlogHeadIcon icon : headIconSet) {
            if (StringUtil.isEmpty(headIconSize)) {
                reValue.add(parseMFace(icon.getHeadIcon()));

            } else if (HEAD_ICON_SIZE_M.equals(headIconSize)) {
                reValue.add(parseMFace(icon.getHeadIcon()));

            } else if (HEAD_ICON_SIZE_S.equals(headIconSize)) {
                reValue.add(parseSFace(icon.getHeadIcon()));

            } else if (HEAD_ICON_SIZE_SS.equals(headIconSize)) {
                reValue.add(parseSSFace(icon.getHeadIcon()));
            }else if(HEAD_ICON_SIZE_B.equals(headIconSize)){
                reValue.add(parseBFace(icon.getHeadIcon()));
            }
        }

        return reValue;
    }


    /**
     * 得到用户多个头像。
     *
     * @param profileBlogHeadIconSet 多头像对象集合的对象
     * @param headIconSize           以headIconSize大小返回头像（可选:ss, s, m, b）
     * @param validStatus            当validStatus为true时表示有效头像 ;validStatus=false时为有效和非法头像
     * @param beginIndex             从数组下标为index的头像开始
     * @param size                   截取num个
     * @return
     */
    public static List<String> parseFacesInclude(ProfileBlogHeadIconSet profileBlogHeadIconSet, String sex, String headIconSize, boolean validStatus, int beginIndex, int size) {

        List<String> reValue = new ArrayList<String>();
        List<String> tempList = new ArrayList<String>();
        if (profileBlogHeadIconSet == null || CollectionUtil.isEmpty(profileBlogHeadIconSet.getIconSet())) {
            if (logger.isDebugEnabled()) {
                logger.debug(" profileBlogHeadIconSet is null or profileBlogHeadIconSet.getIconSet() is null .");
            }

            if (StringUtil.isEmpty(sex)) {
                reValue.add(getUserHeadUrl(defaultURL, headIconSize));
            } else if ("0".equals(sex) || sex == "0") {
                reValue.add(getUserHeadUrl(girlURL, headIconSize));
            } else {
                reValue.add(getUserHeadUrl(boyURL, headIconSize));
            }
            return reValue;
        }

        for (ProfileBlogHeadIcon icon : profileBlogHeadIconSet.getIconSet()) {
            if (validStatus && icon.getValidStatus()) {
                if (StringUtil.isEmpty(headIconSize)) {
                    tempList.add(parseMFace(icon.getHeadIcon()));

                } else if (HEAD_ICON_SIZE_M.equals(headIconSize)) {
                    tempList.add(parseMFace(icon.getHeadIcon()));

                } else if (HEAD_ICON_SIZE_S.equals(headIconSize)) {
                    tempList.add(parseSFace(icon.getHeadIcon()));

                } else if (HEAD_ICON_SIZE_SS.equals(headIconSize)) {
                    tempList.add(parseSSFace(icon.getHeadIcon()));
                } else if (HEAD_ICON_SIZE_B.equals(headIconSize)) {
                    tempList.add(parseBFace(icon.getHeadIcon()));
                }
            }
        }

        if (!CollectionUtil.isEmpty(tempList)) {
            beginIndex = beginIndex > tempList.size() - 1 ? tempList.size() - 1 : beginIndex;
            size = size > tempList.size() - beginIndex ? tempList.size() - beginIndex : size;

            for (int i = beginIndex; i < (beginIndex + size); i++) {
                reValue.add(tempList.get(i));
            }
        } else {
            reValue.add(defaultURL + HEAD_ICON_SIZE_M + ".jpg");
        }

        return reValue;
    }


    /**
     * 得到用户多个头像。
     *
     * @param profileBlogHeadIconSet 多头像对象集合的对象
     * @param headIconSize           以headIconSize大小返回头像（可选:ss, s, m, b）
     * @param validStatus            当validStatus为true时表示有效头像 ;validStatus=false时为有效和非法头像
     * @return
     */
    public static String parseFirstFacesInclude(ProfileBlogHeadIconSet profileBlogHeadIconSet, String sex, String headIconSize, boolean validStatus) {

        String reValue = null;
        if (profileBlogHeadIconSet == null || CollectionUtil.isEmpty(profileBlogHeadIconSet.getIconSet())) {
            if (logger.isDebugEnabled()) {
                logger.debug(" profileBlogHeadIconSet is null or profileBlogHeadIconSet.getIconSet() is null .");
            }

            if (StringUtil.isEmpty(sex)) {
                return getUserHeadUrl(defaultURL, headIconSize);
            } else if ("0".equals(sex)) {
                return getUserHeadUrl(girlURL, headIconSize);
            } else {
                return getUserHeadUrl(boyURL, headIconSize);
            }
        }

        for (ProfileBlogHeadIcon icon : profileBlogHeadIconSet.getIconSet()) {
            if (validStatus && icon.getValidStatus()) {
                if (StringUtil.isEmpty(headIconSize)) {
                    return parseMFace(icon.getHeadIcon());
                } else if (HEAD_ICON_SIZE_M.equals(headIconSize)) {
                    return parseMFace(icon.getHeadIcon());
                } else if (HEAD_ICON_SIZE_S.equals(headIconSize)) {
                    return parseSFace(icon.getHeadIcon());
                } else if (HEAD_ICON_SIZE_SS.equals(headIconSize)) {
                    return parseSSFace(icon.getHeadIcon());
                } else if (HEAD_ICON_SIZE_B.equals(headIconSize)) {
                    return parseBFace(icon.getHeadIcon());
                }
            }
        }

        return reValue;
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

    /**
     * 得到用户多个头像 除第一个以外
     *
     * @param profileBlogHeadIconSet 用户头像对象
     * @param headIconSize           返回头像的大小(ss,s,m,b)
     * @return string http://xxx.xxx.xxx
     */
    public static List parseFacesExOne(ProfileBlogHeadIconSet profileBlogHeadIconSet, String headIconSize) {

        if (profileBlogHeadIconSet == null || CollectionUtil.isEmpty(profileBlogHeadIconSet.getIconSet())) {
            logger.info(" profileBlogHeadIconSet is null or profileBlogHeadIconSet.getIconSet() is null .");
            return null;
        }
        if (profileBlogHeadIconSet.getIconSet().size() < 2) {
            return null;
        }

        List reValue = new ArrayList();

        Object[] icons = profileBlogHeadIconSet.getIconSet().toArray();

        boolean signValidStatus = false;
        // 从第二个开始拿数据
        for (int i = 0; i < icons.length; i++) {
            ProfileBlogHeadIcon icon = (ProfileBlogHeadIcon) icons[i];

            if (icon.getValidStatus() == true && signValidStatus == false) {
                signValidStatus = true;
                continue;
            }

            if (icon.getValidStatus() == false) {
                continue;
            }

            if (StringUtil.isEmpty(headIconSize)) {
                reValue.add(parseMFace(icon.getHeadIcon()));

            } else if (HEAD_ICON_SIZE_M.equals(headIconSize)) {
                reValue.add(parseMFace(icon.getHeadIcon()));

            } else if (HEAD_ICON_SIZE_S.equals(headIconSize)) {
                reValue.add(parseSFace(icon.getHeadIcon()));

            } else if (HEAD_ICON_SIZE_SS.equals(headIconSize)) {
                reValue.add(parseSSFace(icon.getHeadIcon()));
            }else if (HEAD_ICON_SIZE_B.equals(headIconSize)) {
                reValue.add(parseBFace(icon.getHeadIcon()));
            }
        }

        return reValue;
    }


    /**
     * 得到用户指定大小的头像
     *
     * @param profileBlogHeadIconSet 用户头像对象
     * @param headIconSize           返回头像的大小(ss,s,m,b)
     * @param validStatus            == true/false
     * @return string http://xxx.xxx.xxx
     */
    public static String parseFace(ProfileBlogHeadIconSet profileBlogHeadIconSet, String headIconSize, boolean validStatus) {

        if (profileBlogHeadIconSet == null || CollectionUtil.isEmpty(profileBlogHeadIconSet.getIconSet())) {
            logger.info(" profileBlogHeadIconSet is null or profileBlogHeadIconSet.getIconSet() is null .");
            return null;
        }
        String reValue = null;

        Set<ProfileBlogHeadIcon> headIconSet = profileBlogHeadIconSet.getIconSet();

        for (ProfileBlogHeadIcon icon : headIconSet) {

            if (icon.getValidStatus() != validStatus) {
                continue;
            }

            if (StringUtil.isEmpty(headIconSize)) {
                reValue = parseMFace(icon.getHeadIcon());
                break;
            } else if (HEAD_ICON_SIZE_M.equals(headIconSize)) {
                reValue = parseMFace(icon.getHeadIcon());
                break;
            } else if (HEAD_ICON_SIZE_S.equals(headIconSize)) {
                reValue = parseSFace(icon.getHeadIcon());
                break;
            } else if (HEAD_ICON_SIZE_SS.equals(headIconSize)) {
                reValue = parseSSFace(icon.getHeadIcon());
                break;
            }else if (HEAD_ICON_SIZE_B.equals(headIconSize)) {
                reValue = parseBFace(icon.getHeadIcon());
                break;
            }
        }

        return reValue;
    }

    /**
     * 得到用户多个头像
     *
     * @param profileBlogHeadIconSet 用户头像对象
     * @param headIconSize           返回头像的大小(ss,s,m,b)
     * @param validStatus            == true/false
     * @return string http://xxx.xxx.xxx
     */
    public static List parseFaces(ProfileBlogHeadIconSet profileBlogHeadIconSet, String headIconSize, boolean validStatus) {

        if (profileBlogHeadIconSet == null || CollectionUtil.isEmpty(profileBlogHeadIconSet.getIconSet())) {
            logger.info(" profileBlogHeadIconSet is null or profileBlogHeadIconSet.getIconSet() is null .");
            return null;
        }
        List reValue = new ArrayList();

        Set<ProfileBlogHeadIcon> headIconSet = profileBlogHeadIconSet.getIconSet();

        for (ProfileBlogHeadIcon icon : headIconSet) {
            if (icon.getValidStatus() != validStatus) {
                continue;
            }

            if (StringUtil.isEmpty(headIconSize)) {
                reValue.add(parseMFace(icon.getHeadIcon()));

            } else if (HEAD_ICON_SIZE_M.equals(headIconSize)) {
                reValue.add(parseMFace(icon.getHeadIcon()));

            } else if (HEAD_ICON_SIZE_S.equals(headIconSize)) {
                reValue.add(parseSFace(icon.getHeadIcon()));

            } else if (HEAD_ICON_SIZE_SS.equals(headIconSize)) {
                reValue.add(parseSSFace(icon.getHeadIcon()));
            }else if (HEAD_ICON_SIZE_B.equals(headIconSize)) {
                reValue.add(parseBFace(icon.getHeadIcon()));
            }
        }

        return reValue;
    }

    //--- 以下需要修改
    /*
     * 取最大头像
     */
    public static String parseBFace(String furl) {
        //logger.debug("取大头像的地址：" + furl);
        String sReURL = defaultURL + HEAD_ICON_SIZE_M + ".jpg";
        try {
            if (furl != null && !"".equals(furl)) {
                String sR = furl.substring(1, 5); //r001 ResourceServerMonitor.get().getResourceDomainByImageDir(sR)
                // 取文件扩展名
                String exname = furl.substring(furl.lastIndexOf(".")); //.jpg
                String sNametou = furl.substring(0, furl.lastIndexOf(".")); //   /image/2011/07/12/12C14AB9D1DCC4FF4989D5930E45317A_M
                if (sNametou.endsWith("_S") || sNametou.endsWith("_M") || sNametou.endsWith("_SS")) {
                    sNametou = sNametou.substring(0, sNametou.lastIndexOf("_"));//   /image/2011/07/12/12C14AB9D1DCC4FF4989D5930E45317A
                }
                sReURL = "http://" + sR + "." + WebUtil.getDomain() + sNametou + exname;
                //sReURL = "http://" + sR + "." + WebUtil.getDomain() + sNametou + exname+"?"+getMath();
                //logger.debug("返回大头像地址："+sReURL);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return sReURL;
    }

    public static String parseOrgImg(String furl) {
        String sReURL = defaultURL + HEAD_ICON_SIZE_M + ".jpg";
        if (!StringUtil.isEmpty(furl)) {
            String sR = furl.substring(1, 5);
            sReURL = "http://" + sR + "." + WebUtil.getDomain() + furl;
        }

        return sReURL;
    }

    /*
     * 取中头像
     */
    public static String parseMFace(String furl) {
        //logger.debug("取中头像的地址：" + furl);
        String sReURL = defaultURL + HEAD_ICON_SIZE_M + ".jpg";
        try {
            if (furl != null && !"".equals(furl)) {
                String sR = furl.substring(1, 5); //r001
                // 取文件扩展名
                String exname = furl.substring(furl.lastIndexOf(".")); //.jpg
                String sNametou = furl.substring(0, furl.lastIndexOf(".")); //   /image/2011/07/12/12C14AB9D1DCC4FF4989D5930E45317A_M
                if (!sNametou.endsWith("_M")) {
                    if (sNametou.lastIndexOf("_") != -1)
                        sNametou = sNametou.substring(0, sNametou.lastIndexOf("_"));//   /image/2011/07/12/12C14AB9D1DCC4FF4989D5930E45317A
                    sNametou = sNametou + "_M";
                }
                //sReURL = "http://" + sR + "." + WebUtil.getDomain() + sNametou + exname+"?"+getMath();
                sReURL = "http://" + sR + "." + WebUtil.getDomain() + sNametou + exname;
                //logger.debug("返回中头像地址："+sReURL);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return sReURL;
    }

    public static String parseFirstMFace(Profile profile) {
        String reHeadIcon = defaultURL + HEAD_ICON_SIZE_M + ".jpg";
        if (profile.getBlog().getHeadIconSet() == null || CollectionUtil.isEmpty(profile.getBlog().getHeadIconSet().getIconSet())) {
            if (StringUtil.isEmpty(profile.getDetail().getSex())) {
                reHeadIcon = defaultURL + HEAD_ICON_SIZE_M + ".jpg";
            } else if ("0".equals(profile.getDetail().getSex()) || profile.getDetail().getSex() == "0") {
                reHeadIcon = girlURL + HEAD_ICON_SIZE_M + ".jpg";
            } else {
                reHeadIcon = boyURL + HEAD_ICON_SIZE_M + ".jpg";
            }
        } else {
            for (ProfileBlogHeadIcon icon : profile.getBlog().getHeadIconSet().getIconSet()) {
                if (icon.getValidStatus()) {
                    reHeadIcon = ImageURLTag.parseMFace(icon.getHeadIcon());
                    break;
                }
            }
        }
        return reHeadIcon;
    }


    /*
     * 取小头像
     */
    public static String parseSFace(String furl) {
        //logger.debug("取小头像的地址：" + furl);
        String sReURL = defaultURL + HEAD_ICON_SIZE_M + ".jpg";
        try {
            if (furl != null && !"".equals(furl)) {
                String sR = furl.substring(1, 5); //r001
                // 取文件扩展名
                String exname = furl.substring(furl.lastIndexOf(".")); //.jpg
                String sNametou = furl.substring(0, furl.lastIndexOf(".")); //   /image/2011/07/12/12C14AB9D1DCC4FF4989D5930E45317A_M
                if (!sNametou.endsWith("_S")) {
                    if (sNametou.lastIndexOf("_") != -1)
                        sNametou = sNametou.substring(0, sNametou.lastIndexOf("_"));//   /image/2011/07/12/12C14AB9D1DCC4FF4989D5930E45317A
                    sNametou = sNametou + "_S";
                }
                //sReURL = "http://" + sR + "." + WebUtil.getDomain() + sNametou + exname+"?"+getMath();
                sReURL = "http://" + sR + "." + WebUtil.getDomain() + sNametou + exname;
                //logger.debug("返回小头像地址："+sReURL);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return sReURL;
    }

    /*
     * 取超小头像
     */
    public static String parseSSFace(String furl) {
        //logger.debug("取小头像的地址：" + furl);
        String sReURL = defaultURL + HEAD_ICON_SIZE_M + ".jpg";
        try {
            if (!StringUtil.isEmpty(furl)) {
                String sR = furl.substring(1, 5); //r001
                // 取文件扩展名
                String exname = furl.substring(furl.lastIndexOf("."));
                String sNametou = furl.substring(0, furl.lastIndexOf("."));
                if (!sNametou.endsWith("_SS")) {
                    if (sNametou.lastIndexOf("_") != -1)
                        sNametou = sNametou.substring(0, sNametou.lastIndexOf("_"));
                    sNametou = sNametou + "_SS";
                }
                sReURL = "http://" + sR + "." + WebUtil.getDomain() + sNametou + exname;
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return sReURL;
    }


    /**
     * 取大头像 相对路径 /r00x/2011/07/xx/xxxxxxxxxx.xxx
     *
     * @param furl 路径
     * @return String
     */
    public static String parseBFacePath(String furl) {

        String sReURL = defaultURL + HEAD_ICON_SIZE_M + ".jpg";
        try {
            if (furl != null && !"".equals(furl)) {
                String exname = furl.substring(furl.lastIndexOf(".")); //.jpg
                String sNametou = furl.substring(0, furl.lastIndexOf(".")); //   /image/2011/07/12/12C14AB9D1DCC4FF4989D5930E45317A_M
                if (sNametou.endsWith("_S") || sNametou.endsWith("_M") || sNametou.endsWith("_SS")) {
                    if (sNametou.lastIndexOf("_") != -1)
                        sNametou = sNametou.substring(0, sNametou.lastIndexOf("_"));//   /image/2011/07/12/12C14AB9D1DCC4FF4989D5930E45317A
                }
                sReURL = sNametou + exname;
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return sReURL;
    }

    /**
     * 处理随便看看的图片地址
     */
    public static String parseWallThumbImgLink(String thumbImgLink) {
        String sReURL = defaultURL + HEAD_ICON_SIZE_M + ".jpg";
        try {
            if (!StringUtil.isEmpty(thumbImgLink)) {
                String sR = thumbImgLink.substring(1, 5); //r001
                Pattern pattern = Pattern.compile("r\\d+$");

                if (pattern.matcher(sR).find()) {
                    sReURL = "http://" + sR + "." + WebUtil.getDomain() + thumbImgLink;
                } else if (thumbImgLink.startsWith("http")) {
                    sReURL = thumbImgLink;
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return sReURL;
    }

    /**
     * 得到185*185图
     *
     * @param src
     * @return String
     */
    public static String parseAudioM(String src) {
        return ImageResolveUtil.parseAudioM(src);
    }

    /**
     * &#x5f97;&#x5230;&#x539f;&#x56fe;
     *
     * @param src
     * @return String
     */
    public static String parseAudioO(String src) {
        return ImageResolveUtil.parseAudioB(src);
    }

    public static String parseAudioS(String src) {
        return ImageResolveUtil.parseAudioS(src);
    }

    public static String parseAudioSs(String src) {
        return ImageResolveUtil.parseAudioSs(src);
    }

    /**
     * 用户中心 头像。
     *
     * @return
     */
    public static String parseUserCenterHeadIcon(String headIcon, String sex, String headIconSize, boolean validStatus) {

        String reValue = headIcon;
        if (StringUtil.isEmpty(headIcon)) {
            if (logger.isDebugEnabled()) {
                logger.debug(" profileBlogHeadIconSet is null or profileBlogHeadIconSet.getIconSet() is null .");
            }

            if (StringUtil.isEmpty(sex)|| "-1".equals(sex)) {
                return getUserHeadUrl(defaultURL, headIconSize);
            } else if ("0".equals(sex)) {
                return getUserHeadUrl(girlURL, headIconSize);
            } else {
                return getUserHeadUrl(boyURL, headIconSize);
            }
        }
        return reValue;
    }

}
