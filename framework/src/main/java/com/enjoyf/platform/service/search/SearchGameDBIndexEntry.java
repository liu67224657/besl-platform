package com.enjoyf.platform.service.search;

import com.enjoyf.platform.service.gameres.ResourceDomain;

import java.util.Date;

/**
 * <p/>
 * Description:
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>liuhao</a>
 */
public class SearchGameDBIndexEntry {

    public static final String IDX_KEY_GTID = "gtid";
    public static final String IDX_KEY_GTNAME = "gtname";


    //IDX_KEY_gtID,GTNAME,GTDIS,GTCREATEDATE
    private String gtId;
    private String gtName;

    public String getGtId() {
        return gtId;
    }

    public void setGtId(String gtId) {
        this.gtId = gtId;
    }

    public String getGtName() {
        return gtName;
    }

    public void setGtName(String gtName) {
        this.gtName = gtName;
    }
}
