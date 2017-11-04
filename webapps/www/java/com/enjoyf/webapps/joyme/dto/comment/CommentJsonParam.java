package com.enjoyf.webapps.joyme.dto.comment;

import com.enjoyf.platform.service.JsonBinder;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import net.sf.json.JSONObject;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-11-13
 * Time: 下午4:21
 * To change this template use File | Settings | File Templates.
 */
public class CommentJsonParam implements Serializable {

    private String uri;
    private String title;
    private String pic;
    private String description;
    private int star = 0;

    public CommentJsonParam() {
    }

    public CommentJsonParam(String uri, String title, String pic, String description, int star) {
        this.uri = uri;
        this.title = title;
        this.pic = pic;
        this.description = description;
        this.star = star;
    }

    public String toJson(CommentJsonParam param) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("uri", param.getUri());
        jsonObject.put("title", param.getTitle());
        jsonObject.put("pic", param.getPic());
        jsonObject.put("description", param.getDescription());
        jsonObject.put("star", param.getStar());
        return jsonObject.toString();
    }

    public static CommentJsonParam parse(String jsonStr) {
        CommentJsonParam returnValue = null;
        try {
            if(!StringUtil.isEmpty(jsonStr)){
                JSONObject jsonObject = JSONObject.fromObject(jsonStr);
                if(jsonObject != null && !jsonObject.isNullObject()){
                    returnValue = new CommentJsonParam();
                    returnValue.setUri(jsonObject.containsKey("uri") ? jsonObject.getString("uri") : "");
                    returnValue.setTitle(jsonObject.containsKey("title") ? jsonObject.getString("title") : "");
                    returnValue.setPic(jsonObject.containsKey("pic") ? jsonObject.getString("pic") : "");
                    returnValue.setDescription(jsonObject.containsKey("description") ? jsonObject.getString("description") : "");
                    returnValue.setStar(jsonObject.containsKey("star") ? jsonObject.getInt("star") : 0);
                }
            }
        } catch (Exception e) {
            return null;
        }
        return returnValue;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        this.star = star;
    }
}
