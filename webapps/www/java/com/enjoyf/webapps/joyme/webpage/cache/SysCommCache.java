package com.enjoyf.webapps.joyme.webpage.cache;

import com.enjoyf.platform.service.misc.MiscServiceSngl;
import com.enjoyf.platform.service.misc.Region;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.CollectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 系统缓存
 *
 * @author zx
 */
public class SysCommCache {
    private static Logger logger = LoggerFactory.getLogger(SysCommCache.class);

    private static SysCommCache instance;

    //the region cache.
    private static Map<Integer, Region> regionMap = new LinkedHashMap<Integer, Region>();
    private static Map<Integer, Map<Integer, Region>> cityMap = new LinkedHashMap<Integer, Map<Integer, Region>>();

    public static synchronized SysCommCache get() {
        if (instance == null) {
            instance = new SysCommCache();
        }

        return instance;
    }

    private SysCommCache() {
        load();
    }

    // 初始化
    private void load() {
        try {
            List<Region> regionList = MiscServiceSngl.get().getAllRegions();

            for (Region region : regionList) {
                regionMap.put(region.getRegionId(), region);
            }
        } catch (ServiceException e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * 查询区域信息列表
     *
     * @return list
     */
    public Map<Integer, Region> getRegionMap() {
        return regionMap;
    }

    public Map<Integer, Map<Integer, Region>> getCityMap() {
        return cityMap;
    }

    public Map<Integer, Region> getCityIdByParentid(Integer parentId) {
        if (CollectionUtil.isEmpty(regionMap.values())) {
            load();
        }

        //包含城市列表直接返回
        if (cityMap.containsKey(parentId)) {
            return cityMap.get(parentId);
        }


        Map<Integer, Region> returnMap = new LinkedHashMap<Integer, Region>();
        for (Map.Entry<Integer, Region> entry : regionMap.entrySet()) {
            Region region = entry.getValue();
            if (region.getParentRegionId().equals(parentId)) {
                returnMap.put(region.getRegionId(), region);
            }
        }
        if (!CollectionUtil.isEmpty(returnMap.values())) {
            cityMap.put(parentId, returnMap);
        }
        return returnMap;
    }

    /**
     * 通过ID查询城市信息
     *
     * @param id 城市ID
     * @return string 城市名称
     */
    public String findRegionById(Integer id) {
        if (CollectionUtil.isEmpty(regionMap.values())) {
            load();
        }

        if (!regionMap.containsKey(id)) {
            return "";
        }

        return regionMap.get(id).getRegionName();
    }
}
