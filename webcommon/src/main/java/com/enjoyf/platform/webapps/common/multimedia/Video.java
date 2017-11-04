package com.enjoyf.platform.webapps.common.multimedia;

import com.enjoyf.platform.util.reflect.ReflectUtil;

public class Video {
	private String flash;
	private String pic;
    private String bigpic;
	private String time;
	private String description;
	private String tag;
    private String orgUrl;

    public String getBigpic() {
        return bigpic;
    }

    public void setBigpic(String bigpic) {
        this.bigpic = bigpic;
    }

    public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getFlash() {
		return flash;
	}

	public void setFlash(String flash) {
		this.flash = flash;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

    public String getOrgUrl() {
        return orgUrl;
    }

    public void setOrgUrl(String orgUrl) {
        this.orgUrl = orgUrl;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
