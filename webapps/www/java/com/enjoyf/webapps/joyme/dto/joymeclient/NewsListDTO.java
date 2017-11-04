package com.enjoyf.webapps.joyme.dto.joymeclient;

import com.enjoyf.platform.util.reflect.ReflectUtil;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-9-17
 * Time: 下午3:06
 * To change this template use File | Settings | File Templates.
 */
public class NewsListDTO {
    private String lineCode;
    private String lineName;

    public String getLineCode() {
        return lineCode;
    }

    public void setLineCode(String lineCode) {
        this.lineCode = lineCode;
    }

    public String getLineName() {
        return lineName;
    }

    public void setLineName(String lineName) {
        this.lineName = lineName;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
