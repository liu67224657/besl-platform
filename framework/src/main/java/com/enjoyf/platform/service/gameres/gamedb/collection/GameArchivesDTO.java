package com.enjoyf.platform.service.gameres.gamedb.collection;

import com.enjoyf.platform.service.JsonBinder;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.joymeapp.AppRedirectType;
import com.enjoyf.platform.service.joymeapp.gameclient.ArchiveContentType;
import com.enjoyf.platform.service.joymeapp.gameclient.ArchiveRelationType;
import com.enjoyf.platform.service.joymeapp.gameclient.TagDedearchives;
import com.enjoyf.platform.service.joymeapp.gameclient.TagDisplyType;
import com.enjoyf.platform.util.DateUtil;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.reflect.ReflectUtil;
import net.sf.json.JSONObject;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zhimingli
 * Date: 2014/12/31
 * Time: 17:01
 */
public class GameArchivesDTO implements Serializable {
    private long gameId;//标签ID
    private String dede_archives_id;//文章ID
    private String dede_archives_title;//文章的标题
    private String dede_archives_description;//文章的描述
    private String dede_archives_litpic;//文章的头图
    private long dede_archives_pubdate;//文章的发布时间
    private String dede_archives_pubdate_str;
    private String dede_archives_url;//文章的链接

    public long getGameId() {
        return gameId;
    }

    public void setGameId(long gameId) {
        this.gameId = gameId;
    }

    public String getDede_archives_id() {
        return dede_archives_id;
    }

    public void setDede_archives_id(String dede_archives_id) {
        this.dede_archives_id = dede_archives_id;
    }

    public String getDede_archives_title() {
        return dede_archives_title;
    }

    public void setDede_archives_title(String dede_archives_title) {
        this.dede_archives_title = dede_archives_title;
    }

    public String getDede_archives_description() {
        return dede_archives_description;
    }

    public void setDede_archives_description(String dede_archives_description) {
        this.dede_archives_description = dede_archives_description;
    }

    public String getDede_archives_litpic() {
        return dede_archives_litpic;
    }

    public void setDede_archives_litpic(String dede_archives_litpic) {
        this.dede_archives_litpic = dede_archives_litpic;
    }

    public long getDede_archives_pubdate() {
        return dede_archives_pubdate;
    }

    public void setDede_archives_pubdate(long dede_archives_pubdate) {
        this.dede_archives_pubdate = dede_archives_pubdate;
    }

    public String getDede_archives_pubdate_str() {
        return dede_archives_pubdate_str;
    }

    public void setDede_archives_pubdate_str(String dede_archives_pubdate_str) {
        this.dede_archives_pubdate_str = dede_archives_pubdate_str;
    }

    public String getDede_archives_url() {
        return dede_archives_url;
    }

    public void setDede_archives_url(String dede_archives_url) {
        this.dede_archives_url = dede_archives_url;
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

    public static GameArchivesDTO parse(Object jsonObj) {
        if (jsonObj == null) {
            return null;
        }
        JSONObject jsonObject = JSONObject.fromObject(jsonObj);
        if (jsonObject == null) {
            return null;
        }
        GameArchivesDTO gameArchivesDTO = new GameArchivesDTO();
        gameArchivesDTO.setGameId(jsonObject.containsKey("gameId") ? jsonObject.getLong("gameId") : 0l);
        gameArchivesDTO.setDede_archives_id(jsonObject.containsKey("dede_archives_id") ? jsonObject.getString("dede_archives_id") : "");
        gameArchivesDTO.setDede_archives_title(jsonObject.containsKey("dede_archives_title") ? jsonObject.getString("dede_archives_title") : "");
        gameArchivesDTO.setDede_archives_description(jsonObject.containsKey("dede_archives_description") ? jsonObject.getString("dede_archives_description") : "");
        gameArchivesDTO.setDede_archives_litpic(jsonObject.containsKey("dede_archives_litpic") ? jsonObject.getString("dede_archives_litpic") : "");
        gameArchivesDTO.setDede_archives_pubdate(jsonObject.containsKey("dede_archives_pubdate") ? jsonObject.getLong("dede_archives_pubdate") : System.currentTimeMillis());
        gameArchivesDTO.setDede_archives_pubdate_str(jsonObject.containsKey("dede_archives_pubdate_str") ? jsonObject.getString("dede_archives_pubdate_str") : "");
        gameArchivesDTO.setDede_archives_url(jsonObject.containsKey("dede_archives_url") ? jsonObject.getString("dede_archives_url") : "");
        return gameArchivesDTO;
    }

    public static GameArchivesDTO buildDTOFromTagDedeArchives(TagDedearchives tagDedearchives) {
        GameArchivesDTO dto = new GameArchivesDTO();
        dto.setGameId(tagDedearchives.getTagid());
        dto.setDede_archives_description(tagDedearchives.getDede_archives_description());
        dto.setDede_archives_id(tagDedearchives.getDede_archives_id());
        dto.setDede_archives_litpic(tagDedearchives.getDede_archives_litpic());
        dto.setDede_archives_pubdate(tagDedearchives.getDede_archives_pubdate());

        Date date = new Date(tagDedearchives.getDede_archives_pubdate());
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        dto.setDede_archives_pubdate_str(df.format(date));

        dto.setDede_archives_title(tagDedearchives.getDede_archives_title());
        dto.setDede_archives_url(tagDedearchives.getDede_archives_url());
        return dto;
    }
}
