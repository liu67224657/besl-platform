package com.enjoyf.webapps.tools.webpage.controller.audit;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.content.ContentField;
import com.enjoyf.platform.service.content.ContentInteractionField;
import com.enjoyf.platform.service.tools.ContentAuditStatus;
import com.enjoyf.platform.service.tools.ContentReplyAuditStatus;
import com.enjoyf.platform.service.tools.LogOperType;
import com.enjoyf.platform.service.tools.ToolsLog;
import com.enjoyf.platform.util.sql.ObjectField;
import com.enjoyf.platform.webapps.common.JoymeResultMsg;
import com.enjoyf.platform.webapps.common.encode.JsonBinder;
import com.enjoyf.webapps.tools.weblogic.content.ContentWebLogic;
import com.enjoyf.webapps.tools.weblogic.content.StandardStrategy;
import com.enjoyf.webapps.tools.weblogic.content.StatusStrategy;
import com.enjoyf.webapps.tools.weblogic.content.SubstandardStrategy;
import com.enjoyf.webapps.tools.weblogic.content.UnCheckStrategy;
import com.enjoyf.webapps.tools.webpage.controller.ContentAuditUtil;
import com.enjoyf.webapps.tools.webpage.controller.base.ToolsBaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: yujiaxiu
 * Date: 12-4-19
 * Time: 下午11:40
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping("/json/content")
public class JsonContentController extends ToolsBaseController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource(name = "contentWebLogic")
    private ContentWebLogic contentWebLogic;

    private StatusStrategy statusStrategy;

    //@ResponseBody表示该方法返回的结果直接输入到ResponseBody中去
    @ResponseBody
    @RequestMapping("/modifytextstatus")
    public String modifyContentAuditStatusSngl(@RequestParam(value = "contentid", required = false)String contentId,
                                               @RequestParam(value = "uno", required = false)String uno,
                                               @RequestParam(value = "originalvalue", required = false)Integer originalAuditStatus) {

        if(logger.isDebugEnabled()){
            logger.debug("parameter from jsp : contentid=" + contentId + ";uno=" + uno + ";curauditstatus=" + originalAuditStatus);
        }

        Map<ObjectField, Object> fieldObjectMap = new HashMap<ObjectField, Object>();

        JoymeResultMsg resultMsg = new JoymeResultMsg(JoymeResultMsg.CODE_E);

        //依赖原来的值的计算，原来的值最好不要是从页面传过来的
        //这个方法原来的值是从页面传过来的，这样做是为了减少数据库访问的次数，但是同时增大了风险
        ContentAuditStatus contentAuditStatus = ContentAuditStatus.getByValue(originalAuditStatus);

        Integer curAuditStatus = originalAuditStatus;
        if(contentAuditStatus.hasAuditText() && contentAuditStatus.isTextPass()){
            //如果当前状态是：审核过并且通过
            curAuditStatus += ContentAuditStatus.ILLEGAL_TEXT;

            fieldObjectMap.put(ContentField.REMOVESTATUS, ActStatus.ACTED.getCode());
        }else if(contentAuditStatus.hasAuditText() && !contentAuditStatus.isTextPass()){
            //如果当前状态是：审核过并且没通过
            curAuditStatus -= ContentAuditStatus.ILLEGAL_TEXT;

            fieldObjectMap.put(ContentField.REMOVESTATUS, ActStatus.UNACT.getCode());
        }else {
            //如果当前状态是：没审核过
            curAuditStatus = ContentAuditStatus.AUDIT_TEXT + ContentAuditStatus.ILLEGAL_TEXT;

            fieldObjectMap.put(ContentField.REMOVESTATUS, ActStatus.ACTED.getCode());
        }

        fieldObjectMap.put(ContentField.AUDITSTATUS, curAuditStatus);

        boolean success = contentWebLogic.modifyAuditStatus(contentId, uno, fieldObjectMap);

        if(success){
            resultMsg.setMsg(curAuditStatus.toString());
            resultMsg.setStatus_code(JoymeResultMsg.CODE_S);

            //准备log对象
            ToolsLog log = new ToolsLog();
            log.setOpUserId(getCurrentUser().getUserid());
            log.setOperType(LogOperType.TEXT_MODIFYCONTENTAUDITSTATUSSNGL);
            log.setOpTime(new Date());
            log.setOpIp(getIp());
            log.setDescription("资源ID为：CONTENTID[" + contentId + "], UNO[" + uno + "]");
            log.setOpBefore("AUDITSTATUS[" + originalAuditStatus + "]");
            log.setOpAfter("AUDITSTATUS[" + curAuditStatus + "]");
            addLog(log);
        }

        return JsonBinder.buildNormalBinder().toJson(resultMsg);

    }


    @ResponseBody
    @RequestMapping("/modifyreplystatus")
    public String modifyReplyAuditStatusSngl(@RequestParam(value = "replyid", required = false)String replyId,
                                               @RequestParam(value = "contentid", required = false)String contentId,
                                               @RequestParam(value = "originalvalue", required = false)Integer originalAuditStatus) {

        if(logger.isDebugEnabled()){
            logger.debug("parameter from jsp : replyid=" + replyId + ";contentid=" + contentId + ";originalAuditStatus=" + originalAuditStatus);
        }

        Map<ObjectField, Object> fieldObjectMap = new HashMap<ObjectField, Object>();

        JoymeResultMsg resultMsg = new JoymeResultMsg(JoymeResultMsg.CODE_E);

        //依赖原来的值的计算，原来的值最好不要是从页面传过来的
        //这个方法原来的值是从页面传过来的，这样做是为了减少数据库访问的次数，但是同时增大了风险
        ContentReplyAuditStatus replyAuditStatus = ContentReplyAuditStatus.getByValue(originalAuditStatus);

        Integer curAuditStatus = originalAuditStatus;
        if(replyAuditStatus.hasAuditReply() && replyAuditStatus.isReplyPass()){
            //如果当前状态是：审核过并且通过
            curAuditStatus += ContentReplyAuditStatus.ILLEGAL_REPLY;

            fieldObjectMap.put(ContentInteractionField.REMOVESTATUS, ActStatus.ACTED.getCode());
        }else if(replyAuditStatus.hasAuditReply() && !replyAuditStatus.isReplyPass()){
            //如果当前状态是：审核过并且没通过
            curAuditStatus -= ContentReplyAuditStatus.ILLEGAL_REPLY;

            fieldObjectMap.put(ContentInteractionField.REMOVESTATUS, ActStatus.UNACT.getCode());
        }else {
            //如果当前状态是：没审核过
            curAuditStatus = ContentReplyAuditStatus.AUDIT_REPLY + ContentReplyAuditStatus.ILLEGAL_REPLY;

            fieldObjectMap.put(ContentInteractionField.REMOVESTATUS, ActStatus.ACTED.getCode());
        }

        fieldObjectMap.put(ContentInteractionField.AUDITSTATUS, curAuditStatus);

        boolean success = contentWebLogic.modifyReplyAuditStatus(replyId, "??", contentId, fieldObjectMap);

        if(success){
            resultMsg.setMsg(curAuditStatus.toString());
            resultMsg.setStatus_code(JoymeResultMsg.CODE_S);

            //准备log对象
            ToolsLog log = new ToolsLog();
            log.setOpUserId(getCurrentUser().getUserid());
            log.setOperType(LogOperType.REPLY_MODIFYREPLYAUDITSTATUSSNGL);
            log.setOpTime(new Date());
            log.setOpIp(getIp());
            log.setDescription("资源ID为：CONTENTID[" + contentId + "], REPLYID[" + replyId + "]");
            log.setOpBefore("AUDITSTATUS[" + originalAuditStatus + "]");
            log.setOpAfter("AUDITSTATUS[" + curAuditStatus + "]");
            addLog(log);
        }

        return JsonBinder.buildNormalBinder().toJson(resultMsg);

    }


    @ResponseBody
    @RequestMapping("/modifyimgstatus")
    public String modifyImageAuditStatusSngl(@RequestParam(value = "contentid", required = false)String contentId,
                                               @RequestParam(value = "uno", required = false)String uno,
                                               @RequestParam(value = "snglimg", required = false)String[] snglimg,
                                               @RequestParam(value = "originalvalue", required = false)Integer originalAuditStatus,
                                               @RequestParam(value = "auditstatus", required = false)Integer auditStatus) {

        if(logger.isDebugEnabled()){
            logger.debug("parameter from jsp : contentid=" + contentId + ";sreenname=" + uno + ";snglimg=" + snglimg + ";originalAuditStatus=" + originalAuditStatus);
        }

        JoymeResultMsg resultMsg = new JoymeResultMsg(JoymeResultMsg.CODE_E);

        if (snglimg != null && snglimg.length > 0) {
            if (auditStatus.equals(ContentAuditStatus.AUDIT_IMG)) {
                //如果当前状态是：审核过并且通过
                //屏蔽逻辑
                statusStrategy = new SubstandardStrategy(contentWebLogic, contentId, uno, snglimg, originalAuditStatus);
            } else if(auditStatus.equals(ContentAuditStatus.AUDIT_IMG + ContentAuditStatus.ILLEGAL_IMG)){
                //如果当前状态是：审核过并且没通过
                //通过逻辑
                statusStrategy = new StandardStrategy(contentWebLogic, contentId, uno, snglimg, originalAuditStatus);
            } else {
                //如果当前状态是：没审核过
                statusStrategy = new UnCheckStrategy(contentWebLogic, contentId, uno, snglimg, originalAuditStatus);
            }

            Map<ObjectField, Object> map = new HashMap<ObjectField, Object>();

            if(auditStatus.equals(ContentAuditStatus.AUDIT_IMG + ContentAuditStatus.ILLEGAL_IMG)){
                map = statusStrategy.modifyIMGContentTemplate(false);
            }else {
                map = statusStrategy.modifyIMGContentTemplate(true);
            }

            String jsonStr = (String)map.get(ContentField.IMAGES);

            resultMsg.setMsg(jsonStr);
            resultMsg.setStatus_code(JoymeResultMsg.CODE_S);

            //准备log对象
            ToolsLog log = new ToolsLog();
            log.setOpUserId(getCurrentUser().getUserid());
            log.setOperType(LogOperType.IMG_MODIFYIMAGEAUDITSTATUSSNGL);
            log.setOpTime(new Date());
            log.setOpIp(getIp());
            log.setDescription("资源ID为：CONTENTID[" + contentId + "], UNO[" + uno + "]");
            log.setOpBefore("AUDITSTATUS[" + originalAuditStatus + "]");
            log.setOpAfter(ContentAuditUtil.formMapToString(map));
            addLog(log);
        }

        return JsonBinder.buildNormalBinder().toJson(resultMsg);

    }

}
