package com.enjoyf.platform.serv.gameres;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.gameres.GameResource;
import com.enjoyf.platform.service.gameres.GameResourceField;
import com.enjoyf.platform.service.gameres.ResourceDomain;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.timer.FixedRateTimerManagerPool;
import com.enjoyf.platform.util.timer.TimerTasker;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: <a mailto="ericLiu@stuff.enjoyfound.com">ericliu</a>
 * Create time: 12-9-21
 * Description:
 */
public class GameResourceCache {

    //
    private Map<Long, GameResource> cacheMap = new HashMap<Long, GameResource>();

    private GameResourceLogic logic;

    public GameResourceCache(GameResourceLogic logic) {
        this.logic = logic;

        //load the treen into the categoryMap.
        loadGameResource();

        //start the reload timer.
        FixedRateTimerManagerPool.get().addTask(new TimerTasker() {
            @Override
            public void run() {
                loadGameResource();
            }
        }, 5L * 60L * 1000L, "reloadGameResourceCache");
    }

    private void loadGameResource() {
        Map<Long, GameResource> map = new HashMap<Long, GameResource>();
//        try {
//            QueryExpress queryExpress = new QueryExpress();
//            queryExpress.add(QueryCriterions.eq(GameResourceField.REMOVESTATUS, ActStatus.UNACT.getCode()));
//            queryExpress.add(QueryCriterions.eq(GameResourceField.RESOURCEDOMAIN, ResourceDomain.GROUP.getCode()));
//
//            List<GameResource> gameResourceList = logic.queryGameResources(queryExpress);
//
//            for (GameResource gameResource : gameResourceList) {
//                map.put(gameResource.getResourceId(), gameResource);
//            }
//
//        } catch (ServiceException e) {
//            GAlerter.lab("loadGameResource occured ServiceException e: ", e);
//        }

        this.cacheMap = map;
    }

    public Map<Long, GameResource> getCacheMap() {
        return cacheMap;
    }

}
