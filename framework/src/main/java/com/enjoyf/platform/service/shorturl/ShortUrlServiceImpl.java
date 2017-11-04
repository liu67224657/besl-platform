/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.shorturl;

import com.enjoyf.platform.io.RPacket;
import com.enjoyf.platform.io.WPacket;
import com.enjoyf.platform.props.EnvConfig;
import com.enjoyf.platform.service.event.Event;
import com.enjoyf.platform.service.service.ReqProcessor;
import com.enjoyf.platform.service.service.Request;
import com.enjoyf.platform.service.service.ServiceConfig;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.sql.ObjectField;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a> ,zx
 * Create time: 11-8-21 下午4:18
 * Description:
 */
class ShortUrlServiceImpl implements ShortUrlService {
    private ReqProcessor reqProcessor = null;
    private int numOfPartitions;

    public ShortUrlServiceImpl(ServiceConfig scfg) {
        if (scfg == null) {
            throw new RuntimeException("ShortUrlServiceImpl.ctor: ServiceConfig is null!");
        }

        //
        reqProcessor = scfg.getReqProcessor();
        numOfPartitions = EnvConfig.get().getServicePartitionNum(ShortUrlConstants.SERVICE_SECTION);
    }

    @Override
    public ShortUrl generateUrl(String url, String uno) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(url);
        wp.writeString(uno);

        Request req = new Request(ShortUrlConstants.SHORTURL_GENERATE_ONE, wp);

        RPacket rp = reqProcessor.process(req);

        return (ShortUrl) rp.readSerializable();
    }

    @Override
    public Map<String, ShortUrl> generateUrls(Set<String> urls, String uno) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable((Serializable) urls);
        wp.writeString(uno);

        Request req = new Request(ShortUrlConstants.SHORTURL_GENERATE_BATCH, wp);

        RPacket rp = reqProcessor.process(req);

        return (Map<String, ShortUrl>) rp.readSerializable();
    }

    @Override
    public ShortUrl getUrl(String key) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeString(key);

        Request req = new Request(ShortUrlConstants.SHORTURL_GET, wp);
        req.setPartition(Math.abs(key.hashCode()) % numOfPartitions);

        RPacket rp = reqProcessor.process(req);

        return (ShortUrl) rp.readSerializable();
    }

    @Override
    public Map<String, ShortUrl> queryUrls(Set<String> keys) throws ServiceException {
        Map<String, ShortUrl> returnValue = new HashMap<String, ShortUrl>();

        //
        Map<Integer, Set<String>> queryMap = mapUnoList(keys);

        //loop to query the contents.
        for (Map.Entry<Integer, Set<String>> entry : queryMap.entrySet()) {
            returnValue.putAll(queryUrls(entry.getKey(), entry.getValue()));
        }

        return returnValue;
    }

    private Map<Integer, Set<String>> mapUnoList(Set<String> keys) {
        Map<Integer, Set<String>> returnValue = new HashMap<Integer, Set<String>>();

        for (String uno : keys) {
            int idx = Math.abs(uno.hashCode()) % numOfPartitions;

            Set<String> list = returnValue.get(idx);
            if (list == null) {
                list = new HashSet<String>();

                returnValue.put(idx, list);
            }

            list.add(uno);
        }

        return returnValue;
    }

    private Map<String, ShortUrl> queryUrls(Integer idx, Set<String> keys) throws ServiceException {
        WPacket wp = new WPacket();

        wp.writeSerializable((Serializable) keys);

        Request req = new Request(ShortUrlConstants.SHORTURL_QUERY, wp);
        req.setPartition(idx);

        RPacket rp = reqProcessor.process(req);

        return (Map<String, ShortUrl>) rp.readSerializable();
    }

    @Override
    public boolean updateShortUrl(String key, Map<ObjectField, Object> keyValues) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeString(key);
        wp.writeSerializable((Serializable) keyValues);

        Request req = new Request(ShortUrlConstants.SHORTURL_UPDATE, wp);
        req.setPartition(Math.abs(key.hashCode()) % numOfPartitions);

        RPacket rp = reqProcessor.process(req);

        return rp.readBooleanNx();
    }

    @Override
    public boolean receiveEvent(Event event) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(event);

        Request req = new Request(ShortUrlConstants.RECIEVE_EVENT, wp);
        req.setPartition(Math.abs(event.hashCode()) % numOfPartitions);

        RPacket rp = reqProcessor.process(req);

        return rp.readBooleanNx();
    }
}
