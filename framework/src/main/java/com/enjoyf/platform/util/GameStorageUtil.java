package com.enjoyf.platform.util;

import java.text.DecimalFormat;

import com.google.common.base.Strings;

/**
 * @author Daniel
 */
public class GameStorageUtil {

    public static String getStoragePath(Integer gameId) {
	return getStoragePath(null, gameId);
    }

    public static String getStoragePath(String prefix, Integer gameId) {
	DecimalFormat nf = new DecimalFormat("000");
	StringBuilder sb = null;
	if (!Strings.isNullOrEmpty(prefix)) {
	    sb = new StringBuilder(prefix);
	} else {
	    sb = new StringBuilder();
	}
	sb.append("/");
	sb.append(nf.format(gameId / 1000));
	sb.append("/");
	sb.append(nf.format(gameId % 1000));

	return sb.toString();
    }

    public static String getIconsDir(int id) {
	return getStoragePath("/icons", id);
    }

    public static String getIconsDir(String s) {
	int id = Integer.valueOf(s);
	return getStoragePath("/icons", (int) id);
    }

    public static String getScreenShotDir(int id) {
	return getStoragePath("/screens", id) + "/" + id;
    }

    public static String getScreenShotDir(String s) {
	int id = Integer.valueOf(s);
	return getStoragePath("/screens", id) + "/" + id;
    }

    public static String getApkDir(int id) {
	return getStoragePath("/apks", id);
    }

    public static String getApkDir(String s) {
	int id = Integer.valueOf(s);
	return getStoragePath("/apks", id);
    }

}
