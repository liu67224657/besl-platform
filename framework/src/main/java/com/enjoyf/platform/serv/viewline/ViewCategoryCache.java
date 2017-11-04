/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.serv.viewline;

import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.viewline.ViewCategory;
import com.enjoyf.platform.service.viewline.ViewCategoryAspect;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.log.GAlerter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-9-25 下午5:18
 * Description:
 */
public class ViewCategoryCache {
    //
    private Map<Integer, ViewCategory> categoryMap = new HashMap<Integer, ViewCategory>();

    private ViewLineLogic logic;

    public ViewCategoryCache(ViewLineLogic logic) {
        this.logic = logic;

        //load the treen into the categoryMap.
        loadTree(categoryMap);

        //start the reload timer.
        Timer reloadTimer = new Timer();
        reloadTimer.scheduleAtFixedRate(new CategoryTreeReloadTimer(), 300 * 1000, 300 * 1000);
    }

    private void loadTree(Map<Integer, ViewCategory> categoryMap) {
        //
        for (ViewCategoryAspect aspect : ViewCategoryAspect.getAll()) {
            try {
                addCategories(categoryMap, logic.queryCategoryTreeByAspectParent(aspect, 0));
            } catch (ServiceException e) {
                GAlerter.lab("ViewCategoryCache loadTree error, aspect:" + aspect, e);
            }
        }
    }

    private void addCategories(Map<Integer, ViewCategory> categoryMap, List<ViewCategory> categories) {
        if (!CollectionUtil.isEmpty(categories)) {
            //
            for (ViewCategory category : categories) {
                //
                categoryMap.put(category.getCategoryId(), category);

                if (!CollectionUtil.isEmpty(category.getChildrenCategories())) {
                    addCategories(categoryMap, category.getChildrenCategories());
                }
            }
        }
    }

    public ViewCategory getCategory(int categoryId) {
        return categoryMap.get(categoryId);
    }

    class CategoryTreeReloadTimer extends TimerTask {
        /**
         * The action to be performed by this timer task.
         */
        @Override
        public void run() {
            //
            Map<Integer, ViewCategory> tempCategoryMap = new HashMap<Integer, ViewCategory>();

            //load the tree inint tempCategoryMap;
            loadTree(tempCategoryMap);

            //switch the temp map and the been using map.
            synchronized (categoryMap) {
                categoryMap = tempCategoryMap;
            }
        }
    }
}
