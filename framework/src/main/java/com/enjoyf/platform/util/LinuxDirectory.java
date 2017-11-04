package com.enjoyf.platform.util;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import com.google.common.base.Strings;

/**
 * @Auther: <a mailto:yinpengyi@platform.com>Yin Pengyi</a>
 */
public class LinuxDirectory {
    //
    private static final String FILE_SEPARATOR = "/";
    public static final String ROOT = "/";

    //
    private List<String> dirs = new ArrayList<String>();
    private int curDirDeep = 0;

    //////////////////////////////////////////////////////////////
    public LinuxDirectory() {
    }

    public LinuxDirectory(String dirStr) {
        append(dirStr);
    }

    public LinuxDirectory append(String dirStr) {
        if (Strings.isNullOrEmpty(dirStr)) {
            return this;
        }

        StringTokenizer tokenizer = new StringTokenizer(dirStr, FILE_SEPARATOR);
        while (tokenizer.hasMoreTokens()) {
            String dir = tokenizer.nextToken();
            if (Strings.isNullOrEmpty(dir)) {
                continue;
            }

            dirs.add(dir);
        }

        return this;
    }

    public LinuxDirectory append(LinuxDirectory dir) {
        if (dir == null) {
            return this;
        }

        dirs.addAll(dir.dirs);

        return this;
    }

    public boolean hasNextDir() {
        return curDirDeep < dirs.size();
    }

    public String getNextDeepDir() {
        String returnValue = FILE_SEPARATOR;

        curDirDeep++;

        if (curDirDeep > dirs.size()) {
            curDirDeep = dirs.size();
        }

        for (int i = 0; i < curDirDeep; i++) {
            returnValue = returnValue + dirs.get(i) + FILE_SEPARATOR;
        }

        return returnValue;
    }

    public String getLastDeepDir() {
        String returnValue = ROOT;

        for (String dir : dirs) {
            returnValue = returnValue + dir + FILE_SEPARATOR;
        }

        return returnValue;
    }

    public List<String> getDirs() {
        return dirs;
    }

    public int getDirDeep() {
        return dirs.size();
    }

    public int getCurDirDeep() {
        return curDirDeep;
    }

    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
