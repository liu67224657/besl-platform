package com.enjoyf.platform.service.usercenter;

import com.enjoyf.platform.service.JsonBinder;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.reflect.ReflectUtil;
import com.google.common.base.Strings;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: yongmingxu
 * Date: 11-12-12

 * Time: 上午10:19
 * To change this template use File | Settings | File Templates.
 */
public class Icons implements Serializable{
    private List<Icon> iconList = new ArrayList<Icon>();

    public Icons(){
    }

    public Icons(List<Icon> set){
        iconList = set;
    }

    public Icons(Collection headIcons){
        this.iconList.addAll(headIcons);
    }

    public Icons(String jsonStr) {
        if (!Strings.isNullOrEmpty(jsonStr)) {
            try {
                iconList = JsonBinder.buildNormalBinder().getMapper().readValue(jsonStr, new TypeReference<LinkedHashSet<Icon>>() {
                });
            } catch (IOException e) {
                //
                GAlerter.lab("ProfileBlogHeadIconSet constructor error, jsonStr:" + jsonStr, e);
            }
        }
    }

//    public Set<ProfileBlogHeadIcon> getImages() {
//        return iconSet;
//    }

    public void add(Icon image) {
        iconList.add(image);
    }

    public void add(List<Icon> headIcons) {
        this.iconList.addAll(headIcons);
    }

    public String toJsonStr() {
        return JsonBinder.buildNormalBinder().toJson(iconList);
    }

    public static Icons parse(String jsonStr) {
        Icons returnValue = new Icons();
        if (!Strings.isNullOrEmpty(jsonStr)) {
            try {
                List<Icon> imageContents = JsonBinder.buildNormalBinder().getMapper().readValue(jsonStr, new TypeReference<List<Icon>>() {
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
    public int hashCode(){
        return iconList.hashCode();
    }

    public List<Icon> getIconList() {
        return iconList;
    }

    public void setIconList(List<Icon> iconSet) {
        this.iconList = iconSet;
    }

}
