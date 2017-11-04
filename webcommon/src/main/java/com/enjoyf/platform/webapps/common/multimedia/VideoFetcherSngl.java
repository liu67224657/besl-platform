package com.enjoyf.platform.webapps.common.multimedia;

import com.enjoyf.platform.service.content.ContentServiceException;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.webapps.common.multimedia.videofetcher.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * <p/>
 * Description:
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>liuhao</a>
 */
public class VideoFetcherSngl {
    private static Logger logger = LoggerFactory.getLogger(VideoFetcherSngl.class);

    private Map<VideoProvider, VideoFetcher> fetcherMap = new HashMap<VideoProvider, VideoFetcher>();

    private static VideoFetcherSngl instance = null;

    private VideoFetcherSngl() {
        fetcherMap.put(VideoProvider.PROVIDER_KU6, new Ku6Fetcher());
        fetcherMap.put(VideoProvider.PROVIDER_56, new Open56Fetcher());
        fetcherMap.put(VideoProvider.PROVIDER_YOUKU, new YoukuFetcher());
        fetcherMap.put(VideoProvider.PROVIDER_TUDOU, new TudouFetcher());
        fetcherMap.put(VideoProvider.PROVIDER_SINA, new SinaVideoFetcher());
        fetcherMap.put(VideoProvider.PROVIDER_SOHU, new SohuVideoFetcher());
        fetcherMap.put(VideoProvider.PROVIDER_BILIBILI, new BilibiliVideoFetcher());
        fetcherMap.put(VideoProvider.PROVIDER_AIPAI, new AipaiVideoFetcher());
    }

    public static synchronized VideoFetcherSngl get() {
        if (instance == null) {
            instance = new VideoFetcherSngl();
        }
        return instance;
    }

    public Video getVideoByUrl(String url, Map<String, String> paramMap) throws ServiceException {
        Video video = null;

        try {
            if (url.indexOf("v.youku.com") != -1) {
                video = getVideoByProvider(VideoProvider.PROVIDER_YOUKU, url, paramMap);
            } else if (url.indexOf("tudou.com") != -1) {
                video = getVideoByProvider(VideoProvider.PROVIDER_TUDOU, url, paramMap);
            } else if (url.indexOf("v.ku6.com") != -1) {
                video = getVideoByProvider(VideoProvider.PROVIDER_KU6, url, paramMap);
            } else if (url.indexOf("56.com") != -1) {
                video = getVideoByProvider(VideoProvider.PROVIDER_56, url, paramMap);
            } else if (url.indexOf("sina.com") != -1) {
                video = getVideoByProvider(VideoProvider.PROVIDER_SINA, url, paramMap);
            } else if (url.indexOf("sohu.com") != -1){
                video = getVideoByProvider(VideoProvider.PROVIDER_SOHU, url, paramMap);
            } else if (url.indexOf("bilibili.") != -1) {
                video = getVideoByProvider(VideoProvider.PROVIDER_BILIBILI, url, paramMap);
            } else if (url.indexOf("aipai.com") != -1){
                video = getVideoByProvider(VideoProvider.PROVIDER_AIPAI, url, paramMap);
            } else {
                throw new ServiceException(ContentServiceException.BLOG_CONTENT_VIDEO_NOT_APPLY, "is not apply.url:" + url);
            }
        } catch (Exception e) {
            logger.error(" getVideoByUrl occured Exception.url" + url + ",e:", e);
        }

        return video;
    }

    public Video getVideoByProvider(VideoProvider videoProvider, String url, Map<String, String> paramMap) throws Exception {
        VideoFetcher fetcher = fetcherMap.get(videoProvider);
        return fetcher.fetchByUrl(url, paramMap);
    }

    public static void main(String[] args) {
        try {
            Video video = VideoFetcherSngl.get().getVideoByProvider(VideoProvider.PROVIDER_56, "http://www.56.com/u47/v_NjQ5OTc2Mjg.html", null);

            System.out.println("视频缩略图：" + video.getPic());
            System.out.println("视频地址：" + video.getFlash());
            System.out.println("视频时长：" + video.getTime());
            System.out.println("视频描述：" + video.getDescription());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
