package com.enjoyf.platform.service.event.task;

import com.enjoyf.platform.service.JsonBinder;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.reflect.ReflectUtil;
import com.google.common.base.Strings;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-12-16
 * Time: 上午11:22
 * To change this template use File | Settings | File Templates.
 */
public class TaskAwards implements Serializable {
    private List<TaskAward> awardList = new ArrayList<TaskAward>();

    public TaskAwards() {
    }

    public TaskAwards(List<TaskAward> set) {
        awardList = set;
    }

    public TaskAwards(Collection headIcons) {
        this.awardList.addAll(headIcons);
    }

    public TaskAwards(String jsonStr) {
        if (!Strings.isNullOrEmpty(jsonStr)) {
            try {
                awardList = JsonBinder.buildNormalBinder().getMapper().readValue(jsonStr, new TypeReference<LinkedHashSet<TaskAward>>() {
                });
            } catch (IOException e) {
                //
                GAlerter.lab("ProfileBlogHeadIconSet constructor error, jsonStr:" + jsonStr, e);
            }
        }
    }

    public void add(TaskAward icon) {
        awardList.add(icon);
    }

    public void add(List<TaskAward> headIcons) {
        this.awardList.addAll(headIcons);
    }

    public String toJsonStr() {
        return JsonBinder.buildNormalBinder().toJson(awardList);
    }

    public static TaskAwards parse(String jsonStr) {
        TaskAwards returnValue = new TaskAwards();
        if (!Strings.isNullOrEmpty(jsonStr)) {
            try {
                List<TaskAward> imageContents = JsonBinder.buildNormalBinder().getMapper().readValue(jsonStr, new TypeReference<List<TaskAward>>() {
                });

                returnValue.add(imageContents);
            } catch (IOException e) {
                GAlerter.lab("ProfileBlogHeadIconSet parse error, jsonStr:" + jsonStr, e);
            }
        }
        return returnValue;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }

    @Override
    public int hashCode() {
        return awardList.hashCode();
    }

    public List<TaskAward> getAwardList() {
        return awardList;
    }

    public void setAwardList(List<TaskAward> iconSet) {
        this.awardList = iconSet;
    }

}
