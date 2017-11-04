package com.enjoyf.platform.service.content;

import com.enjoyf.platform.util.StringUtil;

/**
 * <p/>
 * Description:
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>liuhao</a>
 */
public class ResourceFileIdGenerator {
    public static String genFileId(String src) {
        if (StringUtil.isEmpty(src)) {
            return null;
        }
        String imageName = src;
        int imageSizeIdx = src.lastIndexOf("_");
        if (imageSizeIdx >= 0) {
            int extNameIdx = src.lastIndexOf(".");
            String extNameStr = src.substring(extNameIdx);
            imageName = src.substring(0, imageSizeIdx);
            imageName += extNameStr;
        }

        return imageName;
    }

    public static void main(String[] args) {
        System.out.println(ResourceFileIdGenerator.genFileId("/r001/image/2012/01/65/F0E8A972035ECCA85FC63703E9511245.jpg"));
    }
}
