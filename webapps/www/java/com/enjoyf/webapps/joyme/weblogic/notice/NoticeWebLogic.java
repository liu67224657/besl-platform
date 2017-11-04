package com.enjoyf.webapps.joyme.weblogic.notice;

import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.service.message.Message;
import com.enjoyf.platform.service.message.MessageServiceSngl;
import com.enjoyf.platform.service.message.MessageType;
import com.enjoyf.platform.service.message.Notice;
import com.enjoyf.platform.service.message.NoticeType;
import com.enjoyf.platform.service.profile.ProfileServiceSngl;
import com.enjoyf.platform.service.profile.ProfileSetting;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.webapps.joyme.weblogic.user.UserPrivilageWebLogic;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p/>
 * Description:系统消息，小纸条实现
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>liuhao</a>  ,zx
 */
@Service(value = "noticeWebLogic")
public class NoticeWebLogic {

    @Resource(name = "userPrivilageWebLogic")
    private UserPrivilageWebLogic userPrivilageWebLogic;

    /**
     * 查询用户未读的消息
     *
     * @param uno
     * @return
     * @throws ServiceException
     */
    
    public List<Notice> queryNotices(String uno) throws ServiceException {
        List<Notice> list = MessageServiceSngl.get().queryNotices(uno);
        List<Notice> reList = new ArrayList();
        ProfileSetting define = ProfileServiceSngl.get().getProfileSetting(uno);

        // 判断是否是未读的消息
        for (Notice notice : list) {
            if (notice.getCount() > 0 && userPrivilageWebLogic.isDisplayMemo(notice, define)) {
                reList.add(notice);
            }
        }
        return reList;
    }

    //关闭该条全员系统消息
    
    public boolean readNoticeByType(String uno, NoticeType noticeType) throws ServiceException {
        return MessageServiceSngl.get().readNoticeByType(uno, noticeType);

    }


    /**
     * 查询系统消息
     *
     * @param uno         用户ID
     * @param messageType 系统消息类型
     * @param pagination
     * @return
     * @throws ServiceException
     */
    
    public PageRows<Message> querySysnoticeByUnoAndType(String uno, MessageType messageType, Pagination pagination) throws ServiceException {
        return MessageServiceSngl.get().queryMessagesBySender(uno, null, messageType, pagination);
    }


    /**
     * 删除系统消息
     *
     * @param uno
     * @param id
     * @return
     * @throws ServiceException
     */
    
    public boolean deleteSysMessage(String uno, Long id) throws ServiceException {
        return MessageServiceSngl.get().removeMessage(uno, id);
    }

}
