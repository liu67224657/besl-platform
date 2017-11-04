package com.enjoyf.webapps.joyme.weblogic.message;

import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.service.message.Message;
import com.enjoyf.platform.service.message.MessageServiceSngl;
import com.enjoyf.platform.service.message.MessageTopic;
import com.enjoyf.platform.service.message.MessageType;
import com.enjoyf.platform.service.profile.Profile;
import com.enjoyf.platform.service.profile.ProfileServiceSngl;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.text.ResolveContent;
import com.enjoyf.platform.text.TextProcessorFatctory;
import com.enjoyf.platform.text.WordProcessorKey;
import com.enjoyf.platform.text.processor.TextProcessor;
import com.enjoyf.webapps.joyme.weblogic.entity.MessageProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 用户消息服务实现类
 *
 * @author xinzhao
 */
@Service(value = "userMessageWebLogic")
public class UserMessageWebLogic {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    
    public void sendMessage(Message message) throws ServiceException {
        logger.debug("sendMessage service impl");

        MessageServiceSngl.get().postMessage(message.getSenderUno(), message);
    }

    
    public PageRows<MessageProfile> queryReceiveMessageListByUno(String uno, Pagination page)
            throws ServiceException {

        PageRows<MessageProfile> pageRows = new PageRows();
        List reList = new ArrayList();

        PageRows<MessageTopic> rePage = MessageServiceSngl.get().querySenderTopics(uno, MessageType.PRIVATE, page);

        Set pUnoSet = new HashSet();
        for (MessageTopic messageTopic : rePage.getRows()) {
            pUnoSet.add(messageTopic.getReletionUno());
        }

        //
        Map<String, Profile> map = ProfileServiceSngl.get().queryProfilesByUnosMap(pUnoSet);


        //封装消息对象
        for (MessageTopic messageTopic : rePage.getRows()) {
            //过滤对象
            String tempBody = messageTopic.getLastestMessage().getBody();
            messageTopic.getLastestMessage().setBody(processMessage(tempBody));

            MessageProfile messageProfile = new MessageProfile(map.get(messageTopic.getReletionUno()), messageTopic);
            reList.add(messageProfile);
        }

        pageRows.setRows(reList);
        pageRows.setPage(rePage.getPage());

        return pageRows;
    }


    
    public PageRows<MessageProfile> queryReceiveMessageListByUno(String ownUno, String senderUno, MessageType type, Pagination page)
            throws ServiceException {

        PageRows<MessageProfile> pageRows = new PageRows();
        List reList = new ArrayList();

        PageRows<Message> rePage = MessageServiceSngl.get().queryMessagesBySender(ownUno, senderUno, type, page);

        Set pUnoSet = new HashSet();
        for (Message message : rePage.getRows()) {
            pUnoSet.add(message.getSenderUno());
        }
        Map<String, Profile> map = null;

        //查询每组会话对应人的信息
        List<Profile> profiles = ProfileServiceSngl.get().queryProfilesByUnos(pUnoSet);
        map = new HashMap<String, Profile>();
        for (Profile profile : profiles) {
            if (profile != null && profile.getBlog() != null && profile.getBlog().getUno() != null)
                if (!map.containsKey(profile.getBlog().getUno()))
                    map.put(profile.getBlog().getUno(), profile);
        }


        //封装消息对象
        for (Message message : rePage.getRows()) {
            //过滤对象
            message.setBody(processMessage(message.getBody()));

            MessageProfile messageProfile = new MessageProfile(map.get(message.getSenderUno()), message);
            reList.add(messageProfile);
        }

        pageRows.setRows(reList);
        pageRows.setPage(rePage.getPage());
        return pageRows;

    }

    private String processMessage( String content) {
        ResolveContent resolveContent = new ResolveContent();
        resolveContent.setContent(content);
        TextProcessor textProcessor = TextProcessorFatctory.get().getProcessorByKey(WordProcessorKey.KEY_PRIVIEW_MESSAGE);
        if (textProcessor != null) {
            resolveContent = textProcessor.process(resolveContent);
        }
        return resolveContent.getContent();
    }
}
