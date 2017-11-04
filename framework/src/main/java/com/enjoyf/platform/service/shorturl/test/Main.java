/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.shorturl.test;

import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.shorturl.ShortUrl;
import com.enjoyf.platform.service.shorturl.ShortUrlServiceSngl;

import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-10-14 下午1:41
 * Description:
 */
public class Main {
    public static void main(String[] args) {
        Map<String, ShortUrl> urls = new HashMap<String, ShortUrl>();

        try {
            for (int i = 0; i < 10; i++) {
                ShortUrl url = ShortUrlServiceSngl.get().generateUrl("http://www.joyme.com/" + System.currentTimeMillis() + "/" + i, "xxssxxsxx");

                if (url != null) {
                    urls.put(url.getUrlKey(), url);
                }
            }

            for (int i = 0; i < 10; i++) {
                ShortUrlServiceSngl.get().queryUrls(urls.keySet());
            }
        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }
}
