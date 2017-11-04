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
public class StatsEditor implements Serializable {
    //the editor admin user no.
    private int adminUno;

    private String editorName;
    private String editorDesc;

    //the editor's status
    private ValidStatus validStatus = ValidStatus.VALID;

    //the create information.
    private Date createDate;
    private String createIp;

    //
    public int getAdminUno() {
        return adminUno;
    }

    public void setAdminUno(int adminUno) {
        this.adminUno = adminUno;
    }

    public String getEditorName() {
        return editorName;
    }

    public void setEditorName(String editorName) {
        this.editorName = editorName;
    }

    public String getEditorDesc() {
        return editorDesc;
    }

    public void setEditorDesc(String editorDesc) {
        this.editorDesc = editorDesc;
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

    //
    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
