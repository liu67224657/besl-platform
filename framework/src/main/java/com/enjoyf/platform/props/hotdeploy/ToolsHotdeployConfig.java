package com.enjoyf.platform.props.hotdeploy;

import com.enjoyf.platform.props.EnvConfig;
import com.enjoyf.platform.service.gameres.GameCategory;
import com.enjoyf.platform.service.gameres.GameDevice;
import com.enjoyf.platform.service.gameres.GameStyle;
import com.google.common.base.Strings;
import org.apache.taglibs.standard.lang.jpath.adapter.StatusIterationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.regex.Pattern;

/**
 * <a href="mailto:taijunli@enjoyf.com">Li Taijun</a>
 */
public class ToolsHotdeployConfig extends HotdeployConfig {
    //
    private static final Logger logger = LoggerFactory.getLogger(ToolsHotdeployConfig.class);

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private static final String KEY_GR_PLATFORM_LIST = "tools.platform.list";           //平台列表
    private static final String KEY_GR_TYPE_LIST = "tools.type.list";                    //游戏类型列表
    private static final String KEY_GR_STYLE_LIST = "tools.style.list";                 //风格列表

    private static final String KEY_UPLOAD_TOKEN_UNO = "upload.token.uno";

    private static final String KEY_OPEN_IP_LOGIN_LIST = "open.regex.list";
    //regex's value
    private static final String SUFFIX_KEY_REGEX_VALUE = ".value";
    private static final String PREFIX_KEY_REGEX = "regex.";
    //customer list
private static final String COMMENT_REPLY_CUSTOMER_PROFILE = "comment.reply.customer.profile";
    //
    private ResourceCache resourceCache;

    public ToolsHotdeployConfig() {
        super(EnvConfig.get().getToolsHotdeployConfigureFile());
    }

    @Override
    public void init() {
        reload();
    }

    @Override
    public void reload() {
        super.reload();

        List<String> platFormList = getList(KEY_GR_PLATFORM_LIST);
        List<String> typeList = getList(KEY_GR_TYPE_LIST);
        List<String> styleList = getList(KEY_GR_STYLE_LIST);
        String uploadTokenUno = getString(KEY_UPLOAD_TOKEN_UNO);

        ResourceCache resCache = new ResourceCache();

        Map<String, GameDevice> deviceMap = new LinkedHashMap<String, GameDevice>();
        for (String deviceCode : platFormList) {
            deviceMap.put(deviceCode, new GameDevice(deviceCode));
        }
        resCache.setDeviceMap(deviceMap);

        Map<String, GameCategory> catogoryMap = new LinkedHashMap<String, GameCategory>();
        for (String catogoryCode : typeList) {
            catogoryMap.put(catogoryCode, new GameCategory(catogoryCode));
        }
        resCache.setCategoryMap(catogoryMap);

        Map<String, GameStyle> styleMap = new LinkedHashMap<String, GameStyle>();
        for (String styleCode : styleList) {
            styleMap.put(styleCode, new GameStyle(styleCode));
        }
        resCache.setStyleMap(styleMap);

        //
        List<String> regexList = getList(KEY_OPEN_IP_LOGIN_LIST);
        Map<String, Pattern> patternMap = new HashMap<String, Pattern>();
        for (String regex : regexList) {
            String regexValue = getString(PREFIX_KEY_REGEX + regex + SUFFIX_KEY_REGEX_VALUE);

            if (!Strings.isNullOrEmpty(regexValue)) {
                patternMap.put(regex, Pattern.compile(regexValue, Pattern.CASE_INSENSITIVE));
            }
        }

        resCache.getRegexPatternsMap().putAll(patternMap);
//
        List<String> customerList = getList(COMMENT_REPLY_CUSTOMER_PROFILE);
        resCache.setCustomerList(customerList);
        //
        resCache.setUploadTokenUno(uploadTokenUno);
        this.resourceCache = resCache;

        logger.info("ToolsHotdeployConfig template Props init finished.");
    }

    private class ResourceCache {
        private Map<String, GameDevice> deviceMap;
        private Map<String, GameCategory> categoryMap;
        private Map<String, GameStyle> styleMap;
        private String uploadTokenUno = "";
        private Map<String, Pattern> regexPatternsMap = new HashMap<String, Pattern>();
        private List<String> customerList;

        public Map<String, GameDevice> getDeviceMap() {
            return deviceMap;
        }

        public void setDeviceMap(Map<String, GameDevice> deviceMap) {
            this.deviceMap = deviceMap;
        }

        public Map<String, GameCategory> getCategoryMap() {
            return categoryMap;
        }

        public void setCategoryMap(Map<String, GameCategory> categoryMap) {
            this.categoryMap = categoryMap;
        }

        public Map<String, GameStyle> getStyleMap() {
            return styleMap;
        }

        public void setStyleMap(Map<String, GameStyle> styleMap) {
            this.styleMap = styleMap;
        }

        public String getUploadTokenUno() {
            return uploadTokenUno;
        }

        public void setUploadTokenUno(String uploadTokenUno) {
            this.uploadTokenUno = uploadTokenUno;
        }

        public Map<String, Pattern> getRegexPatternsMap() {
            return regexPatternsMap;
        }

        public void setRegexPatternsMap(Map<String, Pattern> regexPatternsMap) {
            this.regexPatternsMap = regexPatternsMap;
        }

        public void setCustomerList(List<String> customerList) {
            this.customerList = customerList;
        }

        public List<String> getCustomerList() {
            return customerList;
        }
    }

    public Map<String, GameDevice> getDeviceMap() {
        return resourceCache.getDeviceMap();
    }

    public Map<String, GameCategory> getCategoryMap() {
        return resourceCache.getCategoryMap();
    }

    public Map<String, GameStyle> getStyleMap() {
        return resourceCache.getStyleMap();
    }

    public String getUploadTokenUno() {
        return resourceCache.getUploadTokenUno();
    }

    public Set<Pattern> getRegexPatterns() {
        Set<Pattern> returnValue = new HashSet<Pattern>();

        Iterator<Map.Entry<String, Pattern>> it = resourceCache.getRegexPatternsMap().entrySet().iterator();
        while(it.hasNext()){
            Map.Entry<String, Pattern> entry = it.next();
            returnValue.add(entry.getValue());
        }

        return returnValue;
    }

    public List<String> getCustomerList(){
        return resourceCache.getCustomerList();
    }

}
