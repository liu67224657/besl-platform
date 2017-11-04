package com.enjoyf.platform.util;

import com.enjoyf.platform.crypto.MD5Cryptor;
import com.enjoyf.platform.service.content.ResourceFileType;
import com.google.common.base.Strings;

import java.text.ParseException;
import java.util.Date;
import java.util.UUID;


/**
 * hash ID 工具类
 *
 * @author xinzhao
 */
public class ResourceFilePathUtil {
    //the md5 instance
    private static MD5Cryptor md5 = new MD5Cryptor();

    //文件分割符
    public static String PATH_SEPARATOR = "/";

    //日期符号路径格式
    public static String FMT_DATE_PATH = "/yyyy/MM";

    /**
     * 生成资源路径
     *
     * @param fileName String 资源文件名
     * @return 资源路径
     */
    public static String getFilePath(String resDomainPrefix, String fileName, ResourceFileType fileType) {
        //
        String rootPath = getResPathOfDomainPrefixAndType(resDomainPrefix, fileType.getCode());
        String datePath = getResPathOfDate();
        String hashPath = getResPathOfHash(fileName);

        //
        return rootPath + datePath + hashPath + PATH_SEPARATOR;
    }

    /**
     * 生成资源名
     *
     * @param fileName String 资源文件名
     * @return 资源名
     */
    public static String getFileName(String fileName) {
        if (!Strings.isNullOrEmpty(fileName)) {
            //取文件扩展名
            return UUID.randomUUID().toString().replaceAll("-","0");
        } else {
            return null;
        }
    }

    /**
     * 取时间路径
     *
     * @return 时间路径: /1900/01
     */
    private static String getResPathOfDate() {
        return DateUtil.formatDateToString(new Date(), FMT_DATE_PATH);
    }

    /**
     * HASH取模路径
     *
     * @return 取模路径:/00
     */
    private static String getResPathOfHash(String fileName) {
        if (fileName != null) {
            return PATH_SEPARATOR + getHashCode(fileName);
        } else {
            return null;
        }
    }

    /**
     * @param resType 资源类型: /r001/image, /r001/headicon, etc
     */
    private static String getResPathOfDomainPrefixAndType(String domainPrefix, String resType) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(PATH_SEPARATOR).append(domainPrefix).append(PATH_SEPARATOR).append(resType);

        return stringBuilder.toString();
    }

    /**
     * 根据传入的字符串参数获得对应的hashcode数值
     *
     * @param fileName String
     * @return int
     */
    private static int getHashCode(String fileName) {
        if (fileName != null) {
            fileName = fileName.trim();

            return Math.abs(fileName.hashCode() % 100);
        } else {
            return 0;
        }
    }

    public static Date getDateByFilePath(String filePath) {
        String[] splits = filePath.split(PATH_SEPARATOR);

        try {
            return DateUtil.formatStringToDate(PATH_SEPARATOR + splits[3] + PATH_SEPARATOR + splits[4], FMT_DATE_PATH);
        } catch (ParseException e) {
            return null;
        }
    }

    public static String getServerNoByFilePath(String filePath) {
        String[] splits = filePath.split(PATH_SEPARATOR);

        return splits[1];
    }
}
