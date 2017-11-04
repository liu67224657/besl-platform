package com.enjoyf.platform.service.content.social;

import java.io.Serializable;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  14-4-14 上午10:02
 * Description:
 */
public class SocialConetntAppImages implements Serializable {
    private String pic;
    private String pic_s;

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getPic_s() {
        return pic_s;
    }

    public void setPic_s(String pic_s) {
        this.pic_s = pic_s;
    }
}
