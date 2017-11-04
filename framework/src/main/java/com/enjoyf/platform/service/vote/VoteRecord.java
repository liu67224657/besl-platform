package com.enjoyf.platform.service.vote;

import com.enjoyf.platform.service.JsonBinder;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: yongmingxu
 * Date: 12-9-19
 * Time: 下午2:26
 * Description: 投票记录
 */
public class VoteRecord implements Serializable {

    private long optionId;
    private String optionValue;

    //getter and setter
    public long getOptionId() {
        return optionId;
    }

    public void setOptionId(long optionId) {
        this.optionId = optionId;
    }

    public String getOptionValue() {
        return optionValue;
    }

    public void setOptionValue(String optionValue) {
        this.optionValue = optionValue;
    }

    /** to json */
    public String toJson(){
        return JsonBinder.buildNormalBinder().toJson(this);
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
