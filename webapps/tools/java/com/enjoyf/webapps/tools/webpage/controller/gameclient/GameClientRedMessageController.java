package com.enjoyf.webapps.tools.webpage.controller.gameclient;

import com.enjoyf.platform.service.gameres.gamedb.RedMessageType;
import com.enjoyf.platform.service.message.MessageConstants;
import com.enjoyf.platform.service.message.MessageServiceSngl;
import com.enjoyf.platform.service.message.WanbaMessageType;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.tools.LogOperType;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.webapps.tools.weblogic.dto.gameclient.GameMessageDTO;
import com.enjoyf.webapps.tools.webpage.controller.base.ToolsBaseController;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhimingli on 2015/4/2.
 */
@Controller
@RequestMapping(value = "/gameclient/redmessage")
public class GameClientRedMessageController extends ToolsBaseController {
    @RequestMapping("/list")
    public ModelAndView gameReltaionList(@RequestParam(value = "platform", required = false, defaultValue = "0") String platform) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("redMessageType", RedMessageType.getAll());
        mapMessage.put("wanbaMessageType", WanbaMessageType.getAll());

        try {
            String str = MessageServiceSngl.get().getRedis(MessageConstants.REDIS_KEY_WANBA_REDMESSAGE + platform);

            Map<String, GameMessageDTO> map = GameMessageDTO.topMap(str);
            mapMessage.put("map", map);
            mapMessage.put("platform", platform);
            JSONSerializer.toJSON(map).toString();
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return new ModelAndView("/gameclient/redmessage/redmessagelist", mapMessage);
    }


    @RequestMapping("/create")
    public ModelAndView create(@RequestParam(value = "wanbaMessageType", required = false) String wanbaMessageType,
                               @RequestParam(value = "redmessagetype", required = false, defaultValue = "0") Integer redmessagetype,
                               @RequestParam(value = "text", required = false, defaultValue = "") String text,
                               @RequestParam(value = "platform", required = false) String platform) {
        try {

            writeToolsLog(LogOperType.WANBA_RED_MESSAHE, "platform:" + platform + ",wanbaMessageType:" + wanbaMessageType + ",redmessagetype=" + redmessagetype + ",text=" + text);

            String str = MessageServiceSngl.get().getRedis(MessageConstants.REDIS_KEY_WANBA_REDMESSAGE + platform);
            Map<String, GameMessageDTO> map = GameMessageDTO.topMap(str);
            Object obj = map.get(wanbaMessageType);
            if (obj != null) {
                GameMessageDTO dto = new GameMessageDTO();
                dto.setSid("");
                dto.setType(redmessagetype + "");
                dto.setText(text);
                dto.setMessagetime(System.currentTimeMillis() + "");
                map.put(wanbaMessageType, dto);
                MessageServiceSngl.get().setTORedis(MessageConstants.REDIS_KEY_WANBA_REDMESSAGE + platform, JSONSerializer.toJSON(map).toString());
            } else {
                GameMessageDTO dto = new GameMessageDTO();
                dto.setSid("");
                dto.setType(redmessagetype + "");
                dto.setText(text);
                dto.setMessagetime(System.currentTimeMillis() + "");
                map.put(wanbaMessageType, dto);
                map.put(wanbaMessageType, dto);
                MessageServiceSngl.get().setTORedis(MessageConstants.REDIS_KEY_WANBA_REDMESSAGE + platform, JSONSerializer.toJSON(map).toString());
            }
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return new ModelAndView("redirect:/gameclient/redmessage/list?platform=" + platform);
    }


}
