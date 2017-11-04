package com.enjoyf.platform.service.gameres;

import com.enjoyf.platform.service.JsonBinder;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.reflect.ReflectUtil;
import com.google.common.base.Strings;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: zhaoxin
 * Date: 11-8-22
 * Time: 下午5:28
 * Desc: 图片实体类
 */
public class ResourceImageSet implements Serializable {
    //
    private Set<ResourceImage> images = new LinkedHashSet<ResourceImage>();

    private Boolean validStatus = true; //默认可以显示


    public ResourceImageSet() {
    }

    public ResourceImageSet(Set<ResourceImage> set) {
        images = set;
    }

    public ResourceImageSet(Collection iamges) {
        this.images.addAll(iamges);
    }

    public ResourceImageSet(String jsonStr) {
        if (!Strings.isNullOrEmpty(jsonStr)) {
            try {
                images = JsonBinder.buildNormalBinder().getMapper().readValue(jsonStr, new TypeReference<LinkedHashSet<ResourceImage>>() {
                });
            } catch (IOException e) {
                //
                GAlerter.lab("ResourceImageSet constructor error, jsonStr:" + jsonStr, e);
            }
        }
    }

    public Set<ResourceImage> getImages() {
        return images;
    }

    public void add(ResourceImage image) {
        images.add(image);
    }

    public void add(Set<ResourceImage> images) {
        this.images.addAll(images);
    }

    public String toJsonStr() {
        return JsonBinder.buildNormalBinder().toJson(images);
    }

    public static ResourceImageSet parse(String jsonStr) {
        ResourceImageSet returnValue = new ResourceImageSet();

        if (!Strings.isNullOrEmpty(jsonStr)) {
            try {
                Set<ResourceImage> imageContents = JsonBinder.buildNormalBinder().getMapper().readValue(jsonStr, new TypeReference<LinkedHashSet<ResourceImage>>() {
                });

                returnValue.add(imageContents);
            } catch (IOException e) {
                GAlerter.lab("ResourceImageSet parse error, jsonStr:" + jsonStr, e);
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
        return images.hashCode();
    }

    public Boolean getValidStatus() {
        return validStatus;
    }

    public void setValidStatus(Boolean validStatus) {
        this.validStatus = validStatus;
    }

    public static void main(String[] args) {

        ResourceImageSet set = new ResourceImageSet();
        for (int i = 0; i < 5; i++) {
            ResourceImage imageContent = new ResourceImage();
            imageContent.setDesc("完全" + i);
            imageContent.setLl(i + "");
            set.add(imageContent);
        }
        String jsonstring = set.toJsonStr();
        System.out.println(jsonstring);
        System.out.println("----------------");
        ResourceImageSet ss = ResourceImageSet.parse(jsonstring);
        System.out.println(ss);
        System.out.println(ss.getImages().size());
        for (ResourceImage ic : ss.getImages()) {
//            System.out.println(ic.getDesc());
            System.out.println(ic.getLl());
//            System.out.println(ic);
        }
    }
}
