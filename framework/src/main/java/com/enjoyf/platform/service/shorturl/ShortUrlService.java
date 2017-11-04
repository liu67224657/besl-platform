/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.shorturl;

import com.enjoyf.platform.service.event.EventReceiver;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.service.rpc.RPC;
import com.enjoyf.platform.util.sql.ObjectField;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-10-13 下午6:56
 * Description:
 */
public interface ShortUrlService extends EventReceiver{

    //
    public ShortUrl generateUrl(String url, String uno) throws ServiceException;

    //
    public Map<String, ShortUrl> generateUrls(Set<String> urls, String uno) throws ServiceException;

    //
    public ShortUrl getUrl(String key) throws ServiceException;

    //
    public Map<String, ShortUrl> queryUrls(Set<String> keys) throws ServiceException;

    //
    public boolean updateShortUrl(String key, Map<ObjectField, Object> keyValues) throws ServiceException;

}
