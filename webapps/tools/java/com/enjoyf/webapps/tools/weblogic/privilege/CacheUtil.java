/**
 *
 */
package com.enjoyf.webapps.tools.weblogic.privilege;

import com.enjoyf.platform.props.WebappConfig;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.tools.PrivilegeResource;
import com.enjoyf.platform.service.tools.ToolsServiceSngl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zx
 */
public class CacheUtil {

    public static final String TOOLS_COOKIEKEY_UID = "t_jm_uid";
    public static final String TOOLS_COOKIEKEY_MESSAGE = "t_jm_message";
    public static final String TOOLS_COOKIEKEY_ENCRYPT = "t_jm_encrypt";
    public static final String TOOLS_COOKIEKEY_SECRET_KEY = "yh87&sw2";
    public static final String TOOLS_COOKIEKEY_SECRET_KEY_DEV = "7ejw!9d#";
    public static final String TOOLS_COOKIEKEY_SECRET_KEY_ALPHA = "8F5&JL3";
    public static final String TOOLS_COOKIEKEY_SECRET_KEY_BETA = "#4g%klwe";

    public static long MEM_CACHE_TIME_OUT = 7L*24L*60L*60L;
    private static Map mapSysRs = null;
    private static Map mapSysRsUrlO = null;

    static {
        mapSysRs = new HashMap();
        mapSysRsUrlO = new HashMap();
    }

    /**
     * 从缓存中取系统资源信息,key: id, value:Object
     */
    public static Map getSysRs() {
        if (mapSysRs.isEmpty())
            createSysRsCache();
        return mapSysRs;
    }

    /**
     * 从缓存中取系统资源信息,key: url, value:Object
     */
    public static Map getSysRsUrlO() {
        if (mapSysRsUrlO.isEmpty())
            createSysRsCache();
        return mapSysRsUrlO;
    }

    /**
     * 创建系统资源缓存
     */
    public static void createSysRsCache() {

        List<PrivilegeResource> tempRes = null;
        try {
            tempRes = ToolsServiceSngl.get().queryAllResByStatus(ActStatus.ACTED);
        } catch (ServiceException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        for (PrivilegeResource entity : tempRes) {
            mapSysRs.put(entity.getRsid(), entity);
            mapSysRsUrlO.put(entity.getRsurl(), entity);
        }
    }

    /**
     * 清空系统资源的缓存
     */
    public static void clearSysRsCache() {
        if (!mapSysRs.isEmpty())
            mapSysRs.clear();
    }


    /**
     * 清空缓存
     */
    public static void clearCache() {
        clearSysRsCache();

    }

    /**
     * 获得系统资源的实体 根据资源URL
     *
     * @return
     */
    public static PrivilegeResource getSysRsByURL(String url) {
        if (mapSysRsUrlO.isEmpty())
            createSysRsCache();

        Object o = mapSysRsUrlO.get(url);
        if (o == null) {
            if (url.startsWith("/")) {
                url = url.substring(1);
                o = mapSysRsUrlO.get(url);
            } else {
                o = mapSysRsUrlO.get("/"+url);
            }
        }

        return (PrivilegeResource) o;
    }

    /**
     * 获得系统资源的实体 根据资源ID
     *
     * @return
     */
    public static PrivilegeResource getSysRsByURL(Integer id) {
        if (mapSysRs.isEmpty())
            createSysRsCache();
        return (PrivilegeResource) mapSysRs.get(id);
    }

    public static String getToolsCookeySecretKey() {
        if (WebappConfig.get().DOMAIN.contains("dev") || WebappConfig.get().DOMAIN.contains("test")) {
            return TOOLS_COOKIEKEY_SECRET_KEY_DEV;
        } else if (WebappConfig.get().DOMAIN.contains("alpha")) {
            return TOOLS_COOKIEKEY_SECRET_KEY_ALPHA;
        } else if (WebappConfig.get().DOMAIN.contains("beta")) {
            return TOOLS_COOKIEKEY_SECRET_KEY_BETA;
        }
        return TOOLS_COOKIEKEY_SECRET_KEY;
    }


    public static void putToolsUserInfoCache(String cookieKey, ToolsUserInfo toolsUserInfo) {
        WebappConfig.get().getMemCacheManager().put(cookieKey, toolsUserInfo, MEM_CACHE_TIME_OUT);
    }

    public static ToolsUserInfo getToolsUserInfoCache(String cookieKey) {
        Object toolsUserInfo = WebappConfig.get().getMemCacheManager().get(cookieKey);
        if(toolsUserInfo == null){
            return null;
        }
        return (ToolsUserInfo) toolsUserInfo;
    }

    public static void removeToolsUserInfoCache(String cookieKey) {
        WebappConfig.get().getMemCacheManager().remove(cookieKey);
    }
}
