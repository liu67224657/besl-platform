package com.enjoyf.webapps.joyme.dto.joymeapp.socialclient;

/**
 * Created with IntelliJ IDEA.
 * User: zhimingli
 * Date: 14-8-21
 * Time: 上午9:24
 * To change this template use File | Settings | File Templates.
 */
public class SocialShareDTO {
	private boolean isdefault;			//是否是默认
	private String sharedomain;        //第三方平台
	private String title;        			//标题
	private String description; 			//描述
	private String pic;          			//图片地址
	private String url;          			//地址
	private Long shareflag;              //是否有新内容

	public boolean isIsdefault() {
		return isdefault;
	}

	public void setIsdefault(boolean isdefault) {
		this.isdefault = isdefault;
	}

	public String getSharedomain() {
		return sharedomain;
	}

	public void setSharedomain(String sharedomain) {
		this.sharedomain = sharedomain;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Long getShareflag() {
		return shareflag;
	}

	public void setShareflag(Long shareflag) {
		this.shareflag = shareflag;
	}
}
