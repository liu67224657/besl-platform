package com.enjoyf.platform.service.joymeapp;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.ValidStatus;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-5-27
 * Time: 下午2:06
 * To change this template use File | Settings | File Templates.
 */
public class MessageDevice implements Serializable {
    private long deviceId;

    private String clientId;
    private String deviceToken;
    private String appKey;

    private long lastMsgId;
    private ActStatus publishStatus;

    private ValidStatus validStatus;

}
