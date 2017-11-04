/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.util.htmlparse;


import com.enjoyf.platform.util.HTMLEntities;
import com.google.common.base.Strings;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Garrison
 */
 class StringUtil {


    public static boolean isEmpty(String sval) {
        return (sval == null || sval.trim().length() < 1);
    }

    public static String regReplace(String sval, String replacePattern, String destRep, int caseIgnore) {
        if (sval != null && !sval.equals("")) {
            Pattern _pattern = Pattern.compile(replacePattern, caseIgnore);
            Matcher _matcher = _pattern.matcher(sval);
            StringBuffer _strBuf = new StringBuffer();

            boolean _findFlag = _matcher.find();

            while (_findFlag) {
                _matcher.appendReplacement(_strBuf, destRep);
                _findFlag = _matcher.find();
            }
            _matcher.appendTail(_strBuf);

            return _strBuf.toString();
        } else {
            return "";
        }
    }

    public static String replace(String sval, String replacePattern, String destVal, int caseIgnore) {
        return regReplace(sval, replacePattern, destVal, caseIgnore);
    }







}
