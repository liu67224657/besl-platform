/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.tools.regex;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-8-30 上午11:38
 * Description: 转移旧的博文。
 */
public class RegExChecker {
    private static final Logger logger = LoggerFactory.getLogger(RegExChecker.class);

    private static Pattern regexPattern;

    public static void main(String[] args) {
        regexPattern = Pattern.compile(args[0]);
        Matcher m = regexPattern.matcher(args[1]);

        System.out.println("XXXXXXXXXXXXX" + m.find());
    }
}
