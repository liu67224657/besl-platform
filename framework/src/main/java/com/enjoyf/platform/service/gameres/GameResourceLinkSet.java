package com.enjoyf.platform.service.gameres;

import com.enjoyf.platform.service.JsonBinder;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.reflect.ReflectUtil;
import com.google.common.base.Strings;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * <p/>
 * Description:
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>liuhao</a>
 */
public class GameResourceLinkSet implements Serializable {
    //
    private Set<GameResourceLink> links = new LinkedHashSet<GameResourceLink>();

    public GameResourceLinkSet() {

    }

    public Set<GameResourceLink> getLinks() {
        return links;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }

    public String toJsonStr() {
        return JsonBinder.buildNormalBinder().toJson(links);
    }

    public static GameResourceLinkSet parse(String jsonStr) {
        if (StringUtil.isEmpty(jsonStr)) {
            return null;
        }

        GameResourceLinkSet returnValue = new GameResourceLinkSet();

        if (!Strings.isNullOrEmpty(jsonStr)) {
            try {
                Set<GameResourceLink> links = JsonBinder.buildNormalBinder().getMapper().readValue(jsonStr, new TypeReference<LinkedHashSet<GameResourceLink>>() {
                });

                returnValue.getLinks().addAll(links);
            } catch (IOException e) {
                GAlerter.lab("GameResourceLinkSet parse error, jsonStr:" + jsonStr, e);
            }
        }

        return returnValue;
    }

}
