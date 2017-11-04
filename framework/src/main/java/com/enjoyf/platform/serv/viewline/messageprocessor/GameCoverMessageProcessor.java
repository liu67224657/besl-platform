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
import com.enjoyf.platform.service.gameres.*;
import com.enjoyf.platform.service.message.Message;
import com.enjoyf.platform.service.message.MessageServiceSngl;
import com.enjoyf.platform.service.message.MessageType;
import com.enjoyf.platform.service.profile.Profile;
import com.enjoyf.platform.service.profile.ProfileServiceSngl;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.viewline.ViewCategory;
import com.enjoyf.platform.util.NamedTemplate;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 12-8-13
 * Time: 上午11:21
 * To change this template use File | Settings | File Templates.
 */

public class GameCoverMessageProcessor extends AbstractMessageProcessor {

    public GameCoverMessageProcessor(ViewLineLogic viewLineLogic) {
        super(viewLineLogic);
    }

    @Override
    public void postSystemMessage(ViewLineItemPostSystemMessageEvent event) {
        try {
            if (StringUtil.isEmpty(event.getDirectId()) && StringUtil.isEmpty(event.getDirectUno())) {
                return;
            }

            Set<String> unoSets = new HashSet<String>();

            //content
            Content content = ContentServiceSngl.get().getContentById(event.getDirectId());

            //profile
            unoSets.add(event.getDirectUno());
            Map<String, Profile> profileMap = ProfileServiceSngl.get().queryProfilesByUnosMap(unoSets);
            Profile contentProfile = profileMap.get(event.getDirectUno());

            ViewCategory coveryCategory = getCategory(event.getCategoryId());
            if (coveryCategory == null) {
                //todo
                return;
            }

            //express
            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(GameRelationField.VALIDSTATUS, ValidStatus.VALID.getCode()));
            queryExpress.add(QueryCriterions.eq(GameRelationField.RELATIONTYPE, GameRelationType.GAME_RELATION_TYPE_COVER.getCode()));
            queryExpress.add(QueryCriterions.eq(GameRelationField.RELATIONVALUE, String.valueOf(coveryCategory.getCategoryId())));
            List<GameResource> gameResourceList = GameResourceServiceSngl.get().queryGameResourceByRelationQueryExpress(queryExpress);
            GameResource gameResource = gameResourceList.get(0);

            String URL_WWW = WebappConfig.get().getUrlWww();
            String DOMAIN = WebappConfig.get().getDomain();

            String contentId = content.getContentId();
            String title = StringUtil.isEmpty(content.getSubject()) ?
                    HotdeployConfigFactory.get().getConfig(WebHotdeployConfig.class).getContentSubjectEmptyText() :
                    content.getSubject();
            String contentDomain = contentProfile.getBlog().getDomain();

            String boardLink = gameResource.getResourceDomain().equals(ResourceDomain.GAME) ?
                    URL_WWW + "/game/" + gameResource.getGameCode() :
                    URL_WWW + "/board/" + gameResource.getGameCode();
            String gameName = gameResource.getResourceName();

            TemplateHotdeployConfig templateConfig = HotdeployConfigFactory.get().getConfig(TemplateHotdeployConfig.class);

            Map<String, String> paramMap = new HashMap<String, String>();
            paramMap.put("URL_WWW", URL_WWW);
            paramMap.put("DOMAIN", DOMAIN);
            paramMap.put("contentId", contentId);
            paramMap.put("title", title);
            paramMap.put("contentDomain", contentDomain);
            paramMap.put("boardLink", boardLink);
            paramMap.put("gameName", gameName);

            String messageBody = NamedTemplate.parse(templateConfig.getGameCoverSystemMessageTemplate()).format(paramMap);

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
