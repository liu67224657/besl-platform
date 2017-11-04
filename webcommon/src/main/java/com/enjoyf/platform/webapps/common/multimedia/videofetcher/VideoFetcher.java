package com.enjoyf.platform.webapps.common.multimedia.videofetcher;

import com.enjoyf.platform.webapps.common.multimedia.Video;

import java.util.Map;

/**
 * <p/>
 * Description:
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>liuhao</a>
 */
public interface VideoFetcher {

    public Video fetchByUrl(String url, Map<String, String> paramMap) throws Exception;
}
