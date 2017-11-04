package com.enjoyf.webapps.joyme.weblogic.point;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-6-13
 * Time: 下午4:32
 * To change this template use File | Settings | File Templates.
 */
public class PointReduceResult {
    private PointReduceType type;
    private int reduceValue;

    public PointReduceType getType() {
        return type;
    }

    public void setType(PointReduceType type) {
        this.type = type;
    }

    public int getReduceValue() {
        return reduceValue;
    }

    public void setReduceValue(int reduceValue) {
        this.reduceValue = reduceValue;
    }
}
