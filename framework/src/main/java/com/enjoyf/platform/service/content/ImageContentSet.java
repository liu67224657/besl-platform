package com.enjoyf.platform.service.content;

import com.enjoyf.platform.service.JsonBinder;
import com.enjoyf.platform.service.ValidStatus;
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
 * User: zhaoxin
 * Date: 11-8-22
 * Time: 下午5:28
 * Desc: 图片实体类
 */
public class ImageContentSet implements Serializable {
    //
    private Set<ImageContent> images = new LinkedHashSet<ImageContent>();

    private Boolean validStatus = true; //默认可以显示


    public ImageContentSet() {
    }

    public ImageContentSet(Set<ImageContent> set) {
        images = set;
    }

    public ImageContentSet(Collection iamges) {
        this.images.addAll(iamges);
    }

    public ImageContentSet(String jsonStr) {
        if (!Strings.isNullOrEmpty(jsonStr)) {
            try {
                images = JsonBinder.buildNormalBinder().getMapper().readValue(jsonStr, new TypeReference<LinkedHashSet<ImageContent>>() {
                });
            } catch (IOException e) {
                //
                GAlerter.lab("ResourceImageSet constructor error, jsonStr:" + jsonStr, e);
            }
        }
    }

    public Set<ImageContent> getImages() {
        return images;
    }

    public void add(ImageContent image) {
        images.add(image);
    }

    public void add(Set<ImageContent> images) {
        this.images.addAll(images);
    }

    public String toJsonStr() {
        return JsonBinder.buildNormalBinder().toJson(images);
    }

    public static ImageContentSet parse(String jsonStr) {
        ImageContentSet returnValue = new ImageContentSet();

        if (!Strings.isNullOrEmpty(jsonStr)) {
            try {
                Set<ImageContent> imageContents = JsonBinder.buildNormalBinder().getMapper().readValue(jsonStr, new TypeReference<LinkedHashSet<ImageContent>>() {
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

        ImageContentSet set = new ImageContentSet();
        for (int i = 0; i < 5; i++) {
            ImageContent imageContent = new ImageContent();
            imageContent.setDesc("完全" + i);
            imageContent.setM(i + "");
            set.add(imageContent);
        }
        String jsonstring = set.toJsonStr();
        System.out.println(jsonstring);
        System.out.println("----------------");
        ImageContentSet ss = ImageContentSet.parse(jsonstring);
        System.out.println(ss);
        System.out.println(ss.getImages().size());
        for (ImageContent ic : ss.getImages()) {
            System.out.println(ic.getDesc());
            System.out.println(ic.getUrl());
            System.out.println(ic);
        }
    }
}
