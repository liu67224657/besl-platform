package com.enjoyf.platform.webapps.common.picture;

import com.enjoyf.platform.util.ResourceFilePathUtil;

/**
 * <p/>
 * Description:
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>liuhao</a>
 */
public class UploadUtil {
    public static String genImageName(String srcImg, String suffix) {
        String fileqian = srcImg.substring(srcImg.lastIndexOf(ResourceFilePathUtil.PATH_SEPARATOR) + 1, srcImg.indexOf("."));
        if (fileqian.lastIndexOf("_") != -1)
            fileqian = fileqian.substring(0, fileqian.lastIndexOf("_"));
        String filehou = srcImg.substring(srcImg.indexOf(".")); // .jpg
        String filePath = srcImg.substring(0, srcImg.lastIndexOf(ResourceFilePathUtil.PATH_SEPARATOR) + 1);
        return filePath + fileqian + suffix + filehou;
    }
}
