package com.enjoyf.webapps.joyme.weblogic.profile;

import com.enjoyf.platform.props.WebappConfig;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.usercenter.Profile;
import com.enjoyf.platform.service.usercenter.UserCenterServiceSngl;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.http.HttpClientManager;
import com.enjoyf.platform.util.http.HttpParameter;
import com.enjoyf.platform.util.http.HttpResult;
import com.enjoyf.platform.util.log.GAlerter;
import org.springframework.stereotype.Service;

/**
 * Created by zhimingli on 2017-1-3 0003.
 */
@Service(value = "customizeWebLogic")
public class CustomizeWebLogic {
    private HttpClientManager httpClient = new HttpClientManager();

    public void privacysave(String srcprofileid, String is_secretchat, int type, String desprofileid) throws ServiceException {

        Profile profile = UserCenterServiceSngl.get().getProfileByProfileId(srcprofileid);
        if (profile != null) {
            Profile desprofile = UserCenterServiceSngl.get().getProfileByProfileId(desprofileid);

            HttpParameter httpParameter[] = new HttpParameter[]{
                    new HttpParameter("action", "userboard"),
                    new HttpParameter("uid", profile.getUid()),
                    new HttpParameter("type", type),
                    new HttpParameter("is_secretchat", StringUtil.isEmpty(is_secretchat) ? "" : is_secretchat),
                    new HttpParameter("format", "json"),
                    new HttpParameter("tuid", desprofile == null ? "" : String.valueOf(desprofile.getUid())),
            };
            HttpResult httpResult = httpClient.post("http://wiki." + WebappConfig.get().getDomain() + "/home/api.php", httpParameter, null);

            GAlerter.lan("CustomizeWebLogic save result -->" + httpResult.getReponseCode());
        } else {
            GAlerter.lan("CustomizeWebLogic save result -->profile is null");
        }
    }
}
