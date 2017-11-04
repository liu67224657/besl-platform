package com.enjoyf.platform.webapps.common.dto.upload;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  14-4-14 上午9:59
 * Description:
 */
public class SocialAppMediaDto {
     private SocialAppImageDto pic;
     private SocialAppAudioDto audio;

    public SocialAppImageDto getPic() {
        return pic;
    }

    public void setPic(SocialAppImageDto pic) {
        this.pic = pic;
    }

    public SocialAppAudioDto getAudio() {
        return audio;
    }

    public void setAudio(SocialAppAudioDto audio) {
        this.audio = audio;
    }
}
