package com.enjoyf.platform.service.advertise;

import com.enjoyf.platform.util.oauth.renren.api.client.utils.Md5Utils;

/**
 * Created by ericliu on 16/3/7.
 */
public class AdvertiseUtil {

    public static String getAgentId(AgentCode agentCode) {
        return Md5Utils.md5(agentCode.getCode());
    }
}
