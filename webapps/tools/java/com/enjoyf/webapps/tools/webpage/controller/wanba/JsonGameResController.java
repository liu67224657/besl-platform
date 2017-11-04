package com.enjoyf.webapps.tools.webpage.controller.wanba;

import com.enjoyf.platform.props.WebappConfig;
import com.enjoyf.platform.props.hotdeploy.HotdeployConfig;
import com.enjoyf.platform.props.hotdeploy.HotdeployConfigFactory;
import com.enjoyf.platform.props.hotdeploy.JoymeAppHotdeployConfig;
import com.enjoyf.platform.props.hotdeploy.WebHotdeployConfig;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.ask.AskServiceSngl;
import com.enjoyf.platform.service.ask.WikiGameres;
import com.enjoyf.platform.service.ask.WikiGameresField;
import com.enjoyf.platform.service.joymeapp.JoymeAppConfig;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.http.HttpClientManager;
import com.enjoyf.platform.util.http.HttpParameter;
import com.enjoyf.platform.util.http.HttpResult;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.webapps.common.ResultCodeConstants;
import com.enjoyf.webapps.tools.webpage.controller.base.ToolsBaseController;
import com.google.gson.Gson;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

/**
 * Created by pengxu on 2017/3/23.
 */
@Controller
@RequestMapping(value = "/json/joyme/gameres")
public class JsonGameResController extends AbstractWanbaController {

    @ResponseBody
    @RequestMapping(value = "/addgame")
    public String addGame(@RequestParam(value = "gameid", required = false) Long gameId,
                          @RequestParam(value = "gamename", required = false) String gameName,
                          @RequestParam(value = "wikikey", required = false) String wikiKey) {
        try {
            WikiGameres wikiGameres = AskServiceSngl.get().getWikiGameresByQueryExpress(new QueryExpress().add(QueryCriterions.eq(WikiGameresField.GAMEID, gameId)));
            if (wikiGameres != null) {
                return ResultCodeConstants.GAME_COLLECTION_HAS_EXIST.getJsonString();
            }

            boolean bool = remote(wikiKey, 0);//判断在wiki列表添加没有
            if (!bool) {
                return ResultCodeConstants.GAME_WIKI_NOT_ADD.getJsonString();
            }

            wikiGameres = new WikiGameres();
            wikiGameres.setGameId(gameId);
            wikiGameres.setCreateTime(new Date());
            wikiGameres.setGameName(gameName);
            wikiGameres.setHeadPic("");
            wikiGameres.setRecommend(0);
            wikiGameres.setUpdateTime(new Date());
            wikiGameres.setValidStatus(ValidStatus.INVALID);
            wikiGameres.setUpdateUser(getCurrentUser().getUsername());
            wikiGameres.setDisplayOrder(Integer.MAX_VALUE - (int) (System.currentTimeMillis() / 1000));
            wikiGameres = AskServiceSngl.get().insertWikiGameres(wikiGameres);

        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            return ResultCodeConstants.ERROR.getJsonString();
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            return ResultCodeConstants.ERROR.getJsonString();
        }
        return ResultCodeConstants.SUCCESS.getJsonString();
    }


}
