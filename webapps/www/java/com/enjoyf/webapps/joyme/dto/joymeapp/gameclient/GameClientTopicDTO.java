package com.enjoyf.webapps.joyme.dto.joymeapp.gameclient;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import java.io.Serializable;

/**
 * Created by zhimingli
 * Date: 2014/12/17
 * Time: 11:50
 */
public class GameClientTopicDTO implements Serializable {
    private String topicid;
    private String topicname;

    public String getTopicid() {
        return topicid;
    }

    public void setTopicid(String topicid) {
        this.topicid = topicid;
    }

    public String getTopicname() {
        return topicname;
    }

    public void setTopicname(String topicname) {
        this.topicname = topicname;
    }


    @Override
    public int hashCode() {
        return topicid.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        GameClientTopicDTO dto = (GameClientTopicDTO) o;
        if (this.getTopicid().equals(dto.getTopicid())) {
            return true;
        }

        return false;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
