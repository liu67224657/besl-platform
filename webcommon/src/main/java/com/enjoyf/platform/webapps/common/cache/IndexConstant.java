package com.enjoyf.platform.webapps.common.cache;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  13-9-4 下午4:27
 * Description:
 */
public class IndexConstant {

    public static String index_base_dir = "";

    public static String setIndexBaseDir(String path) {
        return index_base_dir = path;
    }

    public static String getIndexBaseDir() {
        return index_base_dir;
    }

    public static String getIndexImageBaseDir() {
        return index_base_dir + "/images";
    }

    public static String getIndexJsDir() {
        return index_base_dir + "/js";
    }

    public static String getIndexCssDir() {
        return index_base_dir + "/css";
    }

    public static String getIndexHtmlDir() {
        return index_base_dir + "/index.html";
    }
}
