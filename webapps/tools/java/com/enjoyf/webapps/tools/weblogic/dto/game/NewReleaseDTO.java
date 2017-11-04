package com.enjoyf.webapps.tools.weblogic.dto.game;

import com.enjoyf.platform.service.gameres.City;
import com.enjoyf.platform.service.gameres.NewRelease;
import com.enjoyf.platform.service.gameres.NewReleaseTag;
import com.enjoyf.platform.service.sync.ShareBaseInfo;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-8-22
 * Time: 上午11:54
 * To change this template use File | Settings | File Templates.
 */
public class NewReleaseDTO {

    private NewRelease newRelease;
    private List<NewReleaseTag> newReleaseTagList;
    private City city;
    private ShareBaseInfo shareBaseInfo;

    public NewRelease getNewRelease() {
        return newRelease;
    }

    public void setNewRelease(NewRelease newRelease) {
        this.newRelease = newRelease;
    }

    public List<NewReleaseTag> getNewReleaseTagList() {
        return newReleaseTagList;
    }

    public void setNewReleaseTagList(List<NewReleaseTag> newReleaseTagList) {
        this.newReleaseTagList = newReleaseTagList;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public ShareBaseInfo getShareBaseInfo() {
        return shareBaseInfo;
    }

    public void setShareBaseInfo(ShareBaseInfo shareBaseInfo) {
        this.shareBaseInfo = shareBaseInfo;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
