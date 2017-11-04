package com.enjoyf.platform.service.social;

import com.enjoyf.platform.util.oauth.renren.api.client.utils.Md5Utils;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 14/12/11
 * Description:
 */
public class SocialUtil {

    public static String getSocialRelationId(String srcId, String destId, String porfileKey, ObjectRelationType relationType) {
        return Md5Utils.md5(srcId + destId + porfileKey + relationType.getCode()).toLowerCase();
    }
}
