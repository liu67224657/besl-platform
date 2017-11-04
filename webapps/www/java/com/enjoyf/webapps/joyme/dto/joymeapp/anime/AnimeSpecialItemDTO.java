package com.enjoyf.webapps.joyme.dto.joymeapp.anime;

/**
 * Created by IntelliJ IDEA.
 * User: pengxu
 * Date: 14-10-27
 * Time: 下午5:46
 * To change this template use File | Settings | File Templates.
 */
public class AnimeSpecialItemDTO {
    private String title;      //标题
    private String picurl;     //PICurl
    private String url;
    private String desc;
    private String domain; //视频来源
    private String m3u8;//M3U8URL
    private String replynum;//评论数量
	private String tvid;//视频ID
	private String tvnumber;//视频ID

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


	public String getPicurl() {
		return picurl;
	}

	public void setPicurl(String picurl) {
		this.picurl = picurl;
	}

	public String getTvnumber() {
		return tvnumber;
	}

	public void setTvnumber(String tvnumber) {
		this.tvnumber = tvnumber;
	}

	public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getM3u8() {
        return m3u8;
    }

    public void setM3u8(String m3u8) {
        this.m3u8 = m3u8;
    }

    public String getReplynum() {
        return replynum;
    }

    public void setReplynum(String replynum) {
        this.replynum = replynum;
    }

	public String getTvid() {
		return tvid;
	}

	public void setTvid(String tvid) {
		this.tvid = tvid;
	}
}
