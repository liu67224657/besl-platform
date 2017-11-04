package com.enjoyf.platform.webapps.common.html.component.fileupload;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.apache.commons.fileupload.ProgressListener;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Auther: <a mailto:yinpengyi@platform.com>Yin Pengyi</a>
 */
public class FileUploadProgressManager {

    private static FileUploadProgressManager instance;

    private Cache<String, FileUploadProgressListener> progressListenersCache;

    private FileUploadProgressManager() {

        progressListenersCache = CacheBuilder.newBuilder()
                .concurrencyLevel(8)
                .expireAfterWrite(30l, TimeUnit.MINUTES).build();
//        progressListenersCache = new MapMaker().concurrencyLevel(8).exp(
//                3, TimeUnit.SECONDS).makeMap();
    }

    public static synchronized FileUploadProgressManager get() {
        if (instance == null) {
            instance = new FileUploadProgressManager();
        }

        return instance;
    }

    public ProgressListener createProgressListener(final HttpServletRequest request) {
        FileUploadProgressListener listener = new FileUploadProgressListener(request);

        progressListenersCache.put(listener.getSessionKey(), listener);

        return listener;
    }

    public Map<Integer, FileUploadProgress> getFileUploadProgresses(final HttpServletRequest request) {
        Map<Integer, FileUploadProgress> returnValue = new HashMap<Integer, FileUploadProgress>();

        FileUploadProgressListener listener = progressListenersCache.getIfPresent(request.getSession(true).getId());

        if (listener != null) {
            returnValue.putAll(listener.getFileProgresses());
        }

        return returnValue;
    }
}
