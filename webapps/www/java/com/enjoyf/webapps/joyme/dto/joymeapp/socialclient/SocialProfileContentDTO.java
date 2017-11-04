package com.enjoyf.webapps.joyme.dto.joymeapp.socialclient;

import com.enjoyf.webapps.joyme.dto.joymeapp.AppAdvertiseDTO;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-4-17
 * Time: 上午11:43
 * To change this template use File | Settings | File Templates.
 */
public class SocialProfileContentDTO {

    private int type = SocialResultType.CONTENT_RESULT.getCode();

    private AppAdvertiseDTO advertise;

    private ContentDTO content;

    private ProfileDTO profile;

	private AgreeListDTO agress;

	public AgreeListDTO getAgress() {
		return agress;
	}

	public void setAgress(AgreeListDTO agress) {
		this.agress = agress;
	}

	public ContentDTO getContent() {
        return content;
    }

    public void setContent(ContentDTO content) {
        this.content = content;
    }

    public ProfileDTO getProfile() {
        return profile;
    }

    public void setProfile(ProfileDTO profile) {
        this.profile = profile;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public AppAdvertiseDTO getAdvertise() {
        return advertise;
    }

    public void setAdvertise(AppAdvertiseDTO advertise) {
        this.advertise = advertise;
    }
}
