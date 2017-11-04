package com.enjoyf.webapps.joyme.dto.joymeclient;

import com.enjoyf.platform.service.message.WanbaMessageType;
import com.enjoyf.platform.util.StringUtil;
import net.sf.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhimingli on 2015/3/31.
 */
public class GameMessageDTO {
    private String sid;
    private String type;
    private String messagetime;
    private String text;

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessagetime() {
        return messagetime;
    }

    public void setMessagetime(String messagetime) {
        this.messagetime = messagetime;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


    public static Map<String, GameMessageDTO> topMap(String str) {
        Map<String, GameMessageDTO> map = new HashMap<String, GameMessageDTO>();
        if (!StringUtil.isEmpty(str)) {
            JSONObject object = JSONObject.fromObject(str);
            Object objTopic = object.get(WanbaMessageType.TOPIC.getName());
            if (objTopic != null) {
                JSONObject topic = JSONObject.fromObject(objTopic.toString());
                GameMessageDTO dto = new GameMessageDTO();
                dto.setSid(topic.get("sid").toString());
                dto.setType(topic.get("type").toString());
                dto.setText(topic.get("text").toString());
                dto.setMessagetime(topic.get("messagetime").toString());
                map.put(WanbaMessageType.TOPIC.getName(), dto);
            }
            Object miyou = object.get(WanbaMessageType.MIYOU.getName());
            if (miyou != null) {
                JSONObject topic = JSONObject.fromObject(miyou.toString());
                GameMessageDTO dto = new GameMessageDTO();
                dto.setSid(topic.get("sid").toString());
                dto.setType(topic.get("type").toString());
                dto.setText(topic.get("text").toString());
                dto.setMessagetime(topic.get("messagetime").toString());
                map.put(WanbaMessageType.MIYOU.getName(), dto);
            }
            Object hot = object.get(WanbaMessageType.HOT.getName());
            if (hot != null) {
                JSONObject topic = JSONObject.fromObject(hot.toString());
                GameMessageDTO dto = new GameMessageDTO();
                dto.setSid(topic.get("sid").toString());
                dto.setType(topic.get("type").toString());
                dto.setText(topic.get("text").toString());
                dto.setMessagetime(topic.get("messagetime").toString());
                map.put(WanbaMessageType.HOT.getName(), dto);
            }
            Object gift = object.get(WanbaMessageType.GIFT.getName());
            if (gift != null) {
                JSONObject topic = JSONObject.fromObject(gift.toString());
                GameMessageDTO dto = new GameMessageDTO();
                dto.setSid(topic.get("sid").toString());
                dto.setType(topic.get("type").toString());
                dto.setText(topic.get("text").toString());
                dto.setMessagetime(topic.get("messagetime").toString());
                map.put(WanbaMessageType.GIFT.getName(), dto);
            }
            Object task = object.get(WanbaMessageType.TASK.getName());
            if (task != null) {
                JSONObject topic = JSONObject.fromObject(task.toString());
                GameMessageDTO dto = new GameMessageDTO();
                dto.setSid(topic.get("sid").toString());
                dto.setType(topic.get("type").toString());
                dto.setText(topic.get("text").toString());
                dto.setMessagetime(topic.get("messagetime").toString());
                map.put(WanbaMessageType.TASK.getName(), dto);
            }
            Object shake = object.get(WanbaMessageType.SHAKE.getName());
            if (shake != null) {
                JSONObject topic = JSONObject.fromObject(shake.toString());
                GameMessageDTO dto = new GameMessageDTO();
                dto.setSid(topic.get("sid").toString());
                dto.setType(topic.get("type").toString());
                dto.setText(topic.get("text").toString());
                dto.setMessagetime(topic.get("messagetime").toString());
                map.put(WanbaMessageType.SHAKE.getName(), dto);
            }

            Object mygiftmarket = object.get(WanbaMessageType.MYGIFTMARKET.getName());
            if (mygiftmarket != null) {
                JSONObject jsonObject = JSONObject.fromObject(mygiftmarket.toString());
                GameMessageDTO dto = new GameMessageDTO();
                dto.setSid(jsonObject.get("sid").toString());
                dto.setType(jsonObject.get("type").toString());
                dto.setText(jsonObject.get("text").toString());
                dto.setMessagetime(jsonObject.get("messagetime").toString());
                map.put(WanbaMessageType.MYGIFTMARKET.getName(), dto);
            }
        }
        return map;
    }
}
