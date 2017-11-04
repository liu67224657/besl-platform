package com.enjoyf.webapps.joyme.dto.vote;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-10-20
 * Time: 下午3:21
 * To change this template use File | Settings | File Templates.
 */
public class WikiVoteDTO implements Serializable {
    private long id;
    private int votesum;
    private String url;
    private String name;
    private String pic;
    private String nonum;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getVotesum() {
        return votesum;
    }

    public void setVotesum(int votesum) {
        this.votesum = votesum;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getNonum() {
        return nonum;
    }

    public void setNonum(String nonum) {
        this.nonum = nonum;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
