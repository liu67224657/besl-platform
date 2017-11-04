package com.enjoyf.platform.tools.joymeapp;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.content.ContentHandler;
import com.enjoyf.platform.db.joymeapp.JoymeAppHandler;
import com.enjoyf.platform.db.oauth.OAuthHandler;
import com.enjoyf.platform.props.hotdeploy.HotdeployConfigFactory;
import com.enjoyf.platform.props.hotdeploy.WebApiHotdeployConfig;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.content.ForignContent;
import com.enjoyf.platform.service.content.ForignContentDomain;
import com.enjoyf.platform.service.content.ForignContentField;
import com.enjoyf.platform.service.joymeapp.*;
import com.enjoyf.platform.service.oauth.AuthApp;
import com.enjoyf.platform.service.oauth.AuthAppField;
import com.enjoyf.platform.service.oauth.AuthAppType;
import com.enjoyf.platform.service.oauth.OAuthServiceSngl;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.FiveProps;
import com.enjoyf.platform.util.Props;
import com.enjoyf.platform.util.sql.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-7-2
 * Time: 下午5:12
 * To change this template use File | Settings | File Templates.
 */
public class ImportAppDeploymentController {

    private static final Logger logger = LoggerFactory.getLogger(ImportAppDeploymentController.class);

    private static JoymeAppHandler joymeAppHandler;
    private static OAuthHandler oAuthHandler;

    public static void main(String[] args) {
        FiveProps servProps = Props.instance().getServProps();
        try {
            joymeAppHandler = new JoymeAppHandler("joymeapp", servProps);
            oAuthHandler = new OAuthHandler("oauth", servProps);
        } catch (DbException e) {
            System.exit(0);
            logger.error("update pointHandler error.");
        }
        importAppDeployment();
    }

    private static void importAppDeployment() {
        try {
            List<AuthApp> appList = oAuthHandler.queryAuthApp(new QueryExpress()
                    .add(QueryCriterions.eq(AuthAppField.APPTYPE, AuthAppType.INTERNAL_CLIENT.getCode()))
                    .add(QueryCriterions.in(AuthAppField.PLATOFRM, new Integer[]{AppPlatform.ANDROID.getCode(), AppPlatform.CLIENT.getCode(), AppPlatform.IOS.getCode()}))
                    .add(QueryCriterions.eq(AuthAppField.VALIDSTATUS, ValidStatus.VALID.getCode())));

            for (AuthApp app : appList) {
                WebApiHotdeployConfig config = HotdeployConfigFactory.get().getConfig(WebApiHotdeployConfig.class);
                AppVersionInfo versionInfo = config.getAppVersionInfoMap().get(app.getAppId() + "A");

                if (versionInfo != null) {
                    String appKey = getAppKey(versionInfo.getApp_key());
                    int platform = getPlatformByAppKey(versionInfo.getApp_key());
                    AppDeployment appDeployment = new AppDeployment();
                    appDeployment.setAppDeploymentType(AppDeploymentType.VERSION);
                    appDeployment.setAppkey(appKey);
                    appDeployment.setPath(versionInfo.getVersion_url());
                    appDeployment.setDescription(versionInfo.getVersion_info());
                    appDeployment.setTitle(versionInfo.getVersion());
                    appDeployment.setAppVersionUpdateType(AppVersionUpdateType.getByCode(versionInfo.getUpdate_type()));
                    appDeployment.setAppPlatform(AppPlatform.getByCode(platform));
                    appDeployment.setCreateDate(new Date());
                    appDeployment.setRemoveStatus(ActStatus.UNACT);
                    joymeAppHandler.createAppDeployment(appDeployment);
                    System.out.println("--------------------------version:" + appDeployment.getAppkey() + "----------------------------");
                }

                AppVersionInfo versionInfo2 = config.getAppVersionInfoMap().get(app.getAppId() + "I");

                if (versionInfo2 != null) {
                    String appKey = getAppKey(versionInfo2.getApp_key());
                    int platform = getPlatformByAppKey(versionInfo2.getApp_key());
                    AppDeployment appDeployment = new AppDeployment();
                    appDeployment.setAppDeploymentType(AppDeploymentType.VERSION);
                    appDeployment.setAppkey(appKey);
                    appDeployment.setPath(versionInfo2.getVersion_url());
                    appDeployment.setDescription(versionInfo2.getVersion_info());
                    appDeployment.setTitle(versionInfo2.getVersion());
                    appDeployment.setAppVersionUpdateType(AppVersionUpdateType.getByCode(versionInfo2.getUpdate_type()));
                    appDeployment.setAppPlatform(AppPlatform.getByCode(platform));
                    appDeployment.setCreateDate(new Date());
                    appDeployment.setRemoveStatus(ActStatus.UNACT);
                    joymeAppHandler.createAppDeployment(appDeployment);
                    System.out.println("--------------------------version:" + appDeployment.getAppkey() + "----------------------------");
                }

                AppCertificateInfo certificateInfo = config.getCertificatieInfoMap().get(app.getAppId());
                if (certificateInfo != null) {
                    AppDeployment appDeployment = new AppDeployment();
                    appDeployment.setAppDeploymentType(AppDeploymentType.CERTIFICATE);
                    appDeployment.setAppkey(certificateInfo.getApp_key());
                    appDeployment.setAppPlatform(AppPlatform.IOS);
                    appDeployment.setPath(certificateInfo.getPath());
                    appDeployment.setPassword(certificateInfo.getPassword());
                    appDeployment.setCreateDate(new Date());
                    appDeployment.setRemoveStatus(ActStatus.UNACT);
                    joymeAppHandler.createAppDeployment(appDeployment);
                    System.out.println("--------------------------certificate:" + appDeployment.getAppkey() + "----------------------------");
                }

            }
        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    private static String getAppKey(String appKey) {
        if (appKey.length() < 23) {
            return appKey;
        }

        return appKey.substring(0, appKey.length() - 1);
    }

    private static int getPlatformByAppKey(String appKey) {
        if (appKey.length() < 23) {
            return -1;
        } else if (appKey.endsWith("I")) {
            return 0;
        } else if (appKey.endsWith("A")) {
            return 1;
        }

        return -1;
    }


}
