package com.enjoyf.platform.service.profile;

import com.enjoyf.platform.service.JsonBinder;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.reflect.ReflectUtil;
import com.google.common.base.Strings;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: yongmingxu
 * Date: 11-12-12

 * Time: 上午10:19
 * To change this template use File | Settings | File Templates.
 */
public class ProfileBlogHeadIconSet implements Serializable{
    private Set<ProfileBlogHeadIcon> iconSet = new LinkedHashSet<ProfileBlogHeadIcon>();

    public ProfileBlogHeadIconSet(){
    }

    public ProfileBlogHeadIconSet(Set<ProfileBlogHeadIcon> set){
        iconSet = set;
    }

    public ProfileBlogHeadIconSet(Collection headIcons){
        this.iconSet.addAll(headIcons);
    }

    public ProfileBlogHeadIconSet(String jsonStr) {
        if (!Strings.isNullOrEmpty(jsonStr)) {
            try {
                iconSet = JsonBinder.buildNormalBinder().getMapper().readValue(jsonStr, new TypeReference<LinkedHashSet<ProfileBlogHeadIcon>>() {
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

    public void add(ProfileBlogHeadIcon image) {
        iconSet.add(image);
    }

    public void add(Set<ProfileBlogHeadIcon> headIcons) {
        this.iconSet.addAll(headIcons);
    }

    public String toJsonStr() {
        return JsonBinder.buildNormalBinder().toJson(iconSet);
    }

    public static ProfileBlogHeadIconSet parse(String jsonStr) {
        ProfileBlogHeadIconSet returnValue = new ProfileBlogHeadIconSet();

        if (!Strings.isNullOrEmpty(jsonStr)) {
            try {
                Set<ProfileBlogHeadIcon> imageContents = JsonBinder.buildNormalBinder().getMapper().readValue(jsonStr, new TypeReference<LinkedHashSet<ProfileBlogHeadIcon>>() {
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
        return iconSet.hashCode();
    }

    public Set<ProfileBlogHeadIcon> getIconSet() {
        return iconSet;
    }

    public void setIconSet(Set<ProfileBlogHeadIcon> iconSet) {
        this.iconSet = iconSet;
    }

    public int validSize(){
        int i = 0;
        Iterator<ProfileBlogHeadIcon> it = iconSet.iterator();
        while (it.hasNext()){
            ProfileBlogHeadIcon profileBlogHeadIcon = it.next();
            if(profileBlogHeadIcon.getValidStatus()){
                i++;
            }
        }

        return i;
    }
}
