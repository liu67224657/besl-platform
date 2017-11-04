package com.enjoyf.platform.webapps.common.html.component.fileupload;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.ProgressListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @Auther: <a mailto:yinpengyi@platform.com>Yin Pengyi</a>
 */
public class FileUploadProgressListener implements ProgressListener {
	
	private static final Logger logger = LoggerFactory.getLogger(FileUploadProgressListener.class);
	
    private String sessionKey;
    private Map<Integer, FileUploadProgress> fileProgresses = new HashMap<Integer, FileUploadProgress>();

    FileUploadProgressListener(final HttpServletRequest req) {
        sessionKey = req.getSession(true).getId();
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public Map<Integer, FileUploadProgress> getFileProgresses() {
        return fileProgresses;
    }

    public void update(long l, long l1, int i) {
        if (i < 1) {
            return;
        }

        if (logger.isDebugEnabled()) {
        	logger.debug("FileUploadProgressListener recieve the update info, totalSize:" + l1 + ", curSize:" + l + ", fileIdx:" + i);
        }

        synchronized (fileProgresses) {
            FileUploadProgress progress = fileProgresses.get(i);
            if (progress == null) {
                progress = new FileUploadProgress(l, l1);

                fileProgresses.put(i, progress);
            }

            progress.setCurSize(l);
            progress.setTotalSize(l1);
        }
    }
}
