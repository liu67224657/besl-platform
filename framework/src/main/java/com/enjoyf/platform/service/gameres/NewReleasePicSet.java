package com.enjoyf.platform.service.gameres;

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
 * User: zhitaoshi
 * Date: 13-8-20
 * Time: 下午4:37
 * To change this template use File | Settings | File Templates.
 */
public class NewReleasePicSet implements Serializable{

    private Set<NewReleasePic> picSet = new LinkedHashSet<NewReleasePic>();

    public NewReleasePicSet(){}

    public NewReleasePicSet(Set<NewReleasePic> picSet) {
        this.picSet = picSet;
    }

    public NewReleasePicSet(Collection pics){
        this.picSet.addAll(pics);
    }

    public NewReleasePicSet(String jsonStr){
        if(!Strings.isNullOrEmpty(jsonStr)){
            try {
                picSet = JsonBinder.buildNormalBinder().getMapper().readValue(jsonStr, new TypeReference<NewReleasePic>() {});
            } catch (IOException e) {
                GAlerter.lab("NewGamePicSet constructor error, jsonStr:" + jsonStr, e);
            }
        }
    }

    public void add(NewReleasePic img){
        picSet.add(img);
    }

    public void add(Set<NewReleasePic> imgs){
        picSet.addAll(imgs);
    }

    public String toJsonStr(){
        return JsonBinder.buildNormalBinder().toJson(picSet);
    }

    public static NewReleasePicSet parse(String jsonStr) {
        NewReleasePicSet returnValue = new NewReleasePicSet();

        if (!Strings.isNullOrEmpty(jsonStr)) {
            try {
                Set<NewReleasePic> imageContents = JsonBinder.buildNormalBinder().getMapper().readValue(jsonStr, new TypeReference<LinkedHashSet<NewReleasePic>>() {});
                returnValue.add(imageContents);
            } catch (IOException e) {
                GAlerter.lab("NewGamePicSet parse error, jsonStr:" + jsonStr, e);
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
        return picSet.hashCode();
    }

    public Set<NewReleasePic> getPicSet() {
        return picSet;
    }

    public void setPicSet(Set<NewReleasePic> picSet) {
        this.picSet = picSet;
    }

    public int size(){
        int i = 0;
        Iterator<NewReleasePic> it = picSet.iterator();
        while (it.hasNext()){
            NewReleasePic pic = new NewReleasePic();
            if(pic.getDisplay()){
                i++;
            }
        }
        return i;
    }
}
