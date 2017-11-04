package com.enjoyf.platform.props.hotdeploy;

import com.enjoyf.platform.props.EnvConfig;
import com.enjoyf.platform.service.viewline.ViewCategoryAspect;
import com.enjoyf.platform.service.viewline.ViewCategoryConfig;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.reflect.ReflectUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <a href="mailto:taijunli@staff.com">Li Taijun</a>
 */
public class ViewLineHotdeployConfig extends HotdeployConfig {
    //
    private static final Logger logger = LoggerFactory.getLogger(ViewLineHotdeployConfig.class);

    ////////////////////////////////////////////////////////////////
    private final static String KEY_VIEW_CATEGORY_ASPECT_LIST = "view.cate.aspect.list";

    private final static String PREFFIX_KEY_VIEW_CATEGORY_ASPECT = "view.cate.aspect.";

    private final static String SUFFIX_KEY_SINGLE_LINE = ".single.line";
    private final static String SUFFIX_KEY_OPEN_TO_PUBLIC = ".open.to.public";

    private final static String SUFFIX_KEY_LOCATION_LIST = ".location.list";

    private final static String SUFFIX_KEY_LOCATION_NAME = ".name";
    private final static String SUFFIX_KEY_LOCATION_DESCRIPTION = ".description";
    private final static String SUFFIX_KEY_LOCATION_ITEM_TYPE = ".item.type";
    private final static String SUFFIX_KEY_LOCATION_ITEM_MIN_COUNT = ".item.min.count";


    //
    private ViewLinePropsCache viewLinePropsCache;

    public ViewLineHotdeployConfig() {
        super(EnvConfig.get().getViewLineHotdeployConfigureFile());
    }

    @Override
    public void init() {
        reload();
    }

    @Override
    public void reload() {
        super.reload();

        //
        ViewLinePropsCache tempCache = new ViewLinePropsCache();

        //
        List<String> aspectCodes = this.getList(KEY_VIEW_CATEGORY_ASPECT_LIST);
        for (String aspectCode : aspectCodes) {
            ViewCategoryAspect aspect = ViewCategoryAspect.getByCode(aspectCode);

            //
            if (aspect != null) {
                ViewCategoryConfig config = new ViewCategoryConfig(aspect);

                //
                config.setOpenToPublic(this.getBoolean(PREFFIX_KEY_VIEW_CATEGORY_ASPECT + aspectCode + SUFFIX_KEY_OPEN_TO_PUBLIC, false));

                //
                tempCache.getCategoryAspectConfigs().put(config.getCategoryAspect(), config);
            } else {
                GAlerter.lab("There is some configure error in the ViewLineHotdeployConfig, aspectCode:" + aspectCode);
            }
        }

        //
        this.viewLinePropsCache = tempCache;

        logger.info("ViewLineHotdeployConfig Props init finished." + this.toString());
    }

    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }

    public ViewCategoryConfig getCategoryAspectConfig(ViewCategoryAspect aspect) {
        return viewLinePropsCache.getCategoryAspectConfigs().get(aspect);
    }

    private class ViewLinePropsCache {
        private Map<ViewCategoryAspect, ViewCategoryConfig> categoryAspectConfigs = new HashMap<ViewCategoryAspect, ViewCategoryConfig>();

        public Map<ViewCategoryAspect, ViewCategoryConfig> getCategoryAspectConfigs() {
            return categoryAspectConfigs;
        }

        public void setCategoryAspectConfigs(Map<ViewCategoryAspect, ViewCategoryConfig> categoryAspectConfigs) {
            this.categoryAspectConfigs = categoryAspectConfigs;
        }
    }
}
