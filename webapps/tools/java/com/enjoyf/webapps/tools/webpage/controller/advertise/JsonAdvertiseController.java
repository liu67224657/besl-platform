package com.enjoyf.webapps.tools.webpage.controller.advertise;

import com.enjoyf.platform.service.advertise.AdvertiseAgentField;
import com.enjoyf.platform.service.advertise.AdvertiseServiceSngl;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.UpdateExpress;
import com.enjoyf.platform.webapps.common.JoymeResultMsg;
import com.enjoyf.platform.webapps.common.encode.JsonBinder;
import com.enjoyf.webapps.tools.webpage.controller.base.ToolsBaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: lijing
 * Date: 12-7-16
 * Time: 下午1:38
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/json/advertise")
public class JsonAdvertiseController extends ToolsBaseController {
    @ResponseBody
    @RequestMapping(value = "/modifyCheck")
    public String modifyCheck(@RequestParam(value = "agentids", required = false) String agentIds,
                              @RequestParam(value = "modifyvalidstatus") String modifyValidStatus,
                              @RequestParam(value = "projectids", required = false) String projectIds,
                              @RequestParam(value = "publishids", required = false) String publishIds) {

        List<String> list = new ArrayList<String>();
        UpdateExpress updateExpress = new UpdateExpress();

        if (agentIds != null) {
            String[] agentTd = agentIds.split(",");
            for (int i = 0; i < agentTd.length; i++) {
                list.add(agentTd[i]);
                try {
                    updateExpress.set(AdvertiseAgentField.VALIDSTATUS, modifyValidStatus);
                    AdvertiseServiceSngl.get().modifyAgent(updateExpress, agentTd[i]);
                } catch (ServiceException e) {
                    GAlerter.lab(this.getClass().getName() + " occured Service Exception.e:", e);
                }
            }
        }else if (projectIds != null) {
            String[] projectId = projectIds.split(",");
            for (int i = 0; i < projectId.length; i++) {
                list.add(projectId[i]);
                try {
                    updateExpress.set(AdvertiseAgentField.VALIDSTATUS, modifyValidStatus);
                    AdvertiseServiceSngl.get().modifyProject(updateExpress, projectId[i]);
                } catch (ServiceException e) {
                    GAlerter.lab(this.getClass().getName() + " occured Service Exception.e:", e);
                }
            }
        }else if (publishIds != null) {
            String[] publishId = publishIds.split(",");
            for (int i = 0; i < publishId.length; i++) {
                list.add(publishId[i]);
                try {
                    updateExpress.set(AdvertiseAgentField.VALIDSTATUS, modifyValidStatus);
                    AdvertiseServiceSngl.get().modifyPublish(updateExpress, publishId[i]);
                } catch (ServiceException e) {
                    GAlerter.lab(this.getClass().getName() + " occured Service Exception.e:", e);
                }
            }
        }
        JoymeResultMsg resultMsg = new JoymeResultMsg(JoymeResultMsg.CODE_S);
        resultMsg.setResult(list);

        return JsonBinder.buildNormalBinder().toJson(resultMsg);
    }
}
