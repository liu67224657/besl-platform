package com.enjoyf.platform.service.joymeapp;

import com.enjoyf.platform.service.JsonBinder;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.reflect.ReflectUtil;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-9-11
 * Time: 上午9:58
 * To change this template use File | Settings | File Templates.
 */
public class ChannelTopMenuSet implements Serializable {

    private Set<ChannelTopMenu> channelTopMenuSet = new LinkedHashSet<ChannelTopMenu>();

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }

    @Override
    public int hashCode() {
        return channelTopMenuSet.hashCode();
    }

    public Set<ChannelTopMenu> getChannelTopMenuSet() {
        return channelTopMenuSet;
    }

    public void setChannelTopMenuSet(Set<ChannelTopMenu> channelTopMenuSet) {
        this.channelTopMenuSet = channelTopMenuSet;
    }

    public String toJson() {
        return JsonBinder.buildNormalBinder().toJson(channelTopMenuSet);
    }

    public static ChannelTopMenuSet fromJson(String jsonString) {

        ChannelTopMenuSet returnObj = new ChannelTopMenuSet();

        try {
            Set<ChannelTopMenu> setByJsonStr = JsonBinder.buildNormalBinder().getMapper().readValue(jsonString, new TypeReference<LinkedHashSet<ChannelTopMenu>>() {
            });
            returnObj.setChannelTopMenuSet(setByJsonStr);
        } catch (IOException e) {
            GAlerter.lab(ChannelTopMenuSet.class.getName() + " occured IOException.e", e);
        }
        return returnObj;
    }

    public void add(ChannelTopMenu img){
        channelTopMenuSet.add(img);
    }
}
