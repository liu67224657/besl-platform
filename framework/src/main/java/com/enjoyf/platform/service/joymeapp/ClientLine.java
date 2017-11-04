package com.enjoyf.platform.service.joymeapp;

import com.enjoyf.platform.service.ValidStatus;
import net.sf.json.JSONObject;

import java.io.Serializable;
import java.util.Date;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  13-12-9 下午5:51
 * Description:ClinetLine的实体类
 */
public class ClientLine implements Serializable {
    private long lineId;
    private String code;
    private String lineName;
    private ClientItemType itemType;

    //create info
    private Date createDate;
    private String createUserid;

    //last update info
    private Date updateDate;
    private String updateUserid;

    //状态位
    private ValidStatus validStatus;

    private int platform;

    private ClientLineAngular lineAngle;
    private String bigpic;
    private String smallpic;
    private String line_desc;
    private String line_intro;
    private int hot;


    private ClientLineType lineType;
    private int display_order;
    private String sharePic;

    public ClientLine() {
    }

    public long getLineId() {
        return lineId;
    }

    public void setLineId(long lineId) {
        this.lineId = lineId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLineName() {
        return lineName;
    }

    public void setLineName(String lineName) {
        this.lineName = lineName;
    }

    public ClientItemType getItemType() {
        return itemType;
    }

    public void setItemType(ClientItemType itemType) {
        this.itemType = itemType;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCreateUserid() {
        return createUserid;
    }

    public void setCreateUserid(String createUserid) {
        this.createUserid = createUserid;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getUpdateUserid() {
        return updateUserid;
    }

    public void setUpdateUserid(String updateUserid) {
        this.updateUserid = updateUserid;
    }

    public ValidStatus getValidStatus() {
        return validStatus;
    }

    public void setValidStatus(ValidStatus validStatus) {
        this.validStatus = validStatus;
    }

    public int getPlatform() {
        return platform;
    }

    public void setPlatform(int platform) {
        this.platform = platform;
    }

    public ClientLineType getLineType() {
        return lineType;
    }

    public void setLineType(ClientLineType lineType) {
        this.lineType = lineType;
    }

    public ClientLineAngular getLineAngle() {
        return lineAngle;
    }

    public void setLineAngle(ClientLineAngular lineAngle) {
        this.lineAngle = lineAngle;
    }

    public String getBigpic() {
        return bigpic;
    }

    public void setBigpic(String bigpic) {
        this.bigpic = bigpic;
    }

    public String getSmallpic() {
        return smallpic;
    }

    public void setSmallpic(String smallpic) {
        this.smallpic = smallpic;
    }

    public String getLine_desc() {
        return line_desc;
    }

    public void setLine_desc(String line_desc) {
        this.line_desc = line_desc;
    }

    public int getDisplay_order() {
        return display_order;
    }

    public void setDisplay_order(int display_order) {
        this.display_order = display_order;
    }

    public String getLine_intro() {
        return line_intro;
    }

    public void setLine_intro(String line_intro) {
        this.line_intro = line_intro;
    }

    public int getHot() {
        return hot;
    }

    public void setHot(int hot) {
        this.hot = hot;
    }

    public String getSharePic() {
        return sharePic;
    }

    public void setSharePic(String sharePic) {
        this.sharePic = sharePic;
    }

    public String toJsonStr(){
        JSONObject jsonObject = JSONObject.fromObject(this);
        return jsonObject.toString();
    }

    public static ClientLine parse(Object jsonObj){
        if(jsonObj == null){
            return null;
        }
        JSONObject jsonObject = JSONObject.fromObject(jsonObj);
        if(jsonObject == null){
            return null;
        }
        ClientLine clientLine = new ClientLine();
        clientLine.setLineId(jsonObject.containsKey("lineId") ? jsonObject.getLong("lineId") : 0l);
        clientLine.setCode(jsonObject.containsKey("code") ? jsonObject.getString("code") : "");
        clientLine.setLineName(jsonObject.containsKey("lineName") ? jsonObject.getString("lineName") : "");
        if(jsonObject.containsKey("itemType")){
            JSONObject jsonItemType = jsonObject.getJSONObject("itemType");
            if(jsonItemType != null){
                ClientItemType clientItemType = jsonItemType.containsKey("code") ? ClientItemType.getByCode(jsonItemType.getInt("code")) : null;
                if(clientItemType != null){
                    clientLine.setItemType(clientItemType);
                }
            }
        }
        clientLine.setCreateDate(jsonObject.containsKey("createDate") ? (jsonObject.getJSONObject("createDate").containsKey("time") ? new Date(jsonObject.getJSONObject("createDate").getLong("time")) : null) : null);
        clientLine.setCreateUserid(jsonObject.containsKey("createUserid") ? jsonObject.getString("createUserid") : "");
        clientLine.setUpdateDate(jsonObject.containsKey("updateDate") ? (jsonObject.getJSONObject("updateDate").containsKey("time") ? new Date(jsonObject.getJSONObject("updateDate").getLong("time")) : null) : null);
        clientLine.setUpdateUserid(jsonObject.containsKey("updateUserid") ? jsonObject.getString("updateUserid") : "");
        if(jsonObject.containsKey("validStatus")){
            JSONObject jsonStatus = jsonObject.getJSONObject("validStatus");
            if(jsonStatus != null){
                ValidStatus validStatus = jsonStatus.containsKey("code") ? ValidStatus.getByCode(jsonStatus.getString("code")) : null;
                if(validStatus != null){
                    clientLine.setValidStatus(validStatus);
                }
            }
        }
        clientLine.setPlatform(jsonObject.containsKey("platform") ? jsonObject.getInt("platform") : 0);
        if(jsonObject.containsKey("lineAngle")){
            JSONObject jsonAngle = jsonObject.getJSONObject("lineAngle");
            if(jsonAngle != null){
                ClientLineAngular clientLineAngular = jsonAngle.containsKey("code") ? ClientLineAngular.getByCode(jsonAngle.getInt("code")) : null;
                if(clientLineAngular != null){
                    clientLine.setLineAngle(clientLineAngular);
                }
            }
        }
        clientLine.setBigpic(jsonObject.containsKey("bigpic") ? jsonObject.getString("bigpic") : "");
        clientLine.setSmallpic(jsonObject.containsKey("smallpic") ? jsonObject.getString("smallpic") : "");
        clientLine.setLine_desc(jsonObject.containsKey("line_desc") ? jsonObject.getString("line_desc") : "");
        clientLine.setLine_intro(jsonObject.containsKey("line_intro") ? jsonObject.getString("line_intro") : "");
        clientLine.setHot(jsonObject.containsKey("hot") ? jsonObject.getInt("hot") : 0);
        if(jsonObject.containsKey("lineType")){
            JSONObject jsonLineType = jsonObject.getJSONObject("lineType");
            if(jsonLineType != null){
                ClientLineType lineType = jsonLineType.containsKey("code") ? ClientLineType.getByCode(jsonLineType.getInt("code")) : null;
                if(lineType != null){
                    clientLine.setLineType(lineType);
                }
            }
        }
        clientLine.setDisplay_order(jsonObject.containsKey("display_order") ? jsonObject.getInt("display_order") : 0);
        clientLine.setSharePic(jsonObject.containsKey("sharePic") ? jsonObject.getString("sharePic") : "");
        return clientLine;
    }

    @Override
    public String toString() {
        JSONObject jsonObject = JSONObject.fromObject(this);
        return jsonObject.toString();
    }
}
