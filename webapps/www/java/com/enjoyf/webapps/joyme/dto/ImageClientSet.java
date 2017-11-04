package com.enjoyf.webapps.joyme.dto;

import com.enjoyf.platform.service.JsonBinder;
import com.enjoyf.platform.util.log.GAlerter;
import com.google.common.base.Strings;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: yujiaxiu
 * Date: 12-8-13
 * Time: 下午6:36
 * To change this template use File | Settings | File Templates.
 */
public class ImageClientSet implements Serializable {

    private int total;
    private Set<MediaDTO> set;

    public ImageClientSet(){
        set = new LinkedHashSet<MediaDTO>();
    }

    public ImageClientSet(Collection<MediaDTO> medias){
        this.set.addAll(medias);
    }

    public ImageClientSet(String jsonStr){
        if (!Strings.isNullOrEmpty(jsonStr)) {
            try {
                set = JsonBinder.buildNormalBinder().getMapper().readValue(jsonStr, new TypeReference<LinkedHashSet<MediaDTO>>() {
                });
            } catch (IOException e) {
                //
                GAlerter.lab("ImageClientSet constructor error, jsonStr:" + jsonStr, e);
            }
        }
    }

    public Set<MediaDTO> getSet(){
        return set;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public void add(MediaDTO media) {
        set.add(media);
    }

    public void add(Set<MediaDTO> medias) {
        this.set.addAll(medias);
    }

    public String toJsonStr() {

        return JsonBinder.buildNormalBinder().toJson(this);

    }

    public static ImageClientSet parse(String jsonStr) {
        ImageClientSet returnValue = new ImageClientSet();

        if (!Strings.isNullOrEmpty(jsonStr)) {
            try {
                Set<MediaDTO> medias = JsonBinder.buildNormalBinder().getMapper().readValue(jsonStr, new TypeReference<LinkedHashSet<MediaDTO>>() {
                });

                returnValue.add(medias);
            } catch (IOException e) {
                GAlerter.lab("ImageClientSet parse error, jsonStr:" + jsonStr, e);
            }
        }

        return returnValue;
    }

}
