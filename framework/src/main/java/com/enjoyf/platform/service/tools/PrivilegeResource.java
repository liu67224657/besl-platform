package com.enjoyf.platform.service.tools;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Author: zhaoxin
 * Date: 11-10-31
 * Time: 下午8:35
 * Desc:
 */
public class PrivilegeResource implements Serializable, Comparable {

    private Integer rsid;
    private String rsname;
    private String rsurl;
    private PrivilegeResourceLevel rslevel;    //资源级别，没用
    /**
     * status:'y' 可用　status:'n' 不可用
     */
    private ActStatus status;
    private Integer parentid;
    private Integer orderfield;
    private String iconurl;
    /**
     * rstype:1菜单 rstype:2URL功能
     */
    private PrivilegeResourceType rstype;
    /**
     * 页面是否显示ismenu:A代表显示、ismenu:I代表不显示。用于用户登录系统后是否显示这些菜单。
     */
    private ActStatus ismenu;
    private String description;
    private Set<PrivilegeRole> privilegeRoleses = new HashSet<PrivilegeRole>(0);

    public PrivilegeResource() {
    }

    public PrivilegeResource(String rsname, PrivilegeResourceLevel rslevel, ActStatus status,
                             PrivilegeResourceType rstype) {
        this.rsname = rsname;
        this.rslevel = rslevel;
        this.status = status;
        this.rstype = rstype;
    }

    public PrivilegeResource(String rsname, String rsurl, PrivilegeResourceLevel rslevel,
                             ActStatus status, Integer parentid, Integer orderfield,
                             String iconurl, PrivilegeResourceType rstype, String description,
                             Set<PrivilegeRole> privilegeRoleses) {
        this.rsname = rsname;
        this.rsurl = rsurl;
        this.rslevel = rslevel;
        this.status = status;
        this.parentid = parentid;
        this.orderfield = orderfield;
        this.iconurl = iconurl;
        this.rstype = rstype;
        this.description = description;
        this.privilegeRoleses = privilegeRoleses;
    }


    public Integer getRsid() {
        return rsid;
    }

    public void setRsid(Integer rsid) {
        this.rsid = rsid;
    }

    public String getRsname() {
        return this.rsname;
    }

    public void setRsname(String rsname) {
        this.rsname = rsname;
    }

    public String getRsurl() {
        return this.rsurl;
    }

    public void setRsurl(String rsurl) {
        this.rsurl = rsurl;
    }

    public PrivilegeResourceLevel getRslevel() {
        return this.rslevel;
    }

    public void setRslevel(PrivilegeResourceLevel rslevel) {
        this.rslevel = rslevel;
    }

    public ActStatus getStatus() {
        return this.status;
    }

    public void setStatus(ActStatus status) {
        this.status = status;
    }

    public Integer getParentid() {
        return this.parentid;
    }

    public void setParentid(Integer parentid) {
        this.parentid = parentid;
    }

    public Integer getOrderfield() {
        return this.orderfield;
    }

    public void setOrderfield(Integer orderfield) {
        this.orderfield = orderfield;
    }

    public String getIconurl() {
        return this.iconurl;
    }

    public void setIconurl(String iconurl) {
        this.iconurl = iconurl;
    }

    public PrivilegeResourceType getRstype() {
        return this.rstype;
    }

    public void setRstype(PrivilegeResourceType rstype) {
        this.rstype = rstype;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    //privilegeRoleses 这个字段在传到页面的时候不进行josn序列化
    //@JSON(serialize=false,deserialize=false)
    public Set<PrivilegeRole> getPrivilegeRoleses() {
        return this.privilegeRoleses;
    }

    public void setPrivilegeRoleses(Set<PrivilegeRole> privilegeRoleses) {
        this.privilegeRoleses = privilegeRoleses;
    }

    public ActStatus getIsmenu() {
        return ismenu;
    }

    public void setIsmenu(ActStatus ismenu) {
        this.ismenu = ismenu;
    }

    public int compareTo(Object o) {
        return getOrderfield() - ((PrivilegeResource) o).getOrderfield();
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
