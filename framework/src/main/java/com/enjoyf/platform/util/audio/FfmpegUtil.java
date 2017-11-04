package com.enjoyf.platform.util.audio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  14-4-11 上午9:55
 * Description:
 */
public class FfmpegUtil {
    private static Logger logger = LoggerFactory.getLogger(FfmpegUtil.class);

    private static final String CMD_FFMPEG = "ffmpeg -i ";

    public static String runProcess(String src, String destSrc) throws IOException {
        Runtime runtime = Runtime.getRuntime();
        String cmd = CMD_FFMPEG + " " + src + " " + destSrc;
        if (logger.isDebugEnabled()) {
            logger.debug("ffmpeg cmd is:" + cmd);
        }

        runtime.exec(cmd);
        return destSrc;
    }

    public static String runMp3ToAmrProcess(String src, String destSrc) throws IOException {
        Runtime runtime = Runtime.getRuntime();
        String cmd = CMD_FFMPEG + " " + src + " -ac 1 -ar 8000 -ab 12.2k " + destSrc;
        if (logger.isDebugEnabled()) {
            logger.debug("ffmpeg cmd is:" + cmd);
        }

        runtime.exec(cmd);
        return destSrc;
    }

}
