package com.enjoyf.platform.webapps.common.wordfilter;

/**
 * Created by IntelliJ IDEA.
 * User: yongmingxu
 * Date: 11-10-28
 * Time: 下午1:43
 * To change this template use File | Settings | File Templates.
 */

import com.enjoyf.platform.props.hotdeploy.ContextHotdeployConfig;
import com.enjoyf.platform.props.hotdeploy.HotdeployConfigFactory;
import com.enjoyf.platform.service.usercenter.LoginDomain;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.regex.RegexUtil;
import org.apache.log4j.Logger;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 內容敏感词过滤工具类
 */
public class ContextFilterUtils {
    static Logger logger = Logger.getLogger(ContextFilterUtils.class);
    private static final Pattern CHECK_NICK_PATTERN = Pattern.compile("^[\u4e00-\u9fa5a-zA-Z0-9]+$");//只能中文、字母、数字

    private static ContextHotdeployConfig config = HotdeployConfigFactory.get().getConfig(ContextHotdeployConfig.class);

    /**
     * @param context
     * @return true(all matchs true, return true, else return false)
     */
    public static boolean checkNickNames(String context) {
        return RegexUtil.match(context, config.getNicknameFormulaRegexs());
    }

    public static boolean checkNickNamesBlackList(String context) {
        return RegexUtil.contain(context, config.getNicknameBackRegexs());
    }

    /**
     * 检查域名是否符合规则
     *
     * @param context
     * @return true(all matchs true, return true, else return false)
     */
    public static boolean checkDomainFormulaRegexs(String context) {
        return RegexUtil.match(context, config.getDomainNameFormulaRegexs());
    }

    /**
     * 检查域名是否为保留字
     *
     * @param context
     * @return true of contain (one contain return true)
     */
    public static boolean checkDomainBlackList(String context) {
        return RegexUtil.contain(context, config.getDomainNameBackRegexs());
    }

    /**
     * 检查是否为咔哒保留字
     *
     * @param context
     * @return true of contain (one contain return true)
     */
    public static boolean checkKadaDomainBlackList(String context) {
        return config.getKadaPostSeeker().findWords(context).size() > 0;
    }

    /**
     * 检查富文本输入是否符合规则
     *
     * @param context
     * @return true(all matchs true, return true, else return false)
     */
    public static boolean checkRichEditorRegexs(String context) {
        return RegexUtil.match(context, config.getRichEditorFormulaRegexs());
    }

    /**
     * 检查名称是否符合规则
     *
     * @param nick
     * @return 敏感词判断
     * 只能中文、字母、数字
     * 2-10个汉字或者 4-20个字母数字
     */


    public static boolean checkNickRegexs(String nick) {
        boolean bool = config.getPostSeeker().findWords(nick).size() > 0;//敏感词判断
        if (bool) {
            return bool;
        }
        Matcher m = CHECK_NICK_PATTERN.matcher(nick);
        bool = !m.matches();
        if (bool) {
            return bool;
        }

        int nickLength = getStrLength(nick);//2-10个汉字或者 4-20个字母数字
        if (nickLength < 4 || nickLength > 20) {
            bool = true;
        }
        return bool;
    }

    //计算字符长度  中文＝2字节
    private static int getStrLength(String value) {
        int valueLength = 0;
        String chinese = "[\u0391-\uFFE5]";
        for (int i = 0; i < value.length(); i++) {
            String temp = value.substring(i, i + 1);
            if (temp.matches(chinese)) {
                valueLength += 2;
            } else {
                valueLength += 1;
            }
        }
        return valueLength;
    }

    /**
     * 文章是否包含敏感字
     *
     * @param context
     * @return
     */
    public static boolean postContainBlackList(String context) {
//        return RegexUtil.contain(context, config.getRichEditorBackRegexs());
        return config.getPostSeeker().findWords(context).size() > 0;
    }

    /**
     * 文章是否包含敏感字
     *
     * @param context
     * @return
     */
    public static Set<String> getPostContainBlackList(String context) {
//        return RegexUtil.contain(context, config.getRichEditorBackRegexs());
        return config.getPostSeeker().findWords(context);
    }

    /**
     * 检查富文本输入是否含敏感字
     *
     * @param context
     * @return true of contain (one contain return true)
     */
    public static boolean checkRichEditorBlackList(String context) {
        return RegexUtil.contain(context, config.getRichEditorBackRegexs());
    }

    /**
     * 检查普通文本输入是否符合规则
     *
     * @param context
     * @return true(all matchs true, return true, else return false)
     */
    public static boolean checkSimpleEditorRegexs(String context) {
        return RegexUtil.match(context, config.getSimpleEditorFormulaRegexs());
    }

    /**
     * 检查普通文本输入是否含敏感字
     *
     * @param context
     * @return true of contain (one contain return true)
     */
    public static boolean checkSimpleEditorBlackList(String context) {
        return RegexUtil.contain(context, config.getSimpleEditorBackRegexs());
    }

    public static Set<String> getSimpleEditorBlackList(String context) {
//        return RegexUtil.contain(context, config.getRichEditorBackRegexs());
        return config.getRichEditorSeeker().findWords(context);
    }

    /**
     * 检查Tag是否符合规则
     *
     * @param context
     * @return true(all matchs true, return true, else return false)
     */
    public static boolean checkTagRegexs(String context) {
        return RegexUtil.match(context, config.getTagFormulaRegexs());
    }

    /**
     * 检查Tag是否含敏感字
     *
     * @param context
     * @return true of contain (one contain return true)
     */
    public static boolean checkTagBlackList(String context) {
        return RegexUtil.contain(context, config.getTagBackRegexs());
    }

    /**
     * 检查标题是否符合规则
     *
     * @param context
     * @return true(all matchs true, return true, else return false)
     */
    public static boolean checkSubjectRegexs(String context) {
        return RegexUtil.match(context, config.getSubjectFormulaRegexs());
    }

    /**
     * 检查标题是否含敏感字
     *
     * @param context
     * @return true of contain (one contain return true)
     */
    public static boolean checkSubjectBlackList(String context) {
        return RegexUtil.contain(context, config.getSubjectBackRegexs());
    }

    /**
     * 检查博客主题是否符合规则
     *
     * @param context
     * @return true(all matchs true, return true, else return false)
     */
    public static boolean checkBlogTopicRegexs(String context) {
        return RegexUtil.match(context, config.getBlogTopicFormulaRegexs());
    }

    /**
     * 检查标题是否含敏感字
     *
     * @param context
     * @return true of contain (one contain return true)
     */
    public static boolean checkBlogTopicBlackList(String context) {
        return RegexUtil.contain(context, config.getBlogTopicBackRegexs());
    }

    public static boolean customModuleContainBlackWord(String mouduleName) {
        return RegexUtil.contain(mouduleName, config.getCustomModuleBackRegexs());
    }

    public static boolean customModuleMatchRegex(String mouduleName) {
        return RegexUtil.match(mouduleName, config.getCustomModuleFormulaRegexs());
    }

    /**
     * 检查自主的loginkey
     *
     * @param loginKey
     * @return
     */
    public static boolean verifyLoginKey(String loginKey) {
        if (StringUtil.isEmpty(loginKey)) {
            return false;
        }

        return RegexUtil.match(loginKey, RegexUtil.USERID_PATTERN);
    }

    /**
     * 检查自主的password
     *
     * @param pwd
     * @return
     */
    public static boolean verifyPassword(String pwd) {
        if (StringUtil.isEmpty(pwd)) {
            return false;
        }

        return RegexUtil.match(pwd, RegexUtil.PWD_PATTERN);
    }


}
