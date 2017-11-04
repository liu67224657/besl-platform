package com.enjoyf.platform.service.gameres.gamedb;

import com.enjoyf.platform.util.sql.ObjectFieldDBType;
import com.enjoyf.platform.util.sql.mongodb.AbstractMongoObjectField;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  13-11-22 下午12:35
 * Description:
 */
public class GameDBField extends AbstractMongoObjectField {
    //基本
    public static final GameDBField ID = new GameDBField("_id", ObjectFieldDBType.LONG);
    public static final GameDBField GAMEICON = new GameDBField("gameicon", ObjectFieldDBType.STRING);
    public static final GameDBField GAME_DESC = new GameDBField("gamedesc", ObjectFieldDBType.STRING);
    public static final GameDBField GAMENAME = new GameDBField("gamename", ObjectFieldDBType.STRING);
    public static final GameDBField ANOTHERNAME = new GameDBField("anothername", ObjectFieldDBType.STRING);
    public static final GameDBField GAMESIZE = new GameDBField("gamesize", ObjectFieldDBType.STRING);
    public static final GameDBField PLATFORMTYPE_ = new GameDBField("platformtype_", ObjectFieldDBType.BOOLEAN);
    public static final GameDBField GAME_PLATFORM_ = new GameDBField("game_platform_", ObjectFieldDBType.BOOLEAN);
    public static final GameDBField IOSDOWNLOAD = new GameDBField("iosdownload", ObjectFieldDBType.STRING);
    public static final GameDBField ANDROIDDOWNLOAD = new GameDBField("androiddownload", ObjectFieldDBType.STRING);
    public static final GameDBField WPDOWNLOAD = new GameDBField("wpdownload", ObjectFieldDBType.STRING);
    public static final GameDBField GAMEPUBLICTIME = new GameDBField("gamepublictime", ObjectFieldDBType.DATE);
    public static final GameDBField PUBLICTIPS = new GameDBField("publictips", ObjectFieldDBType.BOOLEAN);
    public static final GameDBField OFFICIALWEBSITE = new GameDBField("officialwebsite", ObjectFieldDBType.STRING);
    public static final GameDBField GAMENETTYPE = new GameDBField("gamenet", ObjectFieldDBType.INT);
    public static final GameDBField GAME_LANGUAGE_ = new GameDBField("game_language_", ObjectFieldDBType.BOOLEAN);
    public static final GameDBField GAME_CATEGORY_ = new GameDBField("game_category_", ObjectFieldDBType.BOOLEAN);
    public static final GameDBField GAME_THEME_ = new GameDBField("game_theme_", ObjectFieldDBType.BOOLEAN);
    public static final GameDBField GAMEDEVELOPER = new GameDBField("gamedeveloper", ObjectFieldDBType.STRING);
    public static final GameDBField GAMEPUBLICSHERS = new GameDBField("gamepublishers", ObjectFieldDBType.STRING);
    public static final GameDBField GAMEPROFILE = new GameDBField("gameprofile", ObjectFieldDBType.STRING);
    public static final GameDBField GAMEVIDEO = new GameDBField("gamevideo", ObjectFieldDBType.STRING);
    public static final GameDBField GAMEVIDEOPIC = new GameDBField("gamevideopic", ObjectFieldDBType.STRING);
    public static final GameDBField GAMEPIC = new GameDBField("gamepic", ObjectFieldDBType.STRING);
    public static final GameDBField GAME_PIC_TYPE = new GameDBField("pictype", ObjectFieldDBType.INT);
    public static final GameDBField WIKIURL = new GameDBField("wikiurl", ObjectFieldDBType.STRING);
    public static final GameDBField CMSURL = new GameDBField("cmsurl", ObjectFieldDBType.STRING);
    public static final GameDBField GAMERATE = new GameDBField("gamerate", ObjectFieldDBType.DOUBLE);
    public static final GameDBField FAVOR_SUM = new GameDBField("favorsum", ObjectFieldDBType.INT);
    public static final GameDBField UN_FAVOR_SUM = new GameDBField("unfavorsum", ObjectFieldDBType.INT);
    public static final GameDBField POPULAR = new GameDBField("popular", ObjectFieldDBType.INT);

    public static final GameDBField NEWSSUM = new GameDBField("newssum", ObjectFieldDBType.INT);
    public static final GameDBField VIDEOSUM = new GameDBField("videosum", ObjectFieldDBType.INT);
    public static final GameDBField GUIDESUM = new GameDBField("guidesum", ObjectFieldDBType.INT);
    public static final GameDBField PVSUM = new GameDBField("pvsum", ObjectFieldDBType.INT);
    public static final GameDBField GIFTSUM = new GameDBField("giftsum", ObjectFieldDBType.INT);

    public static final GameDBField VALIDSTATUS = new GameDBField("validstatus", ObjectFieldDBType.STRING);
    public static final GameDBField CREATE_DATE = new GameDBField("create_date", ObjectFieldDBType.DATE);
    public static final GameDBField CREATE_USER = new GameDBField("create_user", ObjectFieldDBType.STRING);
    public static final GameDBField MODIFY_DATE = new GameDBField("modify_date", ObjectFieldDBType.DATE);
    public static final GameDBField MODIFY_USER = new GameDBField("modify_user", ObjectFieldDBType.STRING);
    //玩霸
    public static final GameDBField VERSIONPROFILE = new GameDBField("versionprofile", ObjectFieldDBType.STRING);
    public static final GameDBField REASON = new GameDBField("recommend_reason", ObjectFieldDBType.STRING);
    public static final GameDBField REASON2 = new GameDBField("recommend_reason2", ObjectFieldDBType.STRING);
    public static final GameDBField MODIFY_TIME_JSON = new GameDBField("modify_time", ObjectFieldDBType.STRING);
    public static final GameDBField DISPLAY_ICON = new GameDBField("display_icon", ObjectFieldDBType.INT);
    public static final GameDBField GAMEDB_COVER = new GameDBField("cover", ObjectFieldDBType.STRING);
    public static final GameDBField GAMEDB_COVER_FIELD_JSON = new GameDBField("cover_field_json", ObjectFieldDBType.STRING);
    public static final GameDBField DOWNLOADRECOMMEND = new GameDBField("downloadRecommend", ObjectFieldDBType.STRING);
    //商务
    public static final GameDBField TEAMNAME = new GameDBField("teamname", ObjectFieldDBType.STRING);
    public static final GameDBField TEAMNUM = new GameDBField("teamnum", ObjectFieldDBType.INT);
    public static final GameDBField CITY = new GameDBField("city", ObjectFieldDBType.STRING);
    public static final GameDBField PUBLICTIME = new GameDBField("publictime", ObjectFieldDBType.DATE);
    public static final GameDBField FINANCING = new GameDBField("financing", ObjectFieldDBType.INT);
    public static final GameDBField CONTACTS = new GameDBField("contacts", ObjectFieldDBType.STRING);
    public static final GameDBField EMAIL = new GameDBField("email", ObjectFieldDBType.STRING);
    public static final GameDBField PHONE = new GameDBField("phone", ObjectFieldDBType.STRING);
    public static final GameDBField QQ = new GameDBField("qq", ObjectFieldDBType.STRING);
    public static final GameDBField AREA = new GameDBField("area", ObjectFieldDBType.INT);
    //主站首页
    public static final GameDBField COMMENT_AND_AGREE = new GameDBField("comment_and_agree", ObjectFieldDBType.STRING);
    //渠道下载
    public static final GameDBField CHANNEL_PLATFORM_ = new GameDBField("channel_platform_", ObjectFieldDBType.BOOLEAN);
    public static final GameDBField CHANNEL_DOWNLOAD_INFO_ = new GameDBField("channel_download_info_", ObjectFieldDBType.STRING);

    public static final GameDBField PC_CONFIGURATION_INFO = new GameDBField("pc_configuration_info", ObjectFieldDBType.STRING);

    public static final GameDBField ASSOCIATED_GAME_ID = new GameDBField("associated_gameid", ObjectFieldDBType.LONG);

    public static final GameDBField WIKIKEY = new GameDBField("wiki_key", ObjectFieldDBType.STRING);

    public static final GameDBField PC_DOWNLOAD = new GameDBField("pc_download", ObjectFieldDBType.STRING);

    public static final GameDBField LEVELGAME = new GameDBField("levelgame", ObjectFieldDBType.BOOLEAN);
    public static final GameDBField XBOXONEDOWNLOAD = new GameDBField("xboxonedownload", ObjectFieldDBType.STRING);
    public static final GameDBField PS4DOWNLOAD = new GameDBField("ps4download", ObjectFieldDBType.STRING);

    public static final GameDBField WEBPAGEDOWNLOAD = new GameDBField("webpagedownload", ObjectFieldDBType.STRING);


    public static final GameDBField ISBN = new GameDBField("isbn", ObjectFieldDBType.STRING);
    public static final GameDBField COMMENT = new GameDBField("comment", ObjectFieldDBType.BOOLEAN);
    public static final GameDBField ENGLISHNAME = new GameDBField("englishname", ObjectFieldDBType.STRING);
    public static final GameDBField BACKPIC = new GameDBField("backpic", ObjectFieldDBType.STRING);

    public static final GameDBField APPSTOREPRICE = new GameDBField("appstoreprice", ObjectFieldDBType.STRING);
    public static final GameDBField VIDEO = new GameDBField("video", ObjectFieldDBType.STRING);
    public static final GameDBField VPN = new GameDBField("vpn", ObjectFieldDBType.BOOLEAN);
    public static final GameDBField COMMENTSCORE = new GameDBField("commentscore", ObjectFieldDBType.STRING);
    public static final GameDBField COMMENTSUM = new GameDBField("commentsum", ObjectFieldDBType.STRING);
    public static final GameDBField COMMENTGAMEPIC = new GameDBField("commentgamepic", ObjectFieldDBType.STRING);
    public static final GameDBField GAMETAG = new GameDBField("gametag", ObjectFieldDBType.STRING);



    public GameDBField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
