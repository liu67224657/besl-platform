package com.enjoyf.platform.db.oauth.test;

import com.enjoyf.platform.service.joymeapp.AppPlatform;
import com.enjoyf.platform.service.oauth.GameChannelConfig;
import com.enjoyf.platform.service.oauth.GameChannelConfigField;
import com.enjoyf.platform.service.oauth.OAuthServiceSngl;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.util.Date;

/**
 * Created by ericliu on 16/1/25.
 */
public class TestMain {

    public static void main(String[] args) {
        GameChannelConfig config=new GameChannelConfig();
        config.setVersion("1.0.0.1");
        config.setAppkey("liuhao");
        config.setAppPlatform(AppPlatform.ANDROID);
        config.setChannel("liulian");
        config.setGamekey("lh");
        config.setDebug(true);
        config.setLastModifyIp("127.0.0.1");
        config.setLastModifyTime(new Date());
        config.setLastModifyUserId("liuhao");

        try {
            OAuthServiceSngl.get().createGameChannelConfig(config);
        } catch (ServiceException e) {
            e.printStackTrace();
        }


        try {
            System.out.println(OAuthServiceSngl.get().queryGameChannelConfig(new QueryExpress(), new Pagination(1, 1, 1)).getRows().get(0));

//            85fc8a91d51681a4661abb8e9c95ad8b
            System.out.println("get api:"+OAuthServiceSngl.get().getGameChannelConfig("85fc8a91d51681a4661abb8e9c95ad8b") );


            System.out.println("update api:"+OAuthServiceSngl.get().modifyGameChannelConfig(new UpdateExpress().set(GameChannelConfigField.VERSION,"1.0.22.2"),"85fc8a91d51681a4661abb8e9c95ad8b") );


            System.out.println("delete api:"+OAuthServiceSngl.get().deleteGameChannelConfig("85fc8a91d51681a4661abb8e9c95ad8b") );


        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }
}
