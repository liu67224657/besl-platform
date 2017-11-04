package com.enjoyf.platform.webapps.common.util;

import com.enjoyf.platform.util.StringUtil;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * HTMl标签过滤
 *
 * @author yongmingxu
 */
public class HtmlFilter {
    static Logger logger = Logger.getLogger(HtmlFilter.class);
    //定义script的正则表达式{或<script[^>]*?>[\\s\\S]*?<\\/script> }
    private static String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>";
    //定义style的正则表达式{或<style[^>]*?>[\\s\\S]*?<\\/style> }
    private static String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>";
    //定义HTML标签的正则表达式
    private static String regEx_html = "<[^>]+>";

    //匹配img
    private static String regEx_img = "<img.*src=(.*?)[^>]*?>";
    //匹配src
    private static String regEx_src = "src=\"?(.*?)(\"|>|\\s+)";

    public static String Html2Text(String htmlStr) {
        String textStr = "";
        java.util.regex.Pattern p_script;
        java.util.regex.Matcher m_script;
        java.util.regex.Pattern p_style;
        java.util.regex.Matcher m_style;
        java.util.regex.Pattern p_html;
        java.util.regex.Matcher m_html;

        try {
            p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
            m_script = p_script.matcher(htmlStr);
            htmlStr = m_script.replaceAll(""); //过滤script标签

            p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
            m_style = p_style.matcher(htmlStr);
            htmlStr = m_style.replaceAll(""); //过滤style标签

            p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
            m_html = p_html.matcher(htmlStr);
            htmlStr = m_html.replaceAll(""); //过滤html标签

            textStr = StringUtil.trimString(htmlStr);
            textStr = textStr.replaceAll("&nbsp;", " ");
        } catch (Exception e) {
            logger.debug("Html2Text: " + e.getMessage());
        }

        return textStr;//返回文本字符串
    }

    /**
     * 得到图片地址
     *
     * @param htmlStr
     * @return List<String>
     */
    public static List<String> getImgSrcs(String htmlStr) {
        String img = "";
        Pattern p_image;
        Matcher m_image;
        List<String> pics = new ArrayList<String>();
        p_image = Pattern.compile(regEx_img, Pattern.CASE_INSENSITIVE);//匹配<img/>
        m_image = p_image.matcher(htmlStr);
        while (m_image.find()) {
            img = img + "," + m_image.group();
            logger.debug("img ----->" + img);
            Matcher m = Pattern.compile(regEx_src).matcher(img); //匹配src
            while (m.find()) {
                pics.add(m.group(1));
                logger.debug("imgsrc add----->" + m.group(1));
            }
        }
        return pics;
    }

}
