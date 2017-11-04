package com.enjoyf.webapps.tools.webpage.controller.joymeapp;

import com.enjoyf.platform.service.joymeapp.ChannelTopMenu;
import com.enjoyf.platform.service.joymeappconfig.AppChannel;
import com.enjoyf.platform.service.joymeappconfig.JoymeAppConfigServiceSngl;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.webapps.tools.webpage.controller.base.ToolsBaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-9-16
 * Time: 上午10:02
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/joymeapp/channeltopmenu")
public class ChannelTopMenuController extends ToolsBaseController {

    @RequestMapping(value = "/createpage")
    public ModelAndView createChannelMenuPage() {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        try {
            List<AppChannel> channelList = JoymeAppConfigServiceSngl.get().queryAppChannel();
            if (CollectionUtil.isEmpty(channelList)) {
                return new ModelAndView("redirect:/joymeapp/topmenu/list");
            }
            mapMessage.put("channelList", channelList);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("joymeapp/createchanneltopmenu", mapMessage);
        }
        return new ModelAndView("joymeapp/createchanneltopmenu", mapMessage);
    }

    @RequestMapping(value = "/modifypage")
    public ModelAndView modifyChannelMenuPage(@RequestParam(value = "channelcode", required = true) String channelCode,
                                              @RequestParam(value = "menuurl", required = true) String menuUrl,
                                              @RequestParam(value = "name", required = true) String name,
                                              @RequestParam(value = "picurl", required = true) String picUrl,
                                              @RequestParam(value = "desc", required = true) String desc,
                                              @RequestParam(value = "platform", required = true) Integer platform) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        try {
            List<AppChannel> channelList = JoymeAppConfigServiceSngl.get().queryAppChannel();
            if (CollectionUtil.isEmpty(channelList)) {
                return new ModelAndView("redirect:/joymeapp/topmenu/list");
            }
            mapMessage.put("channelList", channelList);

            if (!StringUtil.isEmpty(channelCode)) {
                ChannelTopMenu channelTopMenu = new ChannelTopMenu();
                channelTopMenu.setChannelCode(channelCode);
                channelTopMenu.setDesc(desc);
                channelTopMenu.setPlatform(platform);
                channelTopMenu.setName(name);
                channelTopMenu.setPicUrl(picUrl);
                channelTopMenu.setUrl(menuUrl);
                mapMessage.put("channelTopMenu", channelTopMenu);
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        return new ModelAndView("joymeapp/modifychanneltopmenu", mapMessage);
    }

}
