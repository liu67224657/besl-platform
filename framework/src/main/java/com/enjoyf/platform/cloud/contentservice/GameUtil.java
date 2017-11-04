package com.enjoyf.platform.cloud.contentservice;

import com.alibaba.fastjson.JSON;
import com.enjoyf.platform.cloud.OkHttpUtil;
import com.enjoyf.platform.cloud.contentservice.game.Game;
import com.enjoyf.platform.cloud.contentservice.game.GameTag;
import com.enjoyf.platform.props.WebappConfig;
import com.enjoyf.platform.service.gameres.gamedb.GameDB;
import com.enjoyf.platform.service.gameres.gamedb.GameLanguageType;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.google.common.base.Joiner;
import com.google.gson.JsonObject;
import com.squareup.okhttp.Response;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhimingli on 2017/6/25.
 */
public class GameUtil {


    public static void updateGameTags(GameDB gameDB, String authorization) {
        if (gameDB == null) {
            return;
        }
        String url = WebappConfig.get().getContentServiceUrl() + "/api/game-tags/gamedb/" + gameDB.getGameDbId() + "/" + gameDB.getGameTag();
        OkHttpUtil.doPost(url, new JsonObject(), authorization, null);
    }


    public static List<GameTag> getGameTags(String gameTags, String authorization) {
        List<GameTag> gameTagList = new ArrayList<GameTag>();
        if (StringUtil.isEmpty(gameTags)) {
            return gameTagList;
        }
        String url = WebappConfig.get().getContentServiceUrl() + "/api/game-tags/ids?ids=" + gameTags;

        Response response = OkHttpUtil.doGet(url, authorization, null);
        if (response.isSuccessful() && !StringUtil.isEmpty(response.body().toString())) {
            try {
                gameTagList = JSON.parseArray(response.body().string(), GameTag.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return gameTagList;
    }


    /**
     * 游戏库更新
     *
     * @param gameDB
     */
    public static void updateGame(GameDB gameDB, String authorization, String recommend, String recommendAuth) {
        if (gameDB == null) {
            return;
        }
        String url = WebappConfig.get().getContentServiceUrl() + "/api/games/" + gameDB.getGameDbId();
        Response response = OkHttpUtil.doGet(url, authorization, null);
        if (response.isSuccessful()) {
            Game game = null;
            try {
                game = JSON.parseObject(response.body().string(), Game.class);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (game == null) {
                return;
            }

            JsonObject json = new JsonObject();
            json.addProperty("id", gameDB.getGameDbId());
            json.addProperty("name", gameDB.getGameName());
            json.addProperty("aliasName", gameDB.getAnotherName());
            json.addProperty("englishName", gameDB.getEnglishName());
            json.addProperty("gameTag", gameDB.getGameTag());
            json.addProperty("validStatus", gameDB.getValidStatus().getCode());

            JsonObject extjson = new JsonObject();
            extjson.addProperty("gameLogo", gameDB.getGameIcon());
            extjson.addProperty("gameDeveloper", gameDB.getGameDeveloper());
            extjson.addProperty("iosDownload", gameDB.getIosDownload());
            extjson.addProperty("androidDownload", gameDB.getAndroidDownload());
            extjson.addProperty("video", gameDB.getGameVideo());
            extjson.addProperty("backPic", gameDB.getBackpic());
            if (!StringUtil.isEmpty(recommend)) {
                extjson.addProperty("recommend", recommend);
            }
            if (!StringUtil.isEmpty(recommendAuth)) {
                extjson.addProperty("recommendAuth", recommendAuth);
            }
            extjson.addProperty("pic", gameDB.getCommentGamePic());
            extjson.addProperty("gameDesc", gameDB.getGameProfile());
            extjson.addProperty("createUser", gameDB.getCreateUser());
            extjson.addProperty("vpn", gameDB.isVpn());
            extjson.addProperty("price", StringUtil.isEmpty(gameDB.getAppstorePrice()) ? "" : gameDB.getAppstorePrice());

            List<String> language = new ArrayList<String>();
            if (!CollectionUtils.isEmpty(gameDB.getLanguageTypeSet())) {
                for (GameLanguageType languageType : gameDB.getLanguageTypeSet()) {
                    language.add(languageType.getName());
                }
            }
            extjson.addProperty("language", StringUtil.isEmpty(Joiner.on(",").join(language)) ? "" : Joiner.on(",").join(language));
            json.add("extjson", extjson);
            String urlstr = WebappConfig.get().getContentServiceUrl() + "/api/games";
            OkHttpUtil.doPost(urlstr, json, authorization, null);
        } else {
            GAlerter.lab("GameUtil.updateGame:id," + gameDB.getGameDbId() + "," + response.message());
        }
    }


}
