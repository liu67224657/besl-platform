/**
 * CopyRight Of Fivewh.com 2012.
 */
package com.enjoyf.platform.service.tools;

import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * Author: <a href="mailto:yinpengyi@gmail.com">ypy</a>
 * Date Time: 3/18/13 3:03 PM
 */
public class StatsEditorItem implements Serializable {
    //the editor stats item no
    private String itemNo;

    //the editor admin user no.
    private int adminUno;

    //
    private EditorStatsItemType itemType = EditorStatsItemType.ARTICLE;
    private String itemSubType;

    //
    private String itemSrcNo;
    private String sourceId;

    //the editor's status
    private ValidStatus validStatus = ValidStatus.VALID;

    //the create information.
    private Date createDate;
    private String createIp;

    //
    public String getItemNo() {
        return itemNo;
    }

    public void setItemNo(String itemNo) {
        this.itemNo = itemNo;
    }

    public int getAdminUno() {
        return adminUno;
    }

    public void setAdminUno(int adminUno) {
        this.adminUno = adminUno;
    }

    public EditorStatsItemType getItemType() {
        return itemType;
    }

    public void setItemType(EditorStatsItemType itemType) {
        this.itemType = itemType;
    }

    public String getItemSubType() {
        return itemSubType;
    }

    public void setItemSubType(String itemSubType) {
        this.itemSubType = itemSubType;
    }

    public String getItemSrcNo() {
        return itemSrcNo;
    }

    public void setItemSrcNo(String itemSrcNo) {
        this.itemSrcNo = itemSrcNo;
    }

    public ValidStatus getValidStatus() {
        return validStatus;
    }

    public void setValidStatus(ValidStatus validStatus) {
        this.validStatus = validStatus;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCreateIp() {
        return createIp;
    }

    public void setCreateIp(String createIp) {
        this.createIp = createIp;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    //
    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
