/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.viewline;

import com.enjoyf.platform.service.JsonBinder;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.reflect.ReflectUtil;
import com.google.common.base.Strings;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.io.Serializable;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 12-2-15 下午1:13
 * Description:
 */
public class ViewLineItemDisplayInfo implements Serializable {
    //
    private String subject;
    private String iconUrl;
    private String linkUrl;
    private String iconUrl2;
    private String desc;

    //extraField1->放分类首页了以及chinajoy的扩展字段
    private String extraField1;
    private String extraField2;
    private String extraField3;
    private String extraField4;
    private String extraField5; // 存放图标标识

    //
    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public static ViewLineItemDisplayInfo parse(String jsonStr) {
        ViewLineItemDisplayInfo returnValue = new ViewLineItemDisplayInfo();

        if (!Strings.isNullOrEmpty(jsonStr)) {
            try {
                returnValue = JsonBinder.buildNormalBinder().getMapper().readValue(jsonStr, new TypeReference<ViewLineItemDisplayInfo>() {
                });

            } catch (IOException e) {
                GAlerter.lab("ViewLineItemDisplayInfo parse error, jsonStr:" + jsonStr, e);
            }
        }

        return returnValue;
    }

    public String getExtraField1() {
        return extraField1;
    }

    public void setExtraField1(String extraField1) {
        this.extraField1 = extraField1;
    }

    public String getExtraField2() {
        return extraField2;
    }

    public void setExtraField2(String extraField2) {
        this.extraField2 = extraField2;
    }

    public String getExtraField3() {
        return extraField3;
    }

    public void setExtraField3(String extraField3) {
        this.extraField3 = extraField3;
    }

    public String getExtraField4() {
        return extraField4;
    }

    public void setExtraField4(String extraField4) {
        this.extraField4 = extraField4;
    }

    public String getExtraField5() {
        return extraField5;
    }

    public void setExtraField5(String extraField5) {
        this.extraField5 = extraField5;
    }

    public String getIconUrl2() {
        return iconUrl2;
    }

    public void setIconUrl2(String iconUrl2) {
        this.iconUrl2 = iconUrl2;
    }

    /**
     * to json
     */
    public String toJson() {
        return JsonBinder.buildNormalBinder().toJson(this);
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
