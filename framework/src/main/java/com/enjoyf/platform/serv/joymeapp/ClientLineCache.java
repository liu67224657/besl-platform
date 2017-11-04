/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.serv.joymeapp;

import com.enjoyf.platform.service.joymeapp.ClientLine;
import com.enjoyf.platform.service.joymeapp.ClientLineItem;
import com.enjoyf.platform.util.memcached.MemCachedConfig;
import com.enjoyf.platform.util.memcached.MemCachedManager;

import java.util.List;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-9-25 下午5:18
 * Description:
 */
class ClientLineCache {

    private static final long TIME_OUT_SEC = 60l * 60l * 6l;

    private static final String PREFIX_CODE_TO_ITEM = "joyme_app_code2item_";
    private static final String PREFIX_CODE_TO_LINE = "joyme_app_code2line_";
    private static final String PREFIX_CODE_TO_CUSTOM = "joyme_app_code2custom_";    //自定义缓存,暂时用于热门页，游戏分类接口，added by tony

    private MemCachedConfig config;

    private MemCachedManager manager;

    ClientLineCache(MemCachedConfig config) {
        this.config = config;
        manager = new MemCachedManager(config);
    }

    //////////////////////////////////////////////////////////
    public void putClientItem(long lineId, List<ClientLineItem> lineItemList) {
        manager.put(PREFIX_CODE_TO_ITEM + lineId, lineItemList, TIME_OUT_SEC);
    }

    public List<ClientLineItem> getClientItem(long lineId) {
        Object archiveObj = manager.get(PREFIX_CODE_TO_ITEM + lineId);
        if (archiveObj == null) {
            return null;
        }
        return (List<ClientLineItem>) archiveObj;
    }

    public boolean removeClientItem(long lineId) {
        return manager.remove(PREFIX_CODE_TO_ITEM + lineId);
    }

    public void putClientLine(ClientLine clientLine) {
        manager.put(PREFIX_CODE_TO_LINE + clientLine.getCode(), clientLine, TIME_OUT_SEC);
    }

    public ClientLine getClientLine(String lineCode) {
        Object archiveObj = manager.get(PREFIX_CODE_TO_LINE + lineCode);
        if (archiveObj == null) {
            return null;
        }
        return (ClientLine) archiveObj;
    }

    public boolean removeClientLine(String lineCode) {
        return manager.remove(PREFIX_CODE_TO_LINE + lineCode);
    }

    public void putClientCustom(String code, List list) {
        manager.put(PREFIX_CODE_TO_CUSTOM + code, list, TIME_OUT_SEC);
    }

    public List getClientCustom(String code) {
        Object archiveObj = manager.get(PREFIX_CODE_TO_CUSTOM + code);
        if (archiveObj == null) {
            return null;
        }
        return (List) archiveObj;
    }

    public boolean removeClientCustom(String code) {
        return manager.remove(PREFIX_CODE_TO_CUSTOM + code);
    }


}
