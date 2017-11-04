package com.enjoyf.platform.service.search;

/**
 * <p/>
 * Description:
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>liuhao</a>
 */
public class SearchGiftIndexEntry {

    public static final String IDX_KEY_GTID = "aid";
    public static final String IDX_KEY_GTNAME = "name";


    //IDX_KEY_gtID,GTNAME,GTDIS,GTCREATEDATE
    private String aId;
    private String name;

    public String getaId() {
        return aId;
    }

    public void setaId(String aId) {
        this.aId = aId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
