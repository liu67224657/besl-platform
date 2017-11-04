package com.enjoyf.platform.serv.viewline.messageprocessor;

import com.enjoyf.platform.props.WebappConfig;
import com.enjoyf.platform.props.hotdeploy.HotdeployConfigFactory;
import com.enjoyf.platform.props.hotdeploy.TemplateHotdeployConfig;
import com.enjoyf.platform.props.hotdeploy.WebHotdeployConfig;
import com.enjoyf.platform.serv.viewline.ViewLineLogic;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.content.Content;
import com.enjoyf.platform.service.content.ContentServiceSngl;
import com.enjoyf.platform.service.event.system.ViewLineItemPostSystemMessageEvent;
import com.enjoyf.platform.service.gameres.GameRelationField;
import com.enjoyf.platform.service.gameres.GameRelationType;
import com.enjoyf.platform.service.gameres.GameResource;
import com.enjoyf.platform.service.gameres.GameResourceServiceSngl;
import com.enjoyf.platform.service.message.Message;
import com.enjoyf.platform.service.message.MessageServiceSngl;
import com.enjoyf.platform.service.message.MessageType;
import com.enjoyf.platform.service.profile.Profile;
import com.enjoyf.platform.service.profile.ProfileServiceSngl;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.viewline.*;
import com.enjoyf.platform.util.*;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.QuerySort;
import com.enjoyf.platform.util.sql.QuerySortOrder;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 12-8-13
 * Time: 上午11:21
 * To change this template use File | Settings | File Templates.
 */
public class BaikeMessageProcessor extends AbstractMessageProcessor {

    public BaikeMessageProcessor(ViewLineLogic viewLineLogic) {
        super(viewLineLogic);
    }

    @Override
    public void postSystemMessage(ViewLineItemPostSystemMessageEvent event) {
        GAlerter.lad("process baike message." + event);

        try {
            if (StringUtil.isEmpty(event.getDirectId()) || StringUtil.isEmpty(event.getDirectUno())) {
                GAlerter.lad("process baike message.directId is null.");
                return;
            }

            Set<String> unoSets = new HashSet<String>();

            //content
            Content content = ContentServiceSngl.get().getContentById(event.getDirectId());

            //profile
            unoSets.add(event.getDirectUno());
            unoSets.add(event.getCreateUno());
            Map<String, Profile> profileMap = ProfileServiceSngl.get().queryProfilesByUnosMap(unoSets);
            Profile contentProfile = profileMap.get(event.getDirectUno());

            Profile createProfile = null;
            if (!StringUtil.isEmpty(event.getCreateUno())) {
                createProfile = profileMap.get(event.getCreateUno());
            }

            ViewCategory baikeCategory = getTopCategory(event.getCategoryId());

            //has first insert
            PageRows<ViewLineLog> logRows = viewLineLogic.queryLineLogs(new QueryExpress()
                    .add(QueryCriterions.eq(ViewLineLogField.SRCID, baikeCategory.getCategoryId()))
                    .add(QueryCriterions.eq(ViewLineLogField.SUBDOMAIN, ViewLineLogDomain.CATEGORY.getCode()))
                    .add(QuerySort.add(ViewLineLogField.CREATEDATE, QuerySortOrder.DESC)), new Pagination(1, 1, 1));

            if (logRows != null && !CollectionUtil.isEmpty(logRows.getRows())) {
                String logContent = logRows.getRows().get(0).getLogContent();
                GAlerter.lad("logContent:" + logContent);

                if (logContent.indexOf(event.getDirectId()) > 0) {
                    GAlerter.lad("baike item is not first insert.");
                    return;
                }
            }

            //express
            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(GameRelationField.VALIDSTATUS, ValidStatus.VALID.getCode()));
            queryExpress.add(QueryCriterions.eq(GameRelationField.RELATIONTYPE, GameRelationType.GAME_RELATION_TYPE_BAIKE.getCode()));
            queryExpress.add(QueryCriterions.eq(GameRelationField.RELATIONVALUE, String.valueOf(baikeCategory.getCategoryId())));
            List<GameResource> gameResourceList = GameResourceServiceSngl.get().queryGameResourceByRelationQueryExpress(queryExpress);
            GameResource gameResource = gameResourceList.get(0);

            String URL_WWW = WebappConfig.get().getUrlWww();
            String DOMAIN = WebappConfig.get().getDomain();
            String contentId = content.getContentId();
            String title = StringUtil.isEmpty(content.getSubject()) ?
                    HotdeployConfigFactory.get().getConfig(WebHotdeployConfig.class).getContentSubjectEmptyText() :
                    content.getSubject();
            String contentDomain = contentProfile.getBlog().getDomain();
            String gameCode = gameResource.getGameCode();
            String gameName = gameResource.getResourceName();

            TemplateHotdeployConfig templateConfig = HotdeployConfigFactory.get().getConfig(TemplateHotdeployConfig.class);


            Map<String, String> paramMap = new HashMap<String, String>();
            paramMap.put("URL_WWW", URL_WWW);
            paramMap.put("DOMAIN", DOMAIN);
            paramMap.put("contentId", contentId);
            paramMap.put("title", title);
            paramMap.put("contentDomain", contentDomain);
            paramMap.put("gameCode", gameCode);
            paramMap.put("gameName", gameName);

            String messageBody;
            if (createProfile != null) {
                paramMap.put("createDomain", createProfile.getBlog().getDomain());
                paramMap.put("createName", createProfile.getBlog().getScreenName());
                messageBody = NamedTemplate.parse(templateConfig.getBaikeSystemMessageTemplate()).format(paramMap);
            } else {
                messageBody = NamedTemplate.parse(templateConfig.getBaikeNotCreateMessageTemplate()).format(paramMap);
            }

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

}
