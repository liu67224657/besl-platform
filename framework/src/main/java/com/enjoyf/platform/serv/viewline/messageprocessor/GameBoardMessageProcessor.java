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
import com.enjoyf.platform.service.viewline.*;
import com.enjoyf.platform.util.*;
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

public class GameBoardMessageProcessor extends AbstractMessageProcessor {

    public GameBoardMessageProcessor(ViewLineLogic viewLineLogic) {
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

            if (!StringUtil.isEmpty(event.getCreateUno())) {
                unoSets.add(event.getCreateUno());
            }

            Map<String, Profile> profileMap = ProfileServiceSngl.get().queryProfilesByUnosMap(unoSets);
            Profile contentProfile = profileMap.get(event.getDirectUno());

            Profile createProfile = null;
            if (!StringUtil.isEmpty(event.getCreateUno())) {
                createProfile = profileMap.get(event.getCreateUno());
            }

            ViewCategory boardCategory = getCategory(event.getCategoryId());
            if (boardCategory == null) {
                //todo
                return;
            }

            //不是精华
            if (!LocationCode.ESS_BOARD.getCode().equals(boardCategory.getLocationCode()) && !LocationCode.COUSTOM_MODULE.getCode().equals(boardCategory.getLocationCode())) {
                return;
            }


            ViewCategory boardTopCategory = getTopCategory(event.getCategoryId());

            //express
            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(GameRelationField.VALIDSTATUS, ValidStatus.VALID.getCode()));
            queryExpress.add(QueryCriterions.eq(GameRelationField.RELATIONTYPE, GameRelationType.GAME_RELATION_TYPE_BOARD.getCode()));
            queryExpress.add(QueryCriterions.eq(GameRelationField.RELATIONVALUE, String.valueOf(boardTopCategory.getCategoryId())));
            List<GameResource> gameResourceList = GameResourceServiceSngl.get().queryGameResourceByRelationQueryExpress(queryExpress);
            GameResource gameResource = gameResourceList.get(0);

            //todo
            String messageBody = null;
            if (ResourceDomain.GAME.equals(gameResource.getResourceDomain())) {
                messageBody = getGameMessageBody(gameResource, boardCategory, content, contentProfile);
            } else if (ResourceDomain.GROUP.equals(gameResource.getResourceDomain())) {
                messageBody = getGroupMessageBody(gameResource, content, contentProfile, createProfile);
            } else {
                GAlerter.lab("send system notice resouce domain is not game or group. gameResource:" + gameResource);
            }

            if (StringUtil.isEmpty(messageBody)) {
                return;
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

    private String getGroupMessageBody(GameResource groupResouce, Content content, Profile contentProfile, Profile createProfile) {
        String URL_WWW = WebappConfig.get().getUrlWww();
        String DOMAIN = WebappConfig.get().getDomain();

        String contentId = content.getContentId();
        String title = StringUtil.isEmpty(content.getSubject()) ?
                HotdeployConfigFactory.get().getConfig(WebHotdeployConfig.class).getContentSubjectEmptyText() :
                content.getSubject();
        String contentDomain = contentProfile.getBlog().getDomain();


        String boardLink = URL_WWW + "/group/" + groupResouce.getGameCode();
        String gameName = groupResouce.getResourceName();

        TemplateHotdeployConfig templateConfig = HotdeployConfigFactory.get().getConfig(TemplateHotdeployConfig.class);

        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("URL_WWW", URL_WWW);
        paramMap.put("DOMAIN", DOMAIN);
        paramMap.put("contentId", contentId);
        paramMap.put("title", title);
        paramMap.put("contentDomain", contentDomain);
        paramMap.put("boardLink", boardLink);
        paramMap.put("gameName", gameName);

        String messageBody;
        if (createProfile != null) {
            paramMap.put("createDomain", createProfile.getBlog().getDomain());
            paramMap.put("createName", createProfile.getBlog().getScreenName());
            messageBody = NamedTemplate.parse(templateConfig.getEssSystemMessageTemplate()).format(paramMap);
        } else {
            messageBody = NamedTemplate.parse(templateConfig.getEssNotCreateMessageTemplate()).format(paramMap);
        }

        return messageBody;
    }

    private String getGameMessageBody(GameResource gameResouce, ViewCategory moduleCategory, Content content, Profile contentProfile) {
        String URL_WWW = WebappConfig.get().getUrlWww();
        String DOMAIN = WebappConfig.get().getDomain();

        String contentId = content.getContentId();
        String title = StringUtil.isEmpty(content.getSubject()) ?
                HotdeployConfigFactory.get().getConfig(WebHotdeployConfig.class).getContentSubjectEmptyText() :
                content.getSubject();
        String contentDomain = contentProfile.getBlog().getDomain();

        String gameName = gameResouce.getResourceName();

        TemplateHotdeployConfig templateConfig = HotdeployConfigFactory.get().getConfig(TemplateHotdeployConfig.class);

        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("URL_WWW", URL_WWW);
        paramMap.put("DOMAIN", DOMAIN);
        paramMap.put("contentId", contentId);
        paramMap.put("title", title);
        paramMap.put("contentDomain", contentDomain);
        paramMap.put("gameCode", gameResouce.getGameCode());
        paramMap.put("gameName", gameName);
        paramMap.put("moudleName", moduleCategory.getCategoryName());

        String messageBody = NamedTemplate.parse(templateConfig.getPostCModuleSuccessMessageTemplate()).format(paramMap);

        return messageBody;
    }
}
