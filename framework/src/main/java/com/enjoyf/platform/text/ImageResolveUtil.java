package com.enjoyf.platform.text;

import com.enjoyf.platform.props.WebappConfig;
import com.enjoyf.platform.util.StringUtil;

import java.util.Map;

/**
 * <p/>
 * Description:上传的图片转换类（包括音乐下迷惘的格式，上传图片的格式）
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>liuhao</a>
 */
public class ImageResolveUtil {

    /**
     * 例子
     * /r001/image/2011/07/12/12C14AB9D1DCC4FF4989D5930E45317A.jpg
     *
     * @param furl
     * @param size _SS _S _M  sizeCode为空为原图
     * @return
     */
    public static String getImageBySize(String furl, ImageSize size) {

        String exname = "";
        String imageSrc = "";
        if (StringUtil.isEmpty(furl) || furl.lastIndexOf(".") == -1) {
            return "";
        }

        // 取文件扩展名
        exname = furl.substring(furl.lastIndexOf(".")); //.jpg
        imageSrc = furl.substring(0, furl.lastIndexOf("."));

        //原图需要截取
        if (size == null) {
            //原图同时该链接已经是原图
            if (imageSrc.lastIndexOf("_") == -1) {
                return imageSrc + exname;
            }

            String srcSuffix = imageSrc.substring(imageSrc.lastIndexOf("_"));
            if (ImageSize.getByCode(srcSuffix) != null) {
                imageSrc = imageSrc.substring(0, imageSrc.lastIndexOf("_"));
            }

        } else {
            //已经是imagesize的图
            if (imageSrc.endsWith(size.getCode())) {
                return imageSrc + exname;
            }

            if (imageSrc.lastIndexOf("_") != -1) {
                imageSrc = imageSrc.substring(0, imageSrc.lastIndexOf("_"));
            }
            imageSrc = imageSrc + size.getCode();
        }
        return imageSrc + exname;
    }

    public static String genImageByTemplate(String furl, ImageSize size) {

        String exname = "";
        String imageSrc = "";
        if (StringUtil.isEmpty(furl) || furl.lastIndexOf(".") == -1) {
            return "";
        }

        // 取文件扩展名
        String serverPrefix = furl.substring(1, 5); //r001
        exname = furl.substring(furl.lastIndexOf(".")); //.jpg
        imageSrc = furl.substring(0, furl.lastIndexOf("."));

        String returnHeader = "http://" + serverPrefix + "." + WebappConfig.get().getDomain();
        //原图需要截取
        if (size == null) {
            //原图同时该链接已经是原图
            if (imageSrc.lastIndexOf("_") == -1) {
                return returnHeader + imageSrc + exname;
            }

            String srcSuffix = imageSrc.substring(imageSrc.lastIndexOf("_"));
            if (ImageSize.getByCode(srcSuffix) != null) {
                imageSrc = imageSrc.substring(0, imageSrc.lastIndexOf("_"));
            }

        } else {
            //已经是imagesize的图
            if (imageSrc.endsWith(size.getCode())) {
                return returnHeader + imageSrc + exname;
            }

            if (imageSrc.lastIndexOf("_") != -1) {
                imageSrc = imageSrc.substring(0, imageSrc.lastIndexOf("_"));
            }
            imageSrc = imageSrc + size.getCode();
        }
        return returnHeader + imageSrc + exname;
    }

    /**
     * 得到100*100图
     *
     * @param src
     * @return
     */
    public static String parseAudioSs(String src) {
        if (src.lastIndexOf(".") == -1) {
            return src;
        }

        String suffix = src.substring(src.lastIndexOf("."));
        String prefix = getPraseAudioImagPrefix(src);

        return prefix + "_1" + suffix;
    }

    /**
     * 得到50*50图
     *
     * @param src
     * @return
     */
    public static String parseAudioS(String src) {
        if (src.lastIndexOf(".") == -1) {
            return src;
        }

        String prefix = getPraseAudioImagPrefix(src);
        String suffix = src.substring(src.lastIndexOf("."));
        return prefix + "_3" + suffix;
    }

    /**
     * 得到185*185图
     *
     * @param src
     * @return
     */
    public static String parseAudioM(String src) {
        if (src.lastIndexOf(".") == -1) {
            return src;
        }
        String suffix = src.substring(src.lastIndexOf("."));
        String prefix = getPraseAudioImagPrefix(src);

        return prefix + "_2" + suffix;
    }

    /**
     * 得到原图
     *
     * @param src
     * @return
     */
    public static String parseAudioB(String src) {
        if (src.lastIndexOf(".") == -1) {
            return src;
        }
        String suffix = src.substring(src.lastIndexOf("."));
        String prefix = getPraseAudioImagPrefix(src);
        return prefix + suffix;

    }

    private static String getPraseAudioImagPrefix(String src) {
        String prefix = src.substring(0, src.lastIndexOf("."));

        if (prefix.endsWith("_1") || prefix.endsWith("_2") || prefix.endsWith("_3")) {
            prefix = prefix.substring(0, src.lastIndexOf("_"));
        }
        return prefix;
    }

}
