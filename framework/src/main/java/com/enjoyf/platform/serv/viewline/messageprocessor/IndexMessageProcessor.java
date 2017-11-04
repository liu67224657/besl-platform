package com.enjoyf.platform.serv.viewline.messageprocessor;

import com.enjoyf.platform.props.WebappConfig;
import com.enjoyf.platform.props.hotdeploy.HotdeployConfigFactory;
import com.enjoyf.platform.props.hotdeploy.TemplateHotdeployConfig;
import com.enjoyf.platform.props.hotdeploy.WebHotdeployConfig;
import com.enjoyf.platform.serv.viewline.ViewLineLogic;
import com.enjoyf.platform.service.content.Content;
import com.enjoyf.platform.service.content.ContentServiceSngl;
import com.enjoyf.platform.service.event.system.ViewLineItemPostSystemMessageEvent;
import com.enjoyf.platform.service.message.Message;
import com.enjoyf.platform.service.message.MessageServiceSngl;
import com.enjoyf.platform.service.message.MessageType;
import com.enjoyf.platform.service.profile.Profile;
import com.enjoyf.platform.service.profile.ProfileServiceSngl;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.viewline.*;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.NamedTemplate;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 12-8-23
 * Time: 下午1:18
 * To change this template use File | Settings | File Templates.
 */
public class IndexMessageProcessor extends AbstractMessageProcessor {
    private static Set<String> sendMessageCategoryCodeSet = new HashSet<String>();

    private static Map<String, ViewCategory> sendMessageCategoryCategorieMap = new HashMap<String, ViewCategory>();

    static {
//        sendMessageCategoryCodeSet.add("topic");
        sendMessageCategoryCodeSet.add("news");
//        sendMessageCategoryCodeSet.add("headlines");
    }

    public IndexMessageProcessor(ViewLineLogic viewLineLogic) {
        super(viewLineLogic);
    }

    @Override
    public void postSystemMessage(ViewLineItemPostSystemMessageEvent event) {
        //不是文章
        if (StringUtil.isEmpty(event.getDirectId()) && StringUtil.isEmpty(event.getDirectUno())) {
            return;
        }

        try {
            //
            ViewLine viewLine = viewLineLogic.getViewLine(new QueryExpress().add(QueryCriterions.eq(ViewLineField.LINEID, event.getLineId())));
            if (viewLine == null || !viewLine.getItemType().equals(ViewItemType.CONTENT)) {
                return;
            }

            //分类为空，在忽略列表中
            ViewCategory category = getTopCategory(viewLine.getCategoryId());
            if (category == null || !sendMessageCategoryCodeSet.contains(category.getCategoryCode())) {
                return;
            }

            //排重
            if (isRepeatSendMessageItem(category.getCategoryCode(), event.getDirectId(), event.getDirectUno())) {
                return;
            }

            //content
            Content content = ContentServiceSngl.get().getContentById(event.getDirectId());

            //profile
            Profile contentProfile = ProfileServiceSngl.get().getProfileByUno(event.getDirectUno());

            String title = StringUtil.isEmpty(content.getSubject()) ?
                    HotdeployConfigFactory.get().getConfig(WebHotdeployConfig.class).getContentSubjectEmptyText() :
                    content.getSubject();

            TemplateHotdeployConfig templateConfig = HotdeployConfigFactory.get().getConfig(TemplateHotdeployConfig.class);

            Map<String, String> paramMap = new HashMap<String, String>();
            paramMap.put("URL_WWW", WebappConfig.get().getUrlWww());
            paramMap.put("contentId", content.getContentId());
            paramMap.put("title", title);
            paramMap.put("contentDomain", contentProfile.getBlog().getDomain());

            String messageBody = NamedTemplate.parse(templateConfig.getIndexSystemMessageTemplate()).format(paramMap);

            Message message = new Message();
            message.setBody(messageBody);
            message.setMsgType(MessageType.OPERATION);
            message.setOwnUno(event.getDirectUno());
            message.setRecieverUno(event.getDirectUno());
            message.setSendDate(new Date());

            MessageServiceSngl.get().postMessage(event.getDirectUno(), message);
        } catch (ServiceException e) {
            GAlerter.lab("send system message error.ServiceException: ", e);
        } catch (Exception e) {
            GAlerter.lab("send system message error.Exception:", e);
        }
    }

    //排重
    private boolean isRepeatSendMessageItem(String itemCategoryCode, String itemDirectId, String itemDirectUno) {
        initSendMessageLogic();

        //查询条件
        Map<String,ViewCategory> otherSendMessageCateogryCode = new HashMap<String,ViewCategory>();
        for (Map.Entry<String,ViewCategory> categoryEntry : sendMessageCategoryCategorieMap.entrySet()) {
            if (!categoryEntry.getKey().equals(itemCategoryCode)) {
                otherSendMessageCateogryCode.put(categoryEntry.getKey(),categoryEntry.getValue());
            }
        }

        QueryCriterions[] sendMessageCategoryCodeCriterions = new QueryCriterions[otherSendMessageCateogryCode.size()];

        int i = 0;
        for (Map.Entry<String,ViewCategory> categoryEntry : otherSendMessageCateogryCode.entrySet()) {
            sendMessageCategoryCodeCriterions[i] = QueryCriterions.eq(ViewCategoryField.CATEGORYID, categoryEntry.getValue().getCategoryId());
            i++;
        }

        try {
            List<ViewLineItem> viewLineItems = viewLineLogic.queryLineItems(new QueryExpress().add(QueryCriterions.eq(ViewLineItemField.DIRECTID, itemDirectId))
                    .add(QueryCriterions.eq(ViewLineItemField.DIRECTUNO, itemDirectUno))
                    .add(QueryCriterions.or(sendMessageCategoryCodeCriterions)));

            return !CollectionUtil.isEmpty(viewLineItems);
        } catch (ServiceException e) {
            GAlerter.lab("send isRepeatSendMessageItem error.ServiceException: ", e);
        }

        return true;
    }

    private void initSendMessageLogic() {
        if (sendMessageCategoryCodeSet.size() != sendMessageCategoryCategorieMap.size()) {

            for (String categoryCode : sendMessageCategoryCodeSet) {
                try {
                    ViewCategory viewCategory = viewLineLogic.getCategory(new QueryExpress().add(QueryCriterions.eq(ViewCategoryField.CATEGORYCODE, categoryCode)));

                    if (viewCategory == null) {
                        GAlerter.lab(getClass().getName() + " init SendMessageMap viewcategoty null.categoryCode: " + categoryCode);
                        continue;
                    }

                    sendMessageCategoryCategorieMap.put(categoryCode, viewCategory);
                } catch (ServiceException e) {
                    GAlerter.lab(getClass().getName() + " init SendMessageMap viewcategoty null.categoryCode: " + categoryCode+" e:", e);
                }
            }

        }
    }
}
