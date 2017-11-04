package com.enjoyf.platform.props.hotdeploy;

import com.enjoyf.platform.props.EnvConfig;
import com.enjoyf.platform.service.content.ContentType;
import com.enjoyf.platform.service.content.magazine.MagazineConfig;
import com.enjoyf.platform.service.content.wall.WallBlockStyleConfig;
import com.enjoyf.platform.service.content.wall.WallBlockType;
import com.enjoyf.platform.service.content.wall.WallContentRule;
import com.enjoyf.platform.service.content.wall.WallContentType;
import com.enjoyf.platform.service.content.wall.WallLayoutConfig;
import com.enjoyf.platform.service.content.wall.WallLayoutType;
import com.enjoyf.platform.util.log.GAlerter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: yongmingxu
 * Date: 12-7-6
 * Time: 下午4:55
 * To change this template use File | Settings | File Templates.
 */
public class DisplayHotdeployConfig extends HotdeployConfig {
    private static final Logger logger = LoggerFactory.getLogger(DisplayHotdeployConfig.class);

    // the key of layout display type
    private static final String KEY_LAYOUT_DISPLAY_TYPE = "layout.display.type";

    // the key of block display type
    private static final String KEY_BLOCK_DISPLAY_TYPE = "block.display.type";

    // the key of content display type
    private static final String KEY_CONTENT_DISPLAY_TYPE = "content.display.type";

    // the content checked rules key
    private static final String KEY_CONTENT_SC_CHECKED_RULES = "content.sc.checked.rules";

    private static final String SUFFIX_LAYOUT_CONTAINS_BLOCK = ".contains.block.list";

    private static final String SUFFIX_BLOCK_STYLE = ".style";

    private static final String PREFIX_BLOCK_STYYLE_URLE = "rule.";

    private static final String DEFAULT_SEPARATOR = ".";

    private static final String RULE_SEPARATOR = ":";

    // magazine config
    private static final String KEY_MAGAZINE_DISPLAY_LIST = "magazine.display.list";
    private static final String KEY_MAGAZINE_DISPLAY_CONTENT_TYPE = ".magazine.display.content.type";
    private static final String KEY_MAGAZINE_LAYOUT_DISPLAY_TYPE = ".magazine.layout.display.type";
    private static final String KEY_MAGAZINE_LAYOUT_DISPLAY_TYPE_CHOOSE_RULE = ".magazine.layout.display.type.choose.rule";
    private static final String KEY_MAGAZINE_LAYOUT_DISPLAY_PAGE_SIZE = ".magazine.layout.display.page.size";
    private static final String KEY_MAGAZINE_LAYOUT_DISPLAY_TOTAL_PAGE = ".magazine.layout.display.total.page";
    private static final String KEY_MAGAZINE_LAYOUT_DISPLAY_FTL_PATH = ".magazine.layout.display.ftl.path";
    private static final String KEY_MAGAZINE_BLOCK_DISPLAY_FTL_PATH = ".magazine.block.display.ftl.path";
    private static final String KEY_MAGAZINE_MODEL_AND_VIEW_PATH = ".magazine.model.and.view.path";

    private Cached cached;

    public DisplayHotdeployConfig() {
        super(EnvConfig.get().getDisplayHotdeployConfigureFile());
    }

    @Override
    public void init() {
        if (logger.isDebugEnabled()) {
            logger.debug("DisplayHotdeployConfig Props init start......");
        }

        reload();
    }


    public synchronized void reload() {
        super.reload();

        Cached tmpCache = new Cached();

        //load block type
        loadBlockType(tmpCache);

        //load content type
        loadContentType(tmpCache);

        //load content checked rule
        loadContentCheckedRule(tmpCache);

        //load layout type
        loadLayoutType(tmpCache);

        //load block style and style's rule
        loadBlockStyleAndRule(tmpCache);

        //load magazine config
        loadMagazineConfig(tmpCache);

        cached = tmpCache;

    }

    // load block type
    private void loadBlockType(Cached tmpCache) {
        Map<WallBlockType, WallBlockType> wallBlockTypeMap = new HashMap<WallBlockType, WallBlockType>();

        //get block type list
        List<String> typeList = this.getList(KEY_BLOCK_DISPLAY_TYPE);

        for (String type : typeList) {
            if (WallBlockType.getByCode(type) != null) {
                wallBlockTypeMap.put(WallBlockType.getByCode(type), WallBlockType.getByCode(type));
            } else {
                GAlerter.lab("DisplayHotdeployConfig, there is not exist block type: " + type);
                continue;
            }
        }

        tmpCache.setWallBlockTypeMap(wallBlockTypeMap);
    }

    // load content type
    private void loadContentType(Cached tmpCache) {
        Map<WallContentType, WallContentType> wallContentTypeMap = new HashMap<WallContentType, WallContentType>();

        //get content type list
        List<String> typeList = this.getList(KEY_CONTENT_DISPLAY_TYPE);

        for (String type : typeList) {
            if (WallContentType.getByCode(type) != null) {
                wallContentTypeMap.put(WallContentType.getByCode(type), WallContentType.getByCode(type));
            } else {
                GAlerter.lab("DisplayHotdeployConfig, there is not exist content type: " + type);
                continue;
            }
        }

        tmpCache.setWallContentTypeMap(wallContentTypeMap);
    }

    // load content checked rule
    private void loadContentCheckedRule(Cached tmpCache) {
        Map<WallContentRule, WallContentRule> wallContentCheckedRuleMap = new HashMap<WallContentRule, WallContentRule>();

        //get content checked rule list
        List<String> ruleList = this.getList(KEY_CONTENT_SC_CHECKED_RULES);

        for (String rule : ruleList) {
            if (WallContentRule.getByCode(rule) != null) {
                wallContentCheckedRuleMap.put(WallContentRule.getByCode(rule), WallContentRule.getByCode(rule));
            } else {
                GAlerter.lab("DisplayHotdeployConfig, there is not exist content checked rule: " + rule);
                continue;
            }
        }

        tmpCache.setWallContentCheckedRuleMap(wallContentCheckedRuleMap);

    }

    // load layout type
    private void loadLayoutType(Cached tmpCache) {
        Map<WallLayoutType, WallLayoutConfig> wallLayoutConfigMap = new HashMap<WallLayoutType, WallLayoutConfig>();

        // get the loayout type list
        List<String> layoutList = this.getList(KEY_LAYOUT_DISPLAY_TYPE);

        for (String layout : layoutList) {
            if (WallLayoutType.getByCode(layout) != null) {
                WallLayoutConfig wallLayoutConfig = new WallLayoutConfig();
                wallLayoutConfig.setLayoutType(WallLayoutType.getByCode(layout));

                // get the layout contains block
                List<String> blockTypeList = this.getList(layout + SUFFIX_LAYOUT_CONTAINS_BLOCK);
                List<WallBlockType> wallBlockTypeList = new ArrayList<WallBlockType>();

                for (String blockType : blockTypeList) {
                    if (WallBlockType.getByCode(blockType) != null && tmpCache.getWallBlockTypeMap().get(WallBlockType.getByCode(blockType)) != null) {
                        wallBlockTypeList.add(WallBlockType.getByCode(blockType));
                    } else {
                        GAlerter.lab("DisplayHotdeployConfig, layout: " + layout + "contains a unknown block type :" + blockType);
                        continue;
                    }

                }

                wallLayoutConfig.setWallBlockTypeList(wallBlockTypeList);

                wallLayoutConfigMap.put(wallLayoutConfig.getLayoutType(), wallLayoutConfig);

            } else {
                GAlerter.lab("DisplayHotdeployConfig, there is not exist layout type: " + layout);
                continue;
            }
        }

        tmpCache.setWallLayoutConfigMap(wallLayoutConfigMap);
    }

    // load block style and style's rule
    private void loadBlockStyleAndRule(Cached tmpCache) {
        Map<String, Map<Integer, String>> wallBlockStyleMap = new HashMap<String, Map<Integer, String>>();
        Map<String, WallBlockStyleConfig> wallBlockStyleConfigMap = new HashMap<String, WallBlockStyleConfig>();

        // content type . block type . content checked rule .style
        for (WallContentType wallContentType : tmpCache.getWallContentTypeMap().values()) {
            // block type
            for (WallBlockType wallBlockType : tmpCache.getWallBlockTypeMap().values()) {
                // content check rule
                for (WallContentRule wallContentRule : tmpCache.getWallContentCheckedRuleMap().values()) {
                    String key = wallContentType.getCode() + DEFAULT_SEPARATOR + wallBlockType.getCode() + DEFAULT_SEPARATOR + wallContentRule.getCode();
                    List<String> styleList = this.getList(key + SUFFIX_BLOCK_STYLE);

                    //
                    Map<Integer, String> styleMap = new HashMap<Integer, String>();
                    for (int i = 0; i < styleList.size(); i++) {
                        styleMap.put(i, styleList.get(i));
                        // style's rule
                        String defineKey = PREFIX_BLOCK_STYYLE_URLE + key + DEFAULT_SEPARATOR + styleList.get(i);
                        List<String> ruleDefine = this.getList(defineKey);
                        WallBlockStyleConfig styleConfig = new WallBlockStyleConfig();
                        Map<String, String> ruleMap = new HashMap<String, String>();

                        for (String config : ruleDefine) {
                            // s:30 c:125 br:5 w:187 h:294 img:m
                            ruleMap.put(config.split(RULE_SEPARATOR)[0], config.split(RULE_SEPARATOR)[1]);
                        }

                        if (ruleMap.get("s") != null) {
                            styleConfig.setSubjectLen(Integer.parseInt(ruleMap.get("s")));
                        } else {
                            GAlerter.lab("DisplayHotdeployConfig, style config: s is null in " + defineKey);
                        }

                        if (ruleMap.get("c") != null) {
                            styleConfig.setContentLen(Integer.parseInt(ruleMap.get("c")));
                        } else {
                            GAlerter.lab("DisplayHotdeployConfig, style config: c is null in " + defineKey);
                        }

                        if (ruleMap.get("br") != null) {
                            styleConfig.setBrLen(Integer.parseInt(ruleMap.get("br")));
                        } else {
                            GAlerter.lab("DisplayHotdeployConfig, style config: br is null in " + defineKey);
                        }

                        if (ruleMap.get("w") != null) {
                            styleConfig.setWidth(Integer.parseInt(ruleMap.get("w")));
                        }
                        if (ruleMap.get("h") != null) {
                            styleConfig.setHeight(Integer.parseInt(ruleMap.get("h")));
                        }
                        if (ruleMap.get("img") != null) {
                            styleConfig.setImg(ruleMap.get("img"));
                        }

                        wallBlockStyleConfigMap.put(key + DEFAULT_SEPARATOR + styleList.get(i), styleConfig);

                    }
                    wallBlockStyleMap.put(key, styleMap);

                }
            }
        }

        tmpCache.setWallBlockStyleMap(wallBlockStyleMap);
        tmpCache.setWallBlockStyleConfigMap(wallBlockStyleConfigMap);

    }

    // load magazine config
    private void loadMagazineConfig(Cached tmpCache) {
        Map<String, MagazineConfig> magazineConfigMap = new HashMap<String, MagazineConfig>();

        // magazine display code
        List<String> magazineCodeList = this.getList(KEY_MAGAZINE_DISPLAY_LIST);

        for (String magazineCode : magazineCodeList) {
            MagazineConfig magazineConfig = new MagazineConfig();
            magazineConfig.setMagazineCode(magazineCode);

            // magazine display content type
            List<String> contentTypeList = this.getList(magazineCode + KEY_MAGAZINE_DISPLAY_CONTENT_TYPE);
            Map<String, ContentType> contentTypeMap = new HashMap<String, ContentType>();
            for(String str : contentTypeList){
                if(str.equals("image")){
                    contentTypeMap.put(str,ContentType.getByValue(ContentType.IMAGE));
                }else if(str.equals("video")){
                    contentTypeMap.put(str,ContentType.getByValue(ContentType.VIDEO));
                }else if(str.equals("audio")){
                    contentTypeMap.put(str,ContentType.getByValue(ContentType.AUDIO));
                }else if(str.equals("text")){
                    contentTypeMap.put(str,ContentType.getByValue(ContentType.TEXT));
                }else if(str.equals("app")){
                    contentTypeMap.put(str,ContentType.getByValue(ContentType.APP));
                } else {
                    GAlerter.lab("DisplayHotdeployConfig, magazine " + magazineCode + " there is not exist display content type: " + str);
                }
            }
            magazineConfig.setContentDisplayTypeMap(contentTypeMap);

            // display choose rule
            int chooseRule = this.getInt(magazineCode + KEY_MAGAZINE_LAYOUT_DISPLAY_TYPE_CHOOSE_RULE, 0);
            magazineConfig.setChooseRule(chooseRule);

            // display page size
            int pageSize = this.getInt(magazineCode + KEY_MAGAZINE_LAYOUT_DISPLAY_PAGE_SIZE, 5);
            magazineConfig.setLayoutSize(pageSize);

            // total display page
            int totalPage = this.getInt(magazineCode + KEY_MAGAZINE_LAYOUT_DISPLAY_TOTAL_PAGE, 20);
            magazineConfig.setTotalLayout(totalPage);
            
            // layout display ftl path
            String layoutFtlPath = this.getString(magazineCode + KEY_MAGAZINE_LAYOUT_DISPLAY_FTL_PATH);
            magazineConfig.setLayoutFtlPath(layoutFtlPath);
            
            // block display ftl path
            String blockFtlPath = this.getString(magazineCode + KEY_MAGAZINE_BLOCK_DISPLAY_FTL_PATH);
            magazineConfig.setBlockFtlPath(blockFtlPath);

            // model and view path
            magazineConfig.setModelAndView(this.getString(magazineCode + KEY_MAGAZINE_MODEL_AND_VIEW_PATH));

            // get the magazine layout type list
            List<String> magazineLayoutList = this.getList(magazineCode + KEY_MAGAZINE_LAYOUT_DISPLAY_TYPE);
            Map<Integer, WallLayoutConfig> layoutMap = new HashMap<Integer, WallLayoutConfig>();

            for (String layout : magazineLayoutList) {
                if (WallLayoutType.getByCode(layout) != null) {
                    WallLayoutConfig wallLayoutConfig = tmpCache.getWallLayoutConfigMap().get(WallLayoutType.getByCode(layout));

                    if (wallLayoutConfig != null) {
                        layoutMap.put(layoutMap.size(), wallLayoutConfig);
                    } else {
                        GAlerter.lab("DisplayHotdeployConfig, magazine " + magazineCode + " there is not exist layoutConfig layout type: " + layout);
                        continue;
                    }
                } else {
                    GAlerter.lab("DisplayHotdeployConfig, magazine " + magazineCode + " there is not exist layout type: " + layout);
                    continue;
                }
            }

            magazineConfig.setLayoutMap(layoutMap);
            magazineConfigMap.put(magazineCode, magazineConfig);
        }

        tmpCache.setMagazineConfigMap(magazineConfigMap);
    }

    public Map<Integer, String> getWallBlockStyleMap(WallContentType wallContentType, WallBlockType wallBlockType, WallContentRule wallContentRule) {
        String key = wallContentType.getCode() + DEFAULT_SEPARATOR + wallBlockType.getCode() + DEFAULT_SEPARATOR + wallContentRule.getCode();

        if (this.cached.getWallBlockStyleMap().get(key) != null) {
            return this.cached.getWallBlockStyleMap().get(key);
        }
        return null;
    }

    public WallBlockStyleConfig getWallBlockStyleConfig(WallContentType wallContentType, WallBlockType wallBlockType, WallContentRule wallContentRule, String style) {
        String key = wallContentType.getCode() + DEFAULT_SEPARATOR + wallBlockType.getCode() + DEFAULT_SEPARATOR + wallContentRule.getCode() + DEFAULT_SEPARATOR + style;

        if (this.cached.getWallBlockStyleConfigMap().get(key) != null) {
            return this.cached.getWallBlockStyleConfigMap().get(key);
        }

        return null;
    }

    public MagazineConfig getMagazineConfig(String magazineCode) {
        return this.cached.getMagazineConfigMap().get(magazineCode);
    }

    private class Cached {
        // wall content type
        Map<WallContentType, WallContentType> wallContentTypeMap;

        // wall block type
        Map<WallBlockType, WallBlockType> wallBlockTypeMap;

        // wall content checked rule
        Map<WallContentRule, WallContentRule> wallContentCheckedRuleMap;

        // wall layout
        Map<WallLayoutType, WallLayoutConfig> wallLayoutConfigMap;

        // wall block style
        Map<String, Map<Integer, String>> wallBlockStyleMap;

        // wall block style rule
        Map<String, WallBlockStyleConfig> wallBlockStyleConfigMap;

        // magazine config
        Map<String, MagazineConfig> magazineConfigMap;

        // the getter and setter

        public Map<WallContentType, WallContentType> getWallContentTypeMap() {
            return wallContentTypeMap;
        }

        public void setWallContentTypeMap(Map<WallContentType, WallContentType> wallContentTypeMap) {
            this.wallContentTypeMap = wallContentTypeMap;
        }

        public Map<WallBlockType, WallBlockType> getWallBlockTypeMap() {
            return wallBlockTypeMap;
        }

        public void setWallBlockTypeMap(Map<WallBlockType, WallBlockType> wallBlockTypeMap) {
            this.wallBlockTypeMap = wallBlockTypeMap;
        }

        public Map<WallContentRule, WallContentRule> getWallContentCheckedRuleMap() {
            return wallContentCheckedRuleMap;
        }

        public void setWallContentCheckedRuleMap(Map<WallContentRule, WallContentRule> wallContentCheckedRuleMap) {
            this.wallContentCheckedRuleMap = wallContentCheckedRuleMap;
        }

        public Map<WallLayoutType, WallLayoutConfig> getWallLayoutConfigMap() {
            return wallLayoutConfigMap;
        }

        public void setWallLayoutConfigMap(Map<WallLayoutType, WallLayoutConfig> wallLayoutConfigMap) {
            this.wallLayoutConfigMap = wallLayoutConfigMap;
        }

        public Map<String, Map<Integer, String>> getWallBlockStyleMap() {
            return wallBlockStyleMap;
        }

        public void setWallBlockStyleMap(Map<String, Map<Integer, String>> wallBlockStyleMap) {
            this.wallBlockStyleMap = wallBlockStyleMap;
        }

        public Map<String, WallBlockStyleConfig> getWallBlockStyleConfigMap() {
            return wallBlockStyleConfigMap;
        }

        public void setWallBlockStyleConfigMap(Map<String, WallBlockStyleConfig> wallBlockStyleConfigMap) {
            this.wallBlockStyleConfigMap = wallBlockStyleConfigMap;
        }

        public Map<String, MagazineConfig> getMagazineConfigMap() {
            return magazineConfigMap;
        }

        public void setMagazineConfigMap(Map<String, MagazineConfig> magazineConfigMap) {
            this.magazineConfigMap = magazineConfigMap;
        }
    }
}
