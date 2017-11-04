package com.enjoyf.webapps.joyme.webpage.controller.joymeapp.gameclient.json;

import com.enjoyf.platform.service.JsonBinder;
import com.enjoyf.platform.service.gameres.GameResourceServiceSngl;
import com.enjoyf.platform.service.gameres.gamedb.GameDB;
import com.enjoyf.platform.service.gameres.gamedb.GameDBField;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.webapps.common.ResultObjectMsg;
import com.enjoyf.webapps.joyme.webpage.controller.joymeapp.gameclient.AbstractGameClientBaseController;
import com.mongodb.BasicDBObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by zhimingli
 * Date: 2015/01/15
 * Time: 17:26
 */
@Controller
@RequestMapping("/joymeapp/gameclient/json/gamedb/")
public class GameClientGameDBCoverJsonController extends AbstractGameClientBaseController {

    @ResponseBody
    @RequestMapping(value = "/agree")
    public String agree(HttpServletRequest request) {
        ResultObjectMsg msg = new ResultObjectMsg();
        msg.setRs(ResultObjectMsg.CODE_S);
        msg.setMsg("SUCCESS");
        msg.setResult("");
        try {
            String gameId = request.getParameter("gameId");
            BasicDBObject query = new BasicDBObject();
            query.put("_id", Long.valueOf(gameId));
            GameDB gameDB = GameResourceServiceSngl.get().getGameDB(query, false);

            if (gameDB != null) {
                BasicDBObject update = new BasicDBObject();
                if (gameDB.getGameDBCover() != null & StringUtil.isEmpty(gameDB.getGameDBCover().getCoverAgreeNum())) {
                    gameDB.getGameDBCover().setCoverAgreeNum("1");
                } else {
                    gameDB.getGameDBCover().setCoverAgreeNum((Integer.valueOf(gameDB.getGameDBCover().getCoverAgreeNum()) + 1) + "");
                }

                update.put(GameDBField.GAMEDB_COVER.getColumn(), gameDB.getGameDBCover().toJson());
                boolean bval = GameResourceServiceSngl.get().updateGameDB(query, update);
                if (!bval) {
                    msg.setRs(ResultObjectMsg.CODE_E);
                }
            }


        } catch (Exception e) {
            msg.setRs(ResultObjectMsg.CODE_E);
            msg.setMsg("system.error");
            msg.setResult("");
        }
        return JsonBinder.buildNormalBinder().toJson(msg);
    }


}
