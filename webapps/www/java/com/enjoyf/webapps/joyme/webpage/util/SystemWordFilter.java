/**
 *
 */
package com.enjoyf.webapps.joyme.webpage.util;

import org.apache.log4j.Logger;

import java.util.Enumeration;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 系统 过滤词
 *
 * @author zx
 */
public class SystemWordFilter {

    static Logger logger = Logger.getLogger(SystemWordFilter.class);
    private static ResourceBundle resourceBundlePOST = null;
    private static ResourceBundle resourceBundleDOMAIN = null;

    //发布文章时使用
    private static String[] POST_KEYS = {"ZHENG_ZHI", "FAN_DONG", "CU_KOU", "SE_QING", "GUANG_GAO", "QI_TA"};
    //全部过滤
    private static String[] TEXT_ALL_KEYS = {"ZHENG_ZHI", "FAN_DONG", "CU_KOU", "SE_QING", "GUANG_GAO", "QI_TA", "FH"};
    // 昵称
    private static String[] NI_CHENG_KEYS = {"NI_CHENG"};

    //取注册发文过滤文件
    private static ResourceBundle getResPOST() {
        if (resourceBundlePOST == null) {
            resourceBundlePOST = ResourceBundle.getBundle("props.common.post_glc");
        }
        return resourceBundlePOST;
    }

    //取域名过滤文件
    private static ResourceBundle getResDOMAIN() {
        if (resourceBundleDOMAIN == null) {
            resourceBundleDOMAIN = ResourceBundle.getBundle("props.common.domain_glc");
        }
        return resourceBundleDOMAIN;
    }

    public static String getFieldsPOST(String fkey) {
        return "" + getResPOST().getObject(fkey);
    }

    public static String getFieldsDOMAIN(String fkey) {
        return "" + getResDOMAIN().getObject(fkey);
    }


    /**
     * 过滤发文全部  ,非法字符变成*
     */
    public static String filterPOSTAll(String sworld) {
        Enumeration enumer = getResPOST().getKeys();

        // 需要去掉空格
        //String tmpStr = sworld.trim();
        String tmpStr = sworld;
        while (enumer.hasMoreElements()) {
            String key = (String) enumer.nextElement();
            String field = getFieldsPOST(key);
            //logger.info(field);
            if (field == null || "".equals(field)) {
                continue;
            }
            tmpStr = chickOne(tmpStr, field);
            //tmpStr = chickTwo(tmpStr, field);

        }
        logger.debug(tmpStr);
        return tmpStr;
    }


    /**
     * 检查文章、描述是否包括屏蔽字
     */
    public static boolean checkText(String sworld) {
        return checkByRules(sworld, TEXT_ALL_KEYS);
    }

    /**
     * 检查昵称
     */
    public static boolean checkNiCheng(String sworld) {
        logger.debug("检查昵称");
        return checkByRules(sworld, NI_CHENG_KEYS);
    }

    /**
     * 检查发布的非法字符
     *
     * @param sworld
     * @return
     */
    public static boolean checkPost(String sworld) {
        logger.debug("检查发布的非法字符");
        return checkByRules(sworld, POST_KEYS);
    }

    private static boolean checkByRules(String sworld, String[] ruleKeys) {
        boolean reVal = false;

        // 需要去掉空格
        String tmpStr = sworld.trim();

        for (String rule : ruleKeys) {
            String field = getFieldsPOST(rule);
            //logger.info(field);
            if (field == null || "".equals(field)) {
                continue;
            }
            reVal = checkDOMAINOne(tmpStr, field);
            if (!reVal) {
                logger.debug(reVal);
                return reVal;
            }
        }
        return reVal;
    }

    /**
     * 检查命名 (domain)
     */
    public static boolean checkMM(String sworld) {
        boolean reVal = false;
        Enumeration enumer = getResDOMAIN().getKeys();

        // 需要去掉空格
        //String tmpStr = sworld.trim();

        String tmpStr = sworld;

        while (enumer.hasMoreElements()) {
            String key = (String) enumer.nextElement();
            String field = getFieldsDOMAIN(key);
            //logger.info(field);
            if (field == null || "".equals(field)) {
                continue;
            }
            reVal = checkDOMAINOne(tmpStr, field);
            if (!reVal) {
                logger.debug(reVal);
                return reVal;
            }
        }
        return reVal;
    }


    /**
     * 过滤命名
     */
    private static boolean checkDOMAINOne(String sworld, String srelue) {
        boolean reVal = true;
        Pattern pattern = Pattern.compile(srelue, Pattern.CASE_INSENSITIVE);
        Matcher m = pattern.matcher(sworld);

        while (m.find()) {
            reVal = false;
            //System.out.println(m.group());
            logger.debug(m.group());
            return reVal;
        }

        logger.debug("第一次过滤命名:" + reVal);
        return reVal;
    }


    // 第一次过滤   ,过滤非法字为*
    private static String chickOne(String sworld, String srelue) {
        Pattern pattern = Pattern.compile(srelue, Pattern.CASE_INSENSITIVE);
        Matcher m = pattern.matcher(sworld);

        // 将查到的字符换成×
        while (m.find()) {
            sworld = sworld.replaceFirst(srelue, getStar(m.group().length()));
        }
        logger.debug("第一次过滤返回结果:" + sworld);
        return sworld;
    }


    private static String getStar(int strLength) {
        StringBuffer sbuff = new StringBuffer();
        for (int i = 0; i < strLength; i++) {
            sbuff.append("*");
        }
        return sbuff.toString();
    }

}
