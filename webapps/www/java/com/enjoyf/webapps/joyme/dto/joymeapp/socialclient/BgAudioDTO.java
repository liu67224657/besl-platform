package com.enjoyf.webapps.joyme.dto.joymeapp.socialclient;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-6-4
 * Time: 上午10:06
 * To change this template use File | Settings | File Templates.
 */
public class BgAudioDTO {

    private long audioid;
    private String audioname;
    private String audiopic;
    private String description;
    private String singer;
    private String audiosrc;
    private int usesum = 0;
    private SubscriptDTO subscript;

    public long getAudioid() {
        return audioid;
    }

    public void setAudioid(long audioid) {
        this.audioid = audioid;
    }

    public String getAudioname() {
        return audioname;
    }

    public void setAudioname(String audioname) {
        this.audioname = audioname;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public String getAudiosrc() {
        return audiosrc;
    }

    public void setAudiosrc(String audiosrc) {
        this.audiosrc = audiosrc;
    }

    public int getUsesum() {
        return usesum;
    }

    public void setUsesum(int usesum) {
        this.usesum = usesum;
    }

    public String getAudiopic() {
        return audiopic;
    }

    public void setAudiopic(String audiopic) {
        this.audiopic = audiopic;
    }

    public SubscriptDTO getSubscript() {
        return subscript;
    }

    public void setSubscript(SubscriptDTO subscript) {
        this.subscript = subscript;
    }
}
