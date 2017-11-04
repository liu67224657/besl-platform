package com.enjoyf.platform.service.joymeapp;

import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;

/**
 * Created with IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-7-30
 * Time: 上午9:59
 * To change this template use File | Settings | File Templates.
 */
public class AppDeploymentField extends AbstractObjectField {

    public static final AppDeploymentField DEPLOYMENT_ID = new AppDeploymentField("deployment_id", ObjectFieldDBType.LONG, true, true);
    public static final AppDeploymentField APP_KEY = new AppDeploymentField("app_key", ObjectFieldDBType.STRING, true, false);
    public static final AppDeploymentField PLATFORM = new AppDeploymentField("platform", ObjectFieldDBType.INT, true, false);
    public static final AppDeploymentField PATH = new AppDeploymentField("path_url", ObjectFieldDBType.STRING, true, false);
    public static final AppDeploymentField PASSWORD = new AppDeploymentField("product_pwd", ObjectFieldDBType.STRING, true, false);
    public static final AppDeploymentField IS_PRODUCT = new AppDeploymentField("is_product", ObjectFieldDBType.BOOLEAN, true, false);
    public static final AppDeploymentField TITLE = new AppDeploymentField("title", ObjectFieldDBType.STRING, true, false);
    public static final AppDeploymentField DESCRIPTION = new AppDeploymentField("description", ObjectFieldDBType.STRING, true, false);
    public static final AppDeploymentField DEPLOYMENT_TYPE = new AppDeploymentField("deployment_type", ObjectFieldDBType.INT, true, false);
    public static final AppDeploymentField REMOVE_STATUS = new AppDeploymentField("remove_status", ObjectFieldDBType.STRING, true, false);
    public static final AppDeploymentField MODIFY_DATE = new AppDeploymentField("modify_date", ObjectFieldDBType.TIMESTAMP, true, false);
    public static final AppDeploymentField MODIFY_IP = new AppDeploymentField("modify_ip", ObjectFieldDBType.STRING, true, false);
    public static final AppDeploymentField MODIFY_USERID = new AppDeploymentField("modify_userid", ObjectFieldDBType.STRING, true, false);
    public static final AppDeploymentField CREATE_DATE = new AppDeploymentField("create_date", ObjectFieldDBType.TIMESTAMP, true, false);
    public static final AppDeploymentField UPDATE_TYPE = new AppDeploymentField("update_type", ObjectFieldDBType.INT, true, false);
    public static final AppDeploymentField CHANNEL = new AppDeploymentField("channel", ObjectFieldDBType.STRING, true, false);
	public static final AppDeploymentField ENTERPRISER = new AppDeploymentField("enterpriser", ObjectFieldDBType.INT, true, false);


    public AppDeploymentField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public AppDeploymentField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
