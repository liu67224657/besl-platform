package com.enjoyf.platform.service.gameres;

import com.enjoyf.platform.util.reflect.ReflectUtil;

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
public class GameResourceLink implements Serializable {
    private String id;
    private String linkName;
    private String linkUrl;
    private Set<String> linkTypes = new LinkedHashSet<String>();

    public String getLinkName() {
        return linkName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setLinkName(String linkName) {
        this.linkName = linkName;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public Set<String> getLinkTypes() {
        return linkTypes;
    }

    public void setLinkTypes(Set<String> linkTypes) {
        this.linkTypes = linkTypes;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
