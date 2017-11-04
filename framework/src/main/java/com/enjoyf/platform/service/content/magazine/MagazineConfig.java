package com.enjoyf.platform.service.content.magazine;

import com.enjoyf.platform.service.content.ContentType;
import com.enjoyf.platform.service.content.wall.WallLayoutConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: yongmingxu
 * Date: 12-7-9
 * Time: 上午10:06
 * To change this template use File | Settings | File Templates.
 */
public class MagazineConfig {
    private String magazineCode;

    // display content type value
    private Map<String, ContentType> contentDisplayTypeMap = new HashMap<String, ContentType>();

    //layout map
    private Map<Integer, WallLayoutConfig> layoutMap;

    //layout display type choose rule 0:sequence 1: random
    private int chooseRule = 0;

    private int layoutSize = 5;

    private int totalLayout = 20;
    
    private String layoutFtlPath;
    private String blockFtlPath;
    private String modelAndView;

    public MagazineConfig() {
    }

    // getter and setter

    public String getMagazineCode() {
        return magazineCode;
    }

    public void setMagazineCode(String magazineCode) {
        this.magazineCode = magazineCode;
    }

    public Map<String, ContentType> getContentDisplayTypeMap() {
        return contentDisplayTypeMap;
    }

    public void setContentDisplayTypeMap(Map<String, ContentType> contentDisplayTypeMap) {
        this.contentDisplayTypeMap = contentDisplayTypeMap;
    }

    public Map<Integer, WallLayoutConfig> getLayoutMap() {
        return layoutMap;
    }

    public void setLayoutMap(Map<Integer, WallLayoutConfig> layoutMap) {
        this.layoutMap = layoutMap;
    }

    public int getChooseRule() {
        return chooseRule;
    }

    public void setChooseRule(int chooseRule) {
        this.chooseRule = chooseRule;
    }

    public int getLayoutSize() {
        return layoutSize;
    }

    public void setLayoutSize(int layoutSize) {
        this.layoutSize = layoutSize;
    }

    public int getTotalLayout() {
        return totalLayout;
    }

    public void setTotalLayout(int totalLayout) {
        this.totalLayout = totalLayout;
    }

    public String getLayoutFtlPath() {
        return layoutFtlPath;
    }

    public void setLayoutFtlPath(String layoutFtlPath) {
        this.layoutFtlPath = layoutFtlPath;
    }

    public String getBlockFtlPath() {
        return blockFtlPath;
    }

    public void setBlockFtlPath(String blockFtlPath) {
        this.blockFtlPath = blockFtlPath;
    }

    public String getModelAndView() {
        return modelAndView;
    }

    public void setModelAndView(String modelAndView) {
        this.modelAndView = modelAndView;
    }
}
