package com.enjoyf.webapps.joyme.weblogic.user;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.message.Notice;
import com.enjoyf.platform.service.message.NoticeType;
import com.enjoyf.platform.service.profile.ProfileSetting;
import com.enjoyf.platform.service.service.ServiceException;
import org.springframework.stereotype.Service;

/**
 * <p/>
 * Description:
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>liuhao</a>
 */
@Service(value = "userPrivilageWebLogic")
public class UserPrivilageWebLogic {
    

    public boolean isDisplayMemo(Notice notice, ProfileSetting define) throws ServiceException {
        if(define==null){
            return true;
        }

        if (notice.getNoticeType().equals(NoticeType.NEW_AT) && define.getHintatme().equals(ActStatus.ACTED)) {
            return true;
        } else if (notice.getNoticeType().equals(NoticeType.NEW_FANS) && define.getHintmyfans().equals(ActStatus.ACTED)) {
            return true;
        } else if (notice.getNoticeType().equals(NoticeType.NEW_REPLY) && define.getHintmyfeedback().equals(ActStatus.ACTED)) {
            return true;
        } else if (notice.getNoticeType().equals(NoticeType.NEW_MESSAGE) && define.getHintmyletter().equals(ActStatus.ACTED)) {
            return true;
        } else if (notice.getNoticeType().equals(NoticeType.NEW_BULLETIN) && define.getHintmynotice().equals(ActStatus.ACTED)) {
            return true;
        } else if (notice.getNoticeType().equals(NoticeType.NEW_CONTENT)) {
            return true;
        } else if (notice.getNoticeType().equals(NoticeType.MAIL_AUTH)) {
            return true;
        } else if (notice.getNoticeType().equals(NoticeType.COMPLETE_PROFILE)) {
            return true;
        } else if (notice.getNoticeType().equals(NoticeType.BILLING_COIN)) {
            return true;
        }
        return false;
    }
}
