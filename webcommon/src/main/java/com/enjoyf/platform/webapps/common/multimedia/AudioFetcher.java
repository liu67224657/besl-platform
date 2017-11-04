package com.enjoyf.platform.webapps.common.multimedia;

import com.enjoyf.platform.webapps.common.util.UrlUtil;
import com.enjoyf.platform.webapps.common.encode.EncodeUtils;
import com.enjoyf.platform.webapps.common.encode.JsonBinder;

import java.io.IOException;

/**
 * <p/>
 * Description:
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>liuhao</a>
 */
public class AudioFetcher {
    private static final int PAGE_SIZE=5;

    private static String getAudioSearchUrl(String code, int page) {
        StringBuffer sb = new StringBuffer("http://kuang.xiami.com/app/nineteen/search/key/");
        sb.append(code).append("/logo/1/page/").append(page).append("?num=").append(PAGE_SIZE);
        return sb.toString();
    }

    /**
     *
     * @param code
     * @param page
     * @return
     * @throws IOException
     */
    public static AudioInfo returnAudioByJson(String code, int page) throws IOException {
        String jsonStr= UrlUtil.retrieveURL(getAudioSearchUrl(EncodeUtils.urlEncode(code), page));
        AudioInfo info=JsonBinder.buildNormalBinder().fromJson(jsonStr,AudioInfo.class);
        return info;
    }

}
