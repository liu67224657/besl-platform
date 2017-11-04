package com.enjoyf.webapps.tools.webpage.controller.batchmanage;

import com.enjoyf.platform.props.hotdeploy.BatchManageHotdeployConfig;
import com.enjoyf.platform.props.hotdeploy.HotdeployConfigFactory;
import com.enjoyf.platform.service.profile.ProfileServiceSngl;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.webapps.tools.weblogic.batchmanage.BatchManageWebLogic;
import com.enjoyf.webapps.tools.webpage.controller.base.ToolsBaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: lijing
 * Date: 12-8-7
 * Time: 上午10:48
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/batchmanage")
public class BatchManageController extends ToolsBaseController {


    @Resource(name = "batchManageWebLogic")
    private BatchManageWebLogic batchManageWebLogic;

    private BatchManageHotdeployConfig batchConfig = HotdeployConfigFactory.get().getConfig(BatchManageHotdeployConfig.class);

    @RequestMapping(value = "/batchcode")
    public ModelAndView batchcode() {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("noticeRadios", batchConfig.getBatchCode());
        return new ModelAndView("/batchmanage/batchcode", mapMessage);
    }

    @RequestMapping(value = "/sendcode")
    public ModelAndView sendcode(@RequestParam(value = "username") String userName,
                                 @RequestParam(value = "pricecode", required = false) String priceCode,
                                 @RequestParam(value = "notice") String notice,
                                 @RequestParam(value = "email") String email,
                                 @RequestParam(value = "emailname") String emailName,
                                 @RequestParam(value = "noticeradio") String noticeRadio) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("noticeRadios", batchConfig.getBatchCode());

        String[] userNames = userName.split("\r\n");
        String[] priceCodes = null;
        if (!StringUtil.isEmpty(priceCode)) {
            priceCodes = priceCode.split("\r\n");
            //验证用户名、奖品码长度
            if (userNames.length != priceCodes.length) {
                mapMessage.put("messageResult", "lengthError");
                mapMessage.put("username", userName);
                mapMessage.put("pricecode", priceCode);
                return new ModelAndView("/batchmanage/batchcode", mapMessage);
            }
        } else {
            priceCodes = new String[userNames.length+1];
            for (int i = 0; i < priceCodes.length; i++) {
                priceCodes[i] = "";
            }
        }

        //得到发送方UNO
        String sendUno;
        try {
            if (noticeRadio.equals("")) {
                sendUno = "";
            } else {
                sendUno = ProfileServiceSngl.get().getProfileBlogByScreenName(noticeRadio).getUno();
            }
        } catch (NullPointerException e) {
            mapMessage.put("messageResult", "sendNameError");
            mapMessage.put("username", userName);
            mapMessage.put("pricecode", priceCode);
            return new ModelAndView("/batchmanage/batchcode", mapMessage);
        } catch (ServiceException e) {
            mapMessage.put("messageResult", "sendNameError");
            mapMessage.put("username", userName);
            mapMessage.put("pricecode", priceCode);
            return new ModelAndView("/batchmanage/batchcode", mapMessage);
        }

        //发送消息
        batchManageWebLogic.startSendThread(userNames, priceCodes, noticeRadio, notice, sendUno, email, emailName, getCurrentUser().getUserid(), getIp());

        mapMessage.put("messageResult", "sendSuccess");
        return new ModelAndView("/batchmanage/batchcode", mapMessage);
    }

}


