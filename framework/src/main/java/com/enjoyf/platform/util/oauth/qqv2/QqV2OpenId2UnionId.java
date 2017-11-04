package com.enjoyf.platform.util.oauth.qqv2;

import com.enjoyf.platform.service.misc.MiscServiceSngl;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.usercenter.*;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.UpdateExpress;

/**
 * @author <a href=mailto:ericliu@fivewh.com>ericliu</a>,Date:2016/11/16
 */
public class QqV2OpenId2UnionId {

    public static boolean openId2UnionId(String openId, String unionId) {
        //TODO 这里更新旧的逻辑
        try {
            UserLogin userLogin = MiscServiceSngl.get().getUserLogin(openId);
            if (userLogin != null) {

                //如果是Unioid查询的用户存在，直接删除redis缓存
                UserLogin userUid = UserCenterServiceSngl.get().getUserLoginByLoginKey(unionId, LoginDomain.QQ);
                if (userUid != null) {
//                    boolean bval = UserCenterServiceSngl.get().modifyUserLogin(new UpdateExpress().set(UserLoginField.UNO, userLogin.getUno()), unioidUserLogin.getLoginId());
//                    if (bval) {
                        MiscServiceSngl.get().deleteUserLogin(openId);
//                    }
                } else {
                    boolean bval = UserCenterServiceSngl.get().modifyUserLogin(new UpdateExpress()
                            .set(UserLoginField.LOGIN_ID, UserCenterUtil.getUserLoginId(unionId, LoginDomain.QQ))
                            .set(UserLoginField.LOGIN_KEY, unionId)
                            , userLogin.getLoginId());
                    //如果更新成功，删除redis里面的key，下次就不会进来了
                    if (bval) {
                        MiscServiceSngl.get().deleteUserLogin(openId);
                    }
                }
            }
        } catch (ServiceException e) {
            GAlerter.lab("QqV2OpenId2UnionId failed: openid: " + openId + " unionId: " + unionId);
        }
        return true;
    }
}
