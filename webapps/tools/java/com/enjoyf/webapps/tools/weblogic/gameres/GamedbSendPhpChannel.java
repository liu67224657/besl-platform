package com.enjoyf.webapps.tools.weblogic.gameres;

import com.enjoyf.platform.props.WebappConfig;
import com.enjoyf.platform.service.gameres.gamedb.GameDB;
import com.enjoyf.platform.util.http.HttpClientManager;
import com.enjoyf.platform.util.http.HttpParameter;
import com.enjoyf.platform.util.http.HttpResult;
import com.enjoyf.platform.util.log.GAlerter;
import com.google.gson.Gson;
import net.sf.json.JSONObject;

/**
 * Created by zhimingli on 2017/5/2.\
 * <p>
 * 文档地址：
 * http://wiki.enjoyf.com/wiki/%E6%B8%A0%E9%81%93%E5%90%8E%E5%8F%B0%E6%8E%A5%E5%8F%A3
 */
public class GamedbSendPhpChannel {


    private HttpClientManager httpClient = new HttpClientManager();


    public void sendPhpGamedb(GameDB gameDB) {
        if (gameDB == null) {
            GAlerter.lab("GamedbSendPhpChannel gamedb is null" + this.getClass());
            return;
        }


        //http://channel.joyme.dev/?c=source&a=savegame
        try {
            HttpParameter httpParameter[] = new HttpParameter[]{
                    new HttpParameter("gid", gameDB.getGameDbId()),
                    new HttpParameter("extra", new Gson().toJson(gameDB))
            };

            String domain = WebappConfig.get().getDomain();
            if (WebappConfig.get().getDomain().equals("joyme.test") || WebappConfig.get().getDomain().equals("joyme.dev")) {
                domain = "joyme.alpha";
            }

            HttpResult httpResult = httpClient.post("http://channel." + domain + "?c=source&a=savegame", httpParameter, null);
            if (httpResult.getReponseCode() == 200) {
                System.out.printf(new Gson().toJson(httpResult));


                JSONObject jsonObject = JSONObject.fromObject(httpResult.getResult());

                int rs = jsonObject.getInt("rs");
                if (rs != 1) {
                    GAlerter.lab(this.getClass() + "GamedbSendPhpChannel sendphp rs is:" + rs + ",gamedb:" + new Gson().toJson(gameDB));
                }

            } else {
                GAlerter.lab(this.getClass() + "GamedbSendPhpChannel sendphp responsecode is:" + httpResult.getReponseCode());
            }
        } catch (Exception e) {
            GAlerter.lab("GamedbSendPhpChannel Exception" + this.getClass(), e);
        }

    }


}
