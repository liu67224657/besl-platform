package com.enjoyf.platform.webapps.common.html.component.fileupload;

import com.enjoyf.platform.util.reflect.ReflectUtil;

/**
 * @Auther: <a mailto:yinpengyi@platform.com>Yin Pengyi</a>
 */
public class FileUploadProgress {
    private long startTime = System.currentTimeMillis();
    private long totalSize = -1;
    private long curSize = 0;

    //////////////////////////////////////////////////
    public FileUploadProgress() {
    }

    public FileUploadProgress(long curSize, long totalSize) {
        this.curSize = curSize;
        this.totalSize = totalSize;
    }

    //////////////////////////////////////////////////
    public long getStartTime() {
        return startTime;
    }

    public long getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(long totalSize) {
        this.totalSize = totalSize;
    }

    public long getCurSize() {
        return curSize;
    }

    public void setCurSize(long curSize) {
        this.curSize = curSize;
    }

    //byte/secs
    public long getRate() {
        long returnValue = 0;

        long time = System.currentTimeMillis() - startTime;
        if (time < 0) {
            time = 100;
        }

        returnValue = (curSize * 1000) / time;

        return returnValue;
    }

    //msecs
    public long getRemainTime() {
        if (totalSize < 0) {
            return -1;
        }

        //
        long returnValue = 0;
        long rate = getRate();
        if (rate < 1) {
            rate = 1;
        }

        returnValue = ((totalSize - curSize) * 1000) / rate;

        return returnValue;
    }

    //////////////////////////////////////////////////
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
