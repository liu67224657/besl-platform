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
public class SearchGameIndexEntry {

    public static final String IDX_KEY_GTID = "gtid";
    public static final String IDX_KEY_GTNAME = "gtname";
    public static final String IDX_KEY_GTCONTENT = "gtcontent";
    public static final String IDX_KEY_CREATEDATE = "createdate";
    public static final String IDX_KEY_SYNONYMS = "synonyms";
    public static final String IDX_KEY_GAMEPLATFORM = "gameplatform";
    public static final String IDX_KEY_DEVELOPCOMPANY = "developCompany";
    public static final String IDX_KEY_PUBLISHCOMPANY = "publishCompany";
    public static final String IDX_KEY_GTYPE = "gametagtype";


    //IDX_KEY_gtID,GTNAME,GTDIS,GTCREATEDATE
    private String gtId;
    private String gtName;
    private String gtcontent;
    private Date createdate;

    private String synonyms;
    private String gameplatform;
    private String develop;
    private String publishCompany;

    private ResourceDomain tagDomain;

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

    public String getGtcontent() {
        return gtcontent;
    }

    public void setGtcontent(String gtcontent) {
        this.gtcontent = gtcontent;
    }

    public Date getCreatedate() {
        return createdate;
    }

    public void setCreatedate(Date createdate) {
        this.createdate = createdate;
    }

    public String getSynonyms() {
        return synonyms;
    }

    public void setSynonyms(String synonyms) {
        this.synonyms = synonyms;
    }

    public String getGameplatform() {
        return gameplatform;
    }

    public void setGameplatform(String gameplatform) {
        this.gameplatform = gameplatform;
    }

    public String getDevelop() {
        return develop;
    }

    public void setDevelop(String develop) {
        this.develop = develop;
    }

    public String getPublishCompany() {
        return publishCompany;
    }

    public void setPublishCompany(String publishCompany) {
        this.publishCompany = publishCompany;
    }

    public ResourceDomain getTagDomain() {
        return tagDomain;
    }

    public void setTagDomain(ResourceDomain tagDomain) {
        this.tagDomain = tagDomain;
    }

}
