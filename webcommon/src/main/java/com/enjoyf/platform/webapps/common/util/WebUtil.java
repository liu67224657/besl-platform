package com.enjoyf.platform.webapps.common.util;

import com.enjoyf.platform.props.WebappConfig;
import com.enjoyf.platform.util.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * 工具类，从配置文件中读取验证，和相关邮件属性
 */
public class WebUtil {
    private static Logger logger = LoggerFactory.getLogger(WebUtil.class);

    /**
     * 注册开关
     *
     * @return
     */
    public static boolean openRegister() {
        return WebappConfig.get().isOpenRegister();
    }

    /**
     * 得到注册后邮件的发送人地址
     *
     * @return
     */
    public static String getRegisterMailFromName() {
        return WebappConfig.get().getRegisterMailFromName();
    }

    public static String getRegisterMailFromAddr() {
        return WebappConfig.get().getRegisterMailFromAddr();
    }

    /**
     * 得到注册后邮件的主题
     *
     * @return
     */
    public static String getRegisterMailSubject() {
        return WebappConfig.get().getRegisterMailSubject();
    }

    /**
     * 密码找回，返回发件人地址
     *
     * @return
     */
    public static String getPwdForgotMailFromName() {
        return WebappConfig.get().getPwdForgotMailFromName();
    }

    public static String getPwdForgotMailFromAddr() {
        return WebappConfig.get().getPwdForgotMailFromAddr();
    }

    /**
     * 密码找回，返回邮件主题
     *
     * @return
     */
    public static String getPwdForgotMailSubject() {
        return WebappConfig.get().getPwdForgotMailSubject();
    }

    /**
     * -------------------------------------
     * ------修改登录邮箱发送邮件相关设置-----开始---
     * ----------------------------------
     */
    public static String getModifyMailFromName() {
        return WebappConfig.get().getModifyMailFromName();
    }

    public static String getModifyMailFromAddr() {
        return WebappConfig.get().getModifyMailFromAddr();
    }

    public static String getModifyMailSubject() {
        return WebappConfig.get().getModifyMailSubject();
    }

    public static String getVerifyMailFromName() {
        return WebappConfig.get().getVerifyMailFromName();
    }

    public static String getVerifyMailFromAddr() {
        return WebappConfig.get().getVerifyMailFromAddr();
    }

    public static String getVerifyMailSubject() {
        return WebappConfig.get().getVerifyMailSubject();
    }

    /**
     * -------------------------------------
     * ------修改登录邮箱发送邮件相关设置---结束---
     * ----------------------------------
     */

    /**
     * 通过host得到子域名，如果不合法并 或者 子域名是www返回空
     *
     * @param host
     * @return
     * @throws MalformedURLException
     */
    public static SubDomainEntity getSubDomainByHost(String host) throws MalformedURLException {
        SubDomainEntity returnValue = new SubDomainEntity();

        //joyme.domain
        String[] domain = getDomain().split("\\.");


        if (!host.startsWith("http")) {
            host = "http://" + host;
        }

        URL url = new URL(host);
        String[] hosts = url.getHost().split("[.]");

        //处理长度为2的信息
        if (hosts.length == 2 && (domain[0].equals(hosts[0]) || domain[1].equals(hosts[1]))) {
            returnValue.setVerify(true);
            return returnValue;
        }

        //处理长度为3的信息
        if (hosts.length == 3) {
            //判断是否是joyme.com的2级域名
            if (!domain[0].equals(hosts[1]) || !domain[1].equals(hosts[2])) {
                returnValue.setVerify(false);
                return returnValue;
            }

            if (hosts[0].equalsIgnoreCase("wwww")) {
                hosts[0] = "www";
                returnValue.setSubDomain(hosts[0].trim());
                return returnValue;
            }

            if (WebUtil.verfiyDomain(hosts[0].trim())) {
                returnValue.setSubDomain(hosts[0].trim());
                return returnValue;
            }

        } else {
            returnValue.setVerify(false);
        }

        return returnValue;
    }

    /**
     * 防止跨站攻击 把传入值里的符号替换为html语言显示
     * @param text
     * @return
     */
    public static  String replaceHtmlText(String text) {
        if (StringUtil.isEmpty(text)) {
            return "";
        }
        text = text.replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\"", "&quot;").replace("'", "&apos;");
        return text;
    }

    /**
     * 返回网站主域名
     */
    public static String getUrlWww() {
        return WebappConfig.get().getUrlWww();
    }

    /**
     * 返回网站用户资源域名
     */
    public static String getDomain() {
        return WebappConfig.get().getDomain();
    }

    /**
     * 返回网站资源库域名
     */
    public static String getUrlLib() {
        return WebappConfig.get().getUrlLib();
    }
    public static String getUrlStatic(){
        return WebappConfig.get().getUrlStatic();
    }

    /* ------------  验证  ------------------------*/

    /**
     * 验证邮件有效性
     *
     * @param email
     * @return
     */
    public static boolean verifyEmail(String email) {
        logger.debug("Email checking.");

        boolean returnValue = true;

        if (StringUtil.isEmpty(email)) {
            returnValue = false;
        } else {
            returnValue = WebappConfig.get().getPatternEmail().matcher(email).matches();
        }
        return returnValue;
    }


    public static boolean verifyChat(String chatContent) {
        boolean bVal = false;

        bVal = !StringUtil.isEmpty(chatContent) && chatContent.length() <= WebappConfig.get().getBlogChatLength();

        return bVal;
    }

    public static boolean verifyTextContent(String textContent) {
        return StringUtil.isEmpty(textContent) || textContent.length() <= WebappConfig.get().getBlogTextLength();
    }

    public static boolean verifyTextSubject(String subject) {
        return subject.length() <= WebappConfig.get().getBlogChatLength();
    }

    public static boolean verifyForwardContentLength(String content) {

        boolean bVal = StringUtil.isEmpty(content) || content.length() <= WebappConfig.get().getBlogChatLength();

        return bVal;
    }

    /**
     * 验证字符型字段是否为空
     */
    public static Boolean stringFieldIsEmpty(String field) {
        return StringUtils.isEmpty(field);
    }

    /**
     * 验证域名是否合法
     *
     * @param domain
     * @return
     */
    public static Boolean verfiyDomain(String domain) {
        return !StringUtil.isEmpty(domain);
    }

    /**
     * 默认的文章内容
     *
     * @return
     */
    public static String getDefaultTextDefault() {
        return WebappConfig.get().getDefaultTextDefault();
    }

    /**
     * 博文首页显示的数量
     *
     * @return
     */


    /**
     * 发现墙显示数量
     *
     * @return
     */
//    public static int getDiscoveryInitSize() {
//        return WebappConfig.get().getDiscoveryInitSize();
//    }
//
//    public static int getDiscoveryPerSize() {
//        return WebappConfig.get().getDiscoveryPerSize();
//    }
//
//    public static int getBlogTopicTagSize() {
//        return WebappConfig.get().getBlogTopicTagSize();
//    }
//
//    public static int getBlogTopicSize() {
//        return WebappConfig.get().getBlogTopicSize();
//    }
    public static String getSystemUno() {
        return WebappConfig.get().getSystemUno();
    }


    /**
     * 游戏打分常量
     */
    public static String getScoringRadio() {
        return WebappConfig.get().getHiddenshowradio();
    }

    /**
     * 用户上传目录
     */
    public static String getUploadRootpath() {
        return WebappConfig.get().getUploadRootpath();
    }
}


