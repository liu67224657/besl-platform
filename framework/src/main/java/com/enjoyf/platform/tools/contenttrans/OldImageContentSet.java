package com.enjoyf.platform.tools.contenttrans;

import com.enjoyf.platform.service.JsonBinder;
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
 * Desc: 音乐实体类
 */
public class OldImageContentSet implements Serializable {
    //
    private Set<OldImageContent> photo = new LinkedHashSet<OldImageContent>();

    public OldImageContentSet() {
    }

    public OldImageContentSet(Set<OldImageContent> set) {
        photo = set;
    }

    public OldImageContentSet(Collection<OldImageContent> iamges) {
        this.photo.addAll(iamges);
    }

    public OldImageContentSet(String jsonStr) {
        if (!Strings.isNullOrEmpty(jsonStr)) {
            try {
                photo = JsonBinder.buildNormalBinder().getMapper().readValue(jsonStr, new TypeReference<LinkedHashSet<OldImageContent>>() {
                });
            } catch (IOException e) {
                //
                //GAlerter.lab("OldImageContentSet constructor error, jsonStr:" + jsonStr, e);
            }
        }
    }

    public Set<OldImageContent> getPhoto() {
        return photo;
    }

    public void add(OldImageContent image) {
        photo.add(image);
    }

    public void add(Set<OldImageContent> images) {
        this.photo.addAll(images);
    }

    public void setPhoto(Set<OldImageContent> photo) {
        this.photo = photo;
    }

    public String toJsonStr() {
        return JsonBinder.buildNormalBinder().toJson(photo);
    }

    public static OldImageContentSet parse(String jsonStr) {
        OldImageContentSet returnValue = new OldImageContentSet();

        if (!Strings.isNullOrEmpty(jsonStr)) {
            try {
                returnValue = JsonBinder.buildNormalBinder().getMapper().readValue(jsonStr, new TypeReference<OldImageContentSet>() {
                });
            } catch (IOException e) {
                //GAlerter.lab("OldImageContentSet parse error, jsonStr:" + jsonStr, e);
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
        return photo.hashCode();
    }
}
