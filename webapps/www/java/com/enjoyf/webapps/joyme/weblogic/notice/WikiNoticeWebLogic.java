package com.enjoyf.webapps.joyme.weblogic.notice;

import com.enjoyf.platform.service.notice.AppNoticeSum;
import com.enjoyf.platform.service.notice.NoticeServiceSngl;
import com.enjoyf.platform.service.notice.NoticeType;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.usercenter.UserCenterServiceSngl;
import com.enjoyf.platform.service.usercenter.UserPrivacy;
import com.enjoyf.platform.service.usercenter.UserPrivacyPrivacyAlarm;
import com.enjoyf.platform.util.StringUtil;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author <a href=mailto:ericliu@fivewh.com>ericliu</a>,Date:16/9/22
 */
@Service
public class WikiNoticeWebLogic {
    private final String PRIVACY = "0";

    public Map<String, AppNoticeSum> queryNoticeSum(String profileId, String appkey, String version, String platform, Set<NoticeType> types) throws ServiceException {
        Map<String, AppNoticeSum> noticeSumMap = new HashMap<String, AppNoticeSum>();
        UserPrivacy userPrivacy = UserCenterServiceSngl.get().getUserPrivacy(profileId);
        if (userPrivacy == null || userPrivacy.getAlarmSetting() == null) {
            return noticeSumMap;
        }

        UserPrivacyPrivacyAlarm userPrivacyPrivacyAlarm = userPrivacy.getAlarmSetting();

        //查询未读消息数量
        noticeSumMap = NoticeServiceSngl.get().queryAppNoticeSum(profileId, appkey, types);
        if (noticeSumMap != null && !noticeSumMap.isEmpty()) {
            if (StringUtil.isEmpty(userPrivacyPrivacyAlarm.getAgreement()) || PRIVACY.equals(userPrivacyPrivacyAlarm.getAgreement())) {
                noticeSumMap.remove(NoticeType.AGREE.getCode());
            }
            if (StringUtil.isEmpty(userPrivacyPrivacyAlarm.getComment()) || PRIVACY.equals(userPrivacyPrivacyAlarm.getComment())) {
                noticeSumMap.remove(NoticeType.REPLY.getCode());
            }
            if (StringUtil.isEmpty(userPrivacyPrivacyAlarm.getFollow()) || PRIVACY.equals(userPrivacyPrivacyAlarm.getFollow())) {
                noticeSumMap.remove(NoticeType.FOLLOW.getCode());
            }
            if (StringUtil.isEmpty(userPrivacyPrivacyAlarm.getUserat()) || PRIVACY.equals(userPrivacyPrivacyAlarm.getUserat())) {
                noticeSumMap.remove(NoticeType.AT.getCode());
            }
            if (StringUtil.isEmpty(userPrivacyPrivacyAlarm.getSysteminfo()) || PRIVACY.equals(userPrivacyPrivacyAlarm.getSysteminfo())) {
                noticeSumMap.remove(NoticeType.SYSTEM.getCode());
            }
        }

        return noticeSumMap;
    }
}

