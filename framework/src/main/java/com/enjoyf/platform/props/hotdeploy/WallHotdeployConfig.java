package com.enjoyf.platform.props.hotdeploy;

import com.enjoyf.platform.props.EnvConfig;
import com.enjoyf.platform.service.content.wall.WallBlockStyleConfig;
import com.enjoyf.platform.service.content.wall.WallBlockType;
import com.enjoyf.platform.service.content.wall.WallContentRule;
import com.enjoyf.platform.service.content.wall.WallContentType;
import com.enjoyf.platform.service.content.wall.WallLayoutConfig;
import com.enjoyf.platform.service.content.wall.WallLayoutType;
import com.enjoyf.platform.util.log.GAlerter;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: yongmingxu
 * Date: 12-3-15
 * Time: 下午1:30
 * To change this template use File | Settings | File Templates.
 */
public class WallHotdeployConfig extends HotdeployConfig {
    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(WallHotdeployConfig.class);

    // the layout display rule key
    private static final String KEY_WALL_LAYOUT_DISPLAY_RULE = "wall.layout.display.rule";
    // the layout display size per page key
    private static final String KEY_WALL_LAYOUT_PAGE_DISPLAY_SIZE = "wall.layout.page.display.size";
    // the layout display size total
    private static final String KEY_WALL_LAYOUT_TOTAL_DISPLAY_SIZE = "wall.layout.total.display.size";

    // the layout type list key
    private static final String KEY_WALL_LAYOUT_TYPE_LIST = "wall.layout.type.list";
    // the wall block type list key
    private static final String KEY_WALL_BLOCK_TYPE_LIST = "wall.block.type.list";
    // the content type list key
    private static final String KEY_WALL_CONTENT_TYPE_LIST = "wall.block.content.type.list";
    // the content checked rules key
    private static final String KEY_WALL_CONTENT_SC_CHECKED_RULES_LIST = "wall.content.sc.checked.rules";
    // the system blacklist key
    private static final String KEY_WALL_SYSTEM_BLACKLIST = "wall.system.blacklist";

    private static final String SUFFIX_LAYOUT_CONTAINS_BLOCK = ".contains.block.list";
    private static final String SUFFIX_BLOCK_STYLE = ".style";

    private static final String PREFIX_BLOCK_STYYLE_URLE = "rule.";

    private static final String DEFAULT_SEPARATOR = ".";
    private static final String RULE_SEPARATOR = ":";

    private static final String KEY_WALL_BLOCK_IMAGE_PROPORTION = "";

    public static final int WALL_LAYOUT_DISPLAY_RULE_VALUE = 0;
    public static final int WALL_LAYOUT_PAGE_DISPLAY_SIZE_VALUE = 5;
    public static final int WALL_LAYOUT_TOTAL_DISPLAY_SIZE_VALUE = 100;
    public static final int WALL_WALL_BLOCK_IMAGE_PROPORTION = 3;

    private Cached cached;

    public WallHotdeployConfig() {
        super(EnvConfig.get().getWallHotdeployConfigureFile());
    }

    @Override
    public void init() {
        if (logger.isDebugEnabled()) {
            logger.debug("WallHotdeployConfig Props init start......");
        }

        reload();
    }

    public synchronized void reload() {
        super.reload();

        Cached tmpCache = new Cached();

        int layoutDisplayRule = this.getInt(KEY_WALL_LAYOUT_DISPLAY_RULE,WALL_LAYOUT_DISPLAY_RULE_VALUE);
        int layoutDisplaySize = this.getInt(KEY_WALL_LAYOUT_PAGE_DISPLAY_SIZE,WALL_LAYOUT_PAGE_DISPLAY_SIZE_VALUE);
        int layoutTotalDisplaySize = this.getInt(KEY_WALL_LAYOUT_TOTAL_DISPLAY_SIZE, WALL_LAYOUT_TOTAL_DISPLAY_SIZE_VALUE);
        int wallBlockImageProportion = this.getInt(KEY_WALL_BLOCK_IMAGE_PROPORTION, WALL_WALL_BLOCK_IMAGE_PROPORTION);

        tmpCache.setWallLayoutDisplayRule(layoutDisplayRule);
        tmpCache.setWallLayoutPageDisplaySize(layoutDisplaySize);
        tmpCache.setWallLayoutTotalDisplaySize(layoutTotalDisplaySize);
        tmpCache.setWallBlockImageProportion(wallBlockImageProportion);

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

        //load blacklist
        loadBlackList(tmpCache);

        cached = tmpCache;

    }

    // load block type
    private void loadBlockType(Cached tmpCache) {
        Map<WallBlockType, WallBlockType> wallBlockTypeMap = new HashMap<WallBlockType, WallBlockType>();

        //get block type list
        List<String> typeList = this.getList(KEY_WALL_BLOCK_TYPE_LIST);

        for (String type : typeList) {
            if (WallBlockType.getByCode(type) != null) {
                wallBlockTypeMap.put(WallBlockType.getByCode(type), WallBlockType.getByCode(type));
            } else {
                GAlerter.lab("WallHotdeployConfig, there is not exist block type: " + type);
                continue;
            }
        }

        tmpCache.setWallBlockTypeMap(wallBlockTypeMap);
    }

    // load content type
    private void loadContentType(Cached tmpCache) {
        Map<WallContentType, WallContentType> wallContentTypeMap = new HashMap<WallContentType, WallContentType>();

        //get content type list
        List<String> typeList = this.getList(KEY_WALL_CONTENT_TYPE_LIST);

        for (String type : typeList) {
            if (WallContentType.getByCode(type) != null) {
                wallContentTypeMap.put(WallContentType.getByCode(type), WallContentType.getByCode(type));
            } else {
                GAlerter.lab("WallHotdeployConfig, there is not exist content type: " + type);
                continue;
            }
        }

        tmpCache.setWallContentTypeMap(wallContentTypeMap);
    }

    // load content checked rule
    private void loadContentCheckedRule(Cached tmpCache) {
        Map<WallContentRule, WallContentRule> wallContentCheckedRuleMap = new HashMap<WallContentRule, WallContentRule>();

        //get content checked rule list
        List<String> ruleList = this.getList(KEY_WALL_CONTENT_SC_CHECKED_RULES_LIST);

        for (String rule : ruleList) {
            if (WallContentRule.getByCode(rule) != null) {
                wallContentCheckedRuleMap.put(WallContentRule.getByCode(rule), WallContentRule.getByCode(rule));
            } else {
                GAlerter.lab("WallHotdeployConfig, there is not exist content checked rule: " + rule);
                continue;
            }
        }

        tmpCache.setWallContentCheckedRuleMap(wallContentCheckedRuleMap);

    }

    // load layout type
    private void loadLayoutType(Cached tmpCache) {
        Map<Integer, WallLayoutConfig> wallLayoutConfigMap = new HashMap<Integer, WallLayoutConfig>();

        // get the loayout type list
        List<String> layoutList = this.getList(KEY_WALL_LAYOUT_TYPE_LIST);

        int i = 0;
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
                        GAlerter.lab("WallHotdeployConfig, layout: " + layout + "contains a unknown block type :" + blockType);
                        continue;
                    }

                }

                wallLayoutConfig.setWallBlockTypeList(wallBlockTypeList);

                wallLayoutConfigMap.put(i++, wallLayoutConfig);

            } else {
                GAlerter.lab("WallHotdeployConfig, there is not exist layout type: " + layout);
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
                    String key = wallContentType.getCode()+DEFAULT_SEPARATOR+wallBlockType.getCode()+DEFAULT_SEPARATOR+wallContentRule.getCode();
                    List<String> styleList = this.getList(key+ SUFFIX_BLOCK_STYLE);

                    //
                    Map<Integer, String> styleMap = new HashMap<Integer, String>();
                    for(int i=0; i<styleList.size(); i++){
                        styleMap.put(i,styleList.get(i));
                        // style's rule
                        String defineKey = PREFIX_BLOCK_STYYLE_URLE+key+DEFAULT_SEPARATOR+styleList.get(i);
                        List<String> ruleDefine = this.getList(defineKey);
                        WallBlockStyleConfig styleConfig = new WallBlockStyleConfig();
                        Map<String,String> ruleMap = new HashMap<String, String>();

                        for(String config : ruleDefine){
                            // s:30 c:125 br:5 w:187 h:294 img:m
                            ruleMap.put(config.split(RULE_SEPARATOR)[0],config.split(RULE_SEPARATOR)[1]);
                        }

                        if(ruleMap.get("s")!=null){
                            styleConfig.setSubjectLen(Integer.parseInt(ruleMap.get("s")));
                        }else{
                            GAlerter.lab("WallHotdeployConfig, style config: s is null in "+defineKey);
                        }

                        if(ruleMap.get("c")!=null){
                            styleConfig.setContentLen(Integer.parseInt(ruleMap.get("c")));
                        } else {
                            GAlerter.lab("WallHotdeployConfig, style config: c is null in "+defineKey);
                        }

                        if(ruleMap.get("br")!=null){
                            styleConfig.setBrLen(Integer.parseInt(ruleMap.get("br")));
                        } else {
                            GAlerter.lab("WallHotdeployConfig, style config: br is null in "+defineKey);
                        }

                        if(ruleMap.get("w")!=null){
                            styleConfig.setWidth(Integer.parseInt(ruleMap.get("w")));
                        }
                        if(ruleMap.get("h")!=null){
                            styleConfig.setHeight(Integer.parseInt(ruleMap.get("h")));
                        }
                        if(ruleMap.get("img")!=null){
                            styleConfig.setImg(ruleMap.get("img"));
                        }

                        wallBlockStyleConfigMap.put(key+DEFAULT_SEPARATOR+styleList.get(i),styleConfig);

                    }
                    wallBlockStyleMap.put(key,styleMap);

                }
            }
        }

        tmpCache.setWallBlockStyleMap(wallBlockStyleMap);
        tmpCache.setWallBlockStyleConfigMap(wallBlockStyleConfigMap);

    }

    //load blacklist
    private void loadBlackList(Cached tmpCache) {
        Map<String, String> wallBlackListMap = new HashMap<String, String>();

        // get system blacklist
        List<String> blacklist = this.getList(KEY_WALL_SYSTEM_BLACKLIST);

        for (String blackuno : blacklist) {
            wallBlackListMap.put(blackuno, blackuno);
        }

        tmpCache.setWallBlackListMap(wallBlackListMap);
    }

    public int getWallLayoutDisplayRule(){
        return this.cached.getWallLayoutDisplayRule();
    }

    public int getWallLayoutPageDisplaySize(){
        return this.cached.getWallLayoutPageDisplaySize();
    }

    public int getWallLayoutTotalDisplaySize(){
        return this.cached.getWallLayoutTotalDisplaySize();
    }

    public int getWallWallBlockImageProportion(){
        return this.cached.getWallBlockImageProportion();
    }

    public Map<String, String> getWallBlackListMap(){
        return this.cached.getWallBlackListMap();
    }

    public Map<Integer, WallLayoutConfig> getWallLayoutConfigMap(){
        return this.cached.getWallLayoutConfigMap();
    }

    public Map<Integer, String> getWallBlockStyleMap(WallContentType wallContentType, WallBlockType wallBlockType, WallContentRule wallContentRule){
        String key = wallContentType.getCode()+DEFAULT_SEPARATOR+wallBlockType.getCode()+DEFAULT_SEPARATOR+wallContentRule.getCode();

        if(this.cached.getWallBlockStyleMap().get(key)!=null){
            return this.cached.getWallBlockStyleMap().get(key);
        }
        return null;
    }

    public WallBlockStyleConfig getWallBlockStyleConfig(WallContentType wallContentType, WallBlockType wallBlockType, WallContentRule wallContentRule,String style){
        String key =  wallContentType.getCode()+DEFAULT_SEPARATOR+wallBlockType.getCode()+DEFAULT_SEPARATOR+wallContentRule.getCode()+DEFAULT_SEPARATOR+style;

        if(this.cached.getWallBlockStyleConfigMap().get(key)!=null){
            return this.cached.getWallBlockStyleConfigMap().get(key);
        }

        return null;
    }

//    public Map<String, Map<Integer, String>> getWallBlockStyleMap(){
//        return this.cached.getWallBlockStyleMap();
//    }
//
//    public  Map<String, WallBlockStyleConfig> getWallBlockStyleConfigMap(){
//        return this.cached.getWallBlockStyleConfigMap();
//    }

    private class Cached {
        // wall content type
        Map<WallContentType, WallContentType> wallContentTypeMap;
        // wall block type
        Map<WallBlockType, WallBlockType> wallBlockTypeMap;
        // wall content checked rule
        Map<WallContentRule, WallContentRule> wallContentCheckedRuleMap;

        // wall loyout
        Map<Integer, WallLayoutConfig> wallLayoutConfigMap;

        // wall block style
        Map<String, Map<Integer, String>> wallBlockStyleMap;
        // wall block style rule
        Map<String, WallBlockStyleConfig> wallBlockStyleConfigMap;

        // wall system blacklist
        Map<String, String> wallBlackListMap;

        int wallLayoutDisplayRule;
        int wallLayoutPageDisplaySize;
        int wallLayoutTotalDisplaySize;
        int wallBlockImageProportion;

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

        public Map<Integer, WallLayoutConfig> getWallLayoutConfigMap() {
            return wallLayoutConfigMap;
        }

        public void setWallLayoutConfigMap(Map<Integer, WallLayoutConfig> wallLayoutConfigMap) {
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

        public Map<String, String> getWallBlackListMap() {
            return wallBlackListMap;
        }

        public void setWallBlackListMap(Map<String, String> wallBlackListMap) {
            this.wallBlackListMap = wallBlackListMap;
        }

        public int getWallLayoutDisplayRule() {
            return wallLayoutDisplayRule;
        }

        public void setWallLayoutDisplayRule(int wallLayoutDisplayRule) {
            this.wallLayoutDisplayRule = wallLayoutDisplayRule;
        }

        public int getWallLayoutPageDisplaySize() {
            return wallLayoutPageDisplaySize;
        }

        public void setWallLayoutPageDisplaySize(int wallLayoutPageDisplaySize) {
            this.wallLayoutPageDisplaySize = wallLayoutPageDisplaySize;
        }

        public int getWallLayoutTotalDisplaySize() {
            return wallLayoutTotalDisplaySize;
        }

        public void setWallLayoutTotalDisplaySize(int wallLayoutTotalDisplaySize) {
            this.wallLayoutTotalDisplaySize = wallLayoutTotalDisplaySize;
        }

        public int getWallBlockImageProportion() {
            return wallBlockImageProportion;
        }

        public void setWallBlockImageProportion(int wallBlockImageProportion) {
            this.wallBlockImageProportion = wallBlockImageProportion;
        }
    }
}
