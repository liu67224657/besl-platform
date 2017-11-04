package com.enjoyf.platform.service.message;

import com.enjoyf.platform.service.event.Event;
import com.enjoyf.platform.service.event.EventReceiver;
import com.enjoyf.platform.service.joymeapp.PushListType;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.service.rpc.RPC;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.RangeRows;
import com.enjoyf.platform.util.Rangination;
import com.enjoyf.platform.util.sql.ObjectField;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-8-17 下午4:29
 * Description:
 */
public interface MessageService extends EventReceiver {
    //the message apis
    //post a message
    @RPC(partitionHashing = 0)
    public void postMessage(String ownUno, Message message) throws ServiceException;

    @RPC(partitionHashing = 0)
    public Message getMessage(String ownUno, QueryExpress queryExpress) throws ServiceException;

    @RPC(partitionHashing = 0)
    public PageRows<Message> queryMessage(String ownUno, QueryExpress queryExpress, Pagination page) throws ServiceException;

    @RPC(partitionHashing = 0)
    public RangeRows<Message> queryMessage(String ownUno, QueryExpress queryExpress, Rangination rangination) throws ServiceException;

    @RPC(partitionHashing = 0)
    public boolean updateMessage(String ownUno, UpdateExpress updateExpress, QueryExpress queryExpress) throws ServiceException;

    //query message topics.
    @RPC(partitionHashing = 0)
    public PageRows<MessageTopic> queryMessageTopics(String ownUno, MessageType type, Pagination page) throws ServiceException;

    //query message topics.
    @RPC(partitionHashing = 0)
    public PageRows<MessageTopic> querySenderTopics(String ownUno, MessageType type, Pagination page) throws ServiceException;

    //query messages. system message's sender uno is null opr empty.
    @RPC(partitionHashing = 0)
    public PageRows<Message> queryMessagesBySender(String ownUno, String senderUno, MessageType type, Pagination page) throws ServiceException;

    @RPC(partitionHashing = 0)
    public PageRows<Message> queryMessagesByPmd(String ownUno, String senderUno, MessageType type, Pagination page) throws ServiceException;

    //query messages.
    @RPC(partitionHashing = 0)
    public PageRows<Message> queryMessagesByTopic(String ownUno, Long topicId, Pagination page) throws ServiceException;

    //remove message
    @RPC(partitionHashing = 0)
    public boolean removeMessage(String ownUno, Long msgId) throws ServiceException;

    //remove messages in a topic
    @RPC(partitionHashing = 0)
    public boolean removeTopicMessages(String ownUno, Long topicId) throws ServiceException;

    //remove messages from a sender
    @RPC(partitionHashing = 0)
    public boolean removeSenderMessages(String ownUno, String senderUno) throws ServiceException;

    //post a notice
    @RPC(partitionHashing = 0)
    public Notice postNotice(String ownUno, Notice notice) throws ServiceException;

    //query avalibale notice, the count is greater than zero.
    @RPC(partitionHashing = 0)
    public List<Notice> queryNotices(String ownUno) throws ServiceException;

    //query avalibale type notice, the count is greater than zero.
    @RPC(partitionHashing = 0)
    public Notice getNotice(String ownUno, NoticeType type) throws ServiceException;

    //read notice
    @RPC(partitionHashing = 0)
    public boolean readNoticeAll(String ownUno) throws ServiceException;

    @RPC(partitionHashing = 0)
    public boolean readNoticeAllExcludeNC(String ownUno) throws ServiceException;

    @RPC(partitionHashing = 0)
    public boolean readNoticeByType(String ownUno, NoticeType type) throws ServiceException;

    @RPC(partitionHashing = 0)
    public boolean updateNoticeKeyValues(String ownUno, NoticeType type, Map<ObjectField, Object> keyValues) throws ServiceException;

    //create PushMessage
    @RPC(partitionHashing = 0)
    public PushMessage create(PushMessage entry) throws ServiceException;

    //get PushMessage
    @RPC(partitionHashing = 0)
    public PushMessage get(QueryExpress queryExpress) throws ServiceException;

    //query PushMessage
    @RPC(partitionHashing = 0)
    public PageRows<PushMessage> query(QueryExpress queryExpress, Pagination page) throws ServiceException;

    @RPC(partitionHashing = 0)
    public RangeRows<PushMessage> query(QueryExpress queryExpress, Rangination range) throws ServiceException;


    //recieve the player event
    @RPC(partitionHashing = 0)
    public boolean receiveEvent(Event event) throws ServiceException;

    //post notice event
    @RPC(partitionHashing = 0)
    public void postNoticeEvent(Notice notice) throws ServiceException;

    @RPC(partitionHashing = 0)
    public Notice getNoticeByCache(String uno, NoticeType noticeType) throws ServiceException;

    @RPC(partitionHashing = 0)
    public ClientDevice createClientDevice(ClientDevice mobileDevice) throws ServiceException;

    @RPC(partitionHashing = 0)
    public ClientDevice getClientDevice(QueryExpress queryExpress) throws ServiceException;

    @RPC(partitionHashing = 0)
    public List<ClientDevice> queryClientDevice(QueryExpress add) throws ServiceException;

    @RPC(partitionHashing = 0)
    public PageRows<ClientDevice> queryClientDeviceByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException;

    @RPC(partitionHashing = 0)
    public PageRows<SocialMessage> querySocialMessageList(String ownUno, SocialMessageType socialMessageType, Pagination page, boolean desc) throws ServiceException;

    @RPC(partitionHashing = 0)
    public PageRows<SocialMessage> queryWanbaSocialMessageList(String ownUno, SocialMessageType socialMessageType, Pagination page) throws ServiceException;

    @RPC(partitionHashing = 0)
    public SocialMessage getSocialMessage(String ownUno, SocialMessageType socialMessageType, SocialMessageCategory socialMessageCategory) throws ServiceException;

    @RPC(partitionHashing = 0)
    public SocialMessage createSocialMessage(SocialMessage socialMessage) throws ServiceException;

    @RPC(partitionHashing = 0)
    public boolean removeSocialMessage(long msgId, String uno) throws ServiceException;

    @RPC(partitionHashing = 0)
    public SocialNotice getSocialNotice(String ownUno) throws ServiceException;

    @RPC(partitionHashing = 0)
    public boolean modifySocialNotice(String uno, int messageType) throws ServiceException;

    @RPC(partitionHashing = 0)
    public void sendSociailPushMessage(com.enjoyf.platform.service.joymeapp.PushMessage pushMessage) throws ServiceException;

    @RPC(partitionHashing = 0)
    public boolean modifySocialMessage(String ownUno, QueryExpress queryExpress, UpdateExpress updateExpress) throws ServiceException;

    @RPC(partitionHashing = 0)
    public List<com.enjoyf.platform.service.joymeapp.PushMessage> queryPushMessageByCache(String appKey, int platform, String uno, Long timestamp, PushListType pushListType) throws ServiceException;

    @RPC(partitionHashing = 0)
    public PageRows<com.enjoyf.platform.service.joymeapp.PushMessage> queryPushMessage(QueryExpress queryExpress, Pagination pagination) throws ServiceException;

    @RPC(partitionHashing = 0)
    public com.enjoyf.platform.service.joymeapp.PushMessage ceatePushMessage(com.enjoyf.platform.service.joymeapp.PushMessage pushMessage) throws ServiceException;

    @RPC(partitionHashing = 0)
    public boolean sendPushMessage(com.enjoyf.platform.service.joymeapp.PushMessage pushMessage) throws ServiceException;

    @RPC(partitionHashing = 0)
    public boolean modifyPushMessage(UpdateExpress updateExpress, QueryExpress queryExpress, String appKey) throws ServiceException;

    @RPC(partitionHashing = 0)
    public void pushMessageNoticeTime(String key, String value) throws ServiceException;

    @RPC(partitionHashing = 0)
    public Long getMessageNoticeTime(String key) throws ServiceException;

    @RPC(partitionHashing = 0)
    public com.enjoyf.platform.service.joymeapp.PushMessage getPushMessage(QueryExpress queryExpress) throws ServiceException;

    @RPC(partitionHashing = 0)
    public boolean removePushMessage(Long msgId) throws ServiceException;


    @RPC(partitionHashing = 0)
    public void setTORedis(String key, String value) throws ServiceException;

    @RPC(partitionHashing = 0)
    public String getRedis(String key) throws ServiceException;

    @RPC(partitionHashing = 0)
    public void setTORedisIncr(String key, long value, int timeOutSec) throws ServiceException;
}
