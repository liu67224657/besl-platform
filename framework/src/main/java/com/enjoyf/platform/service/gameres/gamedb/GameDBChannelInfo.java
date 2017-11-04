package com.enjoyf.platform.service.gameres.gamedb;

import com.enjoyf.platform.service.JsonBinder;
import com.enjoyf.platform.service.joymeapp.ChannelTopMenu;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.reflect.ReflectUtil;
import net.sf.json.JSONObject;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.io.Serializable;
import java.nio.channels.Channel;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * <p/>
 * Description:游戏渠道code数据来源于人不熟配置文件，不要new一个新对象
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>ericliu</a>
 */
public class GameDBChannelInfo implements Serializable {

    private String device;         //平台
    private String channel_id;         //渠道ID

    private GameDBChannelDownloadInfo channelDownloadInfo;

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getChannel_id() {
        return channel_id;
    }

    public void setChannel_id(String channel_id) {
        this.channel_id = channel_id;
    }

    public GameDBChannelDownloadInfo getChannelDownloadInfo() {
        return channelDownloadInfo;
    }

    public void setChannelDownloadInfo(GameDBChannelDownloadInfo channelDownloadInfo) {
        this.channelDownloadInfo = channelDownloadInfo;
    }

    @Override
    public String toString() {
        JSONObject jsonObject = JSONObject.fromObject(this);
        return jsonObject.toString();
    }


    public String toJson() {
        JSONObject jsonObject = JSONObject.fromObject(this);
        return jsonObject.toString();
    }

    public static GameDBChannelInfo parse(Object object) {
        if(object == null){
            return null;
        }
        JSONObject jsonObject = JSONObject.fromObject(object);
        if(jsonObject == null){
            return null;
        }
        GameDBChannelInfo gameDBChannelInfo = new GameDBChannelInfo();
        gameDBChannelInfo.setDevice(jsonObject.containsKey("device") ? jsonObject.getString("device") : "");
        gameDBChannelInfo.setChannel_id(jsonObject.containsKey("channel_id") ? jsonObject.getString("channel_id"):"");

        GameDBChannelDownloadInfo gameDBChannelDownloadInfo = new GameDBChannelDownloadInfo();
        if(jsonObject.containsKey("channelDownloadInfo")){
            JSONObject jsonDownloadObject = JSONObject.fromObject(jsonObject.get("channelDownloadInfo"));
            gameDBChannelDownloadInfo.setDownload(jsonDownloadObject.containsKey("download") ? jsonDownloadObject.getString("download") : "");
            gameDBChannelDownloadInfo.setChannelName(jsonDownloadObject.containsKey("channelName") ? jsonDownloadObject.getString("channelName") : "");
            gameDBChannelDownloadInfo.setGamesize(jsonDownloadObject.containsKey("gamesize") ? jsonDownloadObject.getString("gamesize") : "");
            gameDBChannelDownloadInfo.setGameversion(jsonDownloadObject.containsKey("gameversion") ? jsonDownloadObject.getString("gameversion") : "");
            gameDBChannelDownloadInfo.setSystemversion(jsonDownloadObject.containsKey("systemversion") ? jsonDownloadObject.getString("systemversion") : "");
        }
        gameDBChannelInfo.setChannelDownloadInfo(gameDBChannelDownloadInfo);
        return gameDBChannelInfo;
    }
}
