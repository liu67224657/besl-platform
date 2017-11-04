package com.enjoyf.platform.webapps.common.dto.index;

import java.util.List;

/**
 * <p/>
 * Description:
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>ericliu</a>
 */
public class IndexProfileContentDTO {
    private List<ViewLineItemElementDTO> contents;//头条
    private ViewLineItemElementDTO profile; //结伴玩

    public List<ViewLineItemElementDTO> getContents() {
        return contents;
    }

    public void setContents(List<ViewLineItemElementDTO> contents) {
        this.contents = contents;
    }

    public ViewLineItemElementDTO getProfile() {
        return profile;
    }

    public void setProfile(ViewLineItemElementDTO profile) {
        this.profile = profile;
    }
}
