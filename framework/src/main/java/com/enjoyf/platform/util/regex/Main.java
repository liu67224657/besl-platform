/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.util.regex;

import com.enjoyf.platform.props.hotdeploy.ContextHotdeployConfig;
import com.enjoyf.platform.props.hotdeploy.HotdeployConfigFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-9-27 下午8:38
 * Description:
 */
public class Main {
    public static void main(String[] args) {
        String s = "<b>sdfdsfdsfds<b>fds中文</b>";

        Matcher matcher = Pattern.compile("<b>((?!</?b>).)*</b>").matcher(s);
        while (matcher.find()) {
            System.out.println(matcher.group(1));
        }
        System.out.println("-----------------固化分组--------------------");

        String s2="62500";
//        String s2="625"; //固化分组会匹配失败
        Matcher matcher2 = Pattern.compile("(\\d\\d(?>[1-9]?))\\d+").matcher(s2);
        while (matcher2.find()) {
            System.out.println(matcher2.group(1));
        }

        System.out.println("-----------------占有优先--------------------");
        String s3="625"; //占有优先 倆者转化 (?>[1-9]?) 等价于[1-9]?+
        Matcher matcher3 = Pattern.compile("(\\d\\d[1-9]?+)\\d+").matcher(s3);
        while (matcher3.find()) {
            System.out.println(matcher3.group(1));
        }
    }
}
