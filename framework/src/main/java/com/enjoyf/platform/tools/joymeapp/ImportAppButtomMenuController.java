package com.enjoyf.platform.tools.joymeapp;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.joymeapp.JoymeAppHandler;
import com.enjoyf.platform.db.oauth.OAuthHandler;
import com.enjoyf.platform.props.hotdeploy.HotdeployConfigFactory;
import com.enjoyf.platform.props.hotdeploy.WebApiHotdeployConfig;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.joymeapp.*;
import com.enjoyf.platform.service.oauth.AuthApp;
import com.enjoyf.platform.service.oauth.AuthAppField;
import com.enjoyf.platform.service.oauth.AuthAppType;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.FiveProps;
import com.enjoyf.platform.util.Props;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
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
public class ImportAppButtomMenuController {

    private static final Logger logger = LoggerFactory.getLogger(ImportAppButtomMenuController.class);

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
        importAppbuttomMenu();
    }

    private static void importAppbuttomMenu() {
        try {

            //1lOSAtZIx9pVZPtuzr66b0a     a8380331a0b89c468040a08000e06a0a   全民奇迹
//            UpdateExpress updateExpress = new UpdateExpress();
//            updateExpress.set(AuthAppField.APPID, "1lOSAtZIx9pVZPtuzr66b0");
//            QueryExpress queryExpress = new QueryExpress();
//            queryExpress.add(QueryCriterions.eq(AuthAppField.APPKEY, "a8380331a0b89c468040a08000e06a0a"));
//            oAuthHandler.updateAuthApp(updateExpress, queryExpress);

            List<JoymeAppMenu> menuList = joymeAppHandler.queryJoymeAppMenu(new QueryExpress().add(QueryCriterions.eq(JoymeAppMenuField.APPKEY, "1lOSAtZIx9pVZPtuzr66b0a")));
            for(JoymeAppMenu menu:menuList){
                System.out.println(menu.getMenuId());
                joymeAppHandler.updateJoymeAppMenu(new UpdateExpress().set(JoymeAppMenuField.APPKEY, "1lOSAtZIx9pVZPtuzr66b0"),
                        new QueryExpress().add(QueryCriterions.eq(JoymeAppMenuField.BUTTOM_MENU_ID, menu.getMenuId())));
            }
        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }



}
