package com.enjoyf.platform.serv.viewline.messageprocessor;

import com.enjoyf.platform.props.WebappConfig;
import com.enjoyf.platform.props.hotdeploy.DisplayHotdeployConfig;
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
import com.enjoyf.platform.service.viewline.ViewCategory;
import com.enjoyf.platform.util.NamedTemplate;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 12-8-13
 * Time: 上午11:21
 * To change this template use File | Settings | File Templates.
 */

public class MagazineMessageProcessor extends AbstractMessageProcessor {

    public MagazineMessageProcessor(ViewLineLogic viewLineLogic) {
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

            ViewCategory magazineCategory = getCategory(event.getCategoryId());
            if (magazineCategory == null) {
                //todo
                return;
            }

            String magazineCode=magazineCategory.getCategoryCode();
            if(HotdeployConfigFactory.get().getConfig(DisplayHotdeployConfig.class).getMagazineConfig(magazineCode)==null){
                //该 magazine不是杂志
                return ;
            }

            String title = StringUtil.isEmpty(content.getSubject()) ?
                    HotdeployConfigFactory.get().getConfig(WebHotdeployConfig.class).getContentSubjectEmptyText() :
                    content.getSubject();
            TemplateHotdeployConfig templateConfig = HotdeployConfigFactory.get().getConfig(TemplateHotdeployConfig.class);

            Map<String, String> paramMap = new HashMap<String, String>();
            paramMap.put("URL_WWW", WebappConfig.get().getUrlWww());
            paramMap.put("contentId", content.getContentId());
            paramMap.put("title", title);
            paramMap.put("contentDomain", contentProfile.getBlog().getDomain());
            paramMap.put("magazineCode", magazineCategory.getCategoryCode());
            paramMap.put("magazineName", magazineCategory.getCategoryName());

            String messageBody = NamedTemplate.parse(templateConfig.getMagazineSystemMessageTemplate()).format(paramMap);

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
