/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.tools;


import com.google.common.base.Strings;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 12-1-30 下午5:27
 * Description:
 */
public class LogOperType implements Serializable {
    //
    private static Map<String, LogOperType> map = new HashMap<String, LogOperType>();
    //
    public static LogOperType RESOURCE_MODIFYRESPAGE = new LogOperType("privilege", "resource.modifyrespage");
    public static LogOperType RESOURCE_REMOVERES = new LogOperType("privilege", "resource.removeres");

    public static LogOperType ROLES_MODIFYROLESPAGE = new LogOperType("privilege", "role.modifyrolespage");
    public static LogOperType ROLES_SAVEROLESPAGE = new LogOperType("privilege", "role.saverolespage");
    public static LogOperType ROLES_SAVEROLESMENU = new LogOperType("privilege", "role.saverolesmenu");
    public static LogOperType ROLES_REMOVEROLES = new LogOperType("privilege", "role.removeroles");

    public static LogOperType USER_MODIFYUSERPAGE = new LogOperType("privilege", "user.modifyuserpage");
    public static LogOperType USER_SAVEPWD = new LogOperType("privilege", "user.savepwd");
    public static LogOperType USER_SAVEUSERPAGE = new LogOperType("privilege", "user.saveuserpage");
    public static LogOperType USER_REMOVEUSER = new LogOperType("privilege", "user.removeuser");
    public static LogOperType USER_MODIFYUSER = new LogOperType("privilege", "user.modifyuser");
    public static LogOperType USER_SAVEUSERROLE = new LogOperType("privilege", "user.saveuserrole");


    public static LogOperType LINE_SAVELINE = new LogOperType("viewline", "line.saveline");
    public static LogOperType LINE_REMOVELINE = new LogOperType("viewline", "line.removeline");
    public static LogOperType LINE_EDITLINE = new LogOperType("viewline", "line.editline");
    public static LogOperType LINE_SAVEASSIGNLINE = new LogOperType("viewline", "line.saveassignline");


    public static LogOperType LINEITEM_SAVELINEITEM = new LogOperType("viewlineitem", "lineitem.savelineitem");
    public static LogOperType LINEITEM_REMOVELINEITEM = new LogOperType("viewlineitem", "lineitem.removelineitem");
    public static LogOperType LINEITEM_EDITLINEITEM = new LogOperType("viewlineitem", "lineitem.editlineitem");
    public static LogOperType LINEITEM_SORTLINEITEM = new LogOperType("viewlineitem", "lineitem.sortlineitem");

    public static LogOperType GLYXTM_MODIFYGAMERESPAGE = new LogOperType("gameres", "glyxtm.modifygamerespage");
    public static LogOperType CJYXTM_SAVEGAMERESPAGE = new LogOperType("gameres", "cjyxtm.savegamerespage");
    public static LogOperType SCYXTM_REMOVEGAMERES = new LogOperType("gameres", "scyxtm.removegameres");
    public static LogOperType GLHTTM_MODIFYGAMETOPICPAGE = new LogOperType("gameres", "glhttm.modifygametopicpage");
    public static LogOperType CJHTTM_SAVEGAMETOPICPAGE = new LogOperType("gameres", "cjhttm.savegametopicpage");
    public static LogOperType SCHTTM_REMOVEGAMETOPIC = new LogOperType("gameres", "schttm.removegametopic");

    public static LogOperType GAMEREPRIVACY_MODIFY = new LogOperType("gameres", "gameprivacy.modify");
    public static LogOperType GAMEREPRIVACY_CREATE = new LogOperType("gameres", "gameprivacy.create");
    public static LogOperType GAMEREPRIVACY_DELETE = new LogOperType("gameres", "gameprivacy.delete");

    public static LogOperType WIKIRESOURCE_SAVE = new LogOperType("gameres", "wikiresource.create");
    public static LogOperType WIKIRESOURCE_MODIFY = new LogOperType("gameres", "wikiresource.modify");
    public static LogOperType WIKIRESOURCE_DELETE = new LogOperType("gameres", "wikiresource.delete");
    public static LogOperType CJHTTM_BATCHMODIFY = new LogOperType("gameres", "wikiresource.batchmodify");


    public static LogOperType TEXT_MODIFYCONTENTAUDITSTATUSSNGL = new LogOperType("text", "modifycontentauditstatussngl");
    public static LogOperType TEXT_BATCHUPDATECONTENTTEXT = new LogOperType("text", "batchupdatecontenttext");

    public static LogOperType IMG_MODIFYIMAGEAUDITSTATUSSNGL = new LogOperType("img", "modifyimageauditstatussngl");
    public static LogOperType IMG_BATCHUPDATECONTENTIMAGE = new LogOperType("img", "batchupdatecontentimage");

    public static LogOperType REPLY_MODIFYREPLYAUDITSTATUSSNGL = new LogOperType("reply", "modifyreplyauditstatussngl");
    public static LogOperType REPLY_BATCHUPDATECONTENTREPLY = new LogOperType("reply", "batchupdatecontentreply");

    public static LogOperType PROFILE_AUDITMULTIPLEPROFILES = new LogOperType("profile", "auditmultipleprofiles");
    public static LogOperType PROFILE_BAN = new LogOperType("profile", "ban");
    public static LogOperType PROFILE_JSONDELETEHEADICON = new LogOperType("profile", "jsondeleteheadicon");
    public static LogOperType PROFILE_JSONRESTOREHEADICON = new LogOperType("profile", "jsonrestoreheadicon");
    public static LogOperType PROFILE_JSONDELETEBLOGDESC = new LogOperType("profile", "jsondeleteblogdesc");
    public static LogOperType PROFILE_JSONRESTOREDESC = new LogOperType("profile", "jsonrestoredesc");

    public static LogOperType PROFILE_JSONDELETEPLAYINGGAMES = new LogOperType("profile", "jsondeleteplayinggames");
    public static LogOperType PROFILE_JSONRESTOREPLAYINGGAMES = new LogOperType("profile", "jsonrestoreplayinggames");

    public static LogOperType IPMANAGEMENT_INCREASEIPFORBIDDEN = new LogOperType("ipmanagement", "increaseipforbidden");
    public static LogOperType IPMANAGEMENT_BATCHUPDATE = new LogOperType("ipmanagement", "batchupdate");

    public static LogOperType BATCHMANAGE_BATCHCODE = new LogOperType("batchmanage", "batchmanage.batchcode");

    public static LogOperType CREATE_JOYMEAPP_PUSHMESSAGE = new LogOperType("joymeapp", "create.push.message");
    public static LogOperType SEND_JOYMEAPP_PUSHMESSAGE = new LogOperType("joymeapp", "send.push.message");
    public static LogOperType MODIFY_JOYMEAPP_PUSHMESSAGE = new LogOperType("joymeapp", "modify.push.message");
    public static LogOperType DELETE_JOYMEAPP_PUSHMESSAGE = new LogOperType("joymeapp", "delete.push.message");

    public static LogOperType CREATE_JOYMEAPP_INFO = new LogOperType("joymeapp", "create.app.info");
    public static LogOperType MODIFY_JOYMEAPP_INFO = new LogOperType("joymeapp", "modify.app.info");

    public static LogOperType MODIFY_SHARE_BASE_INFO = new LogOperType("share", "modify.share.baseinfo");
    public static LogOperType MODIFY_SHARE_BODY = new LogOperType("share", "modify.share.sharebody");
    public static LogOperType MODIFY_SHARE_TOPIC = new LogOperType("share", "modify.share.sharetopic");

    public static LogOperType MODIFY_GOODS = new LogOperType("point", "modify.point.goods");
    public static LogOperType MODIFY_EXCHANGE_GOODS = new LogOperType("point", "modify.point.exchangegoods");
    public static LogOperType ADMIN_MODIFY_POINT = new LogOperType("point", "admin.modify.point");
    public static LogOperType MODIFY_LOTTERY = new LogOperType("lottery", "modify.lottery");

    public static LogOperType MODIFY_LOTTERY_AWARD = new LogOperType("lottery", "modify.lottery.award");

    public static LogOperType CREATE_JOYMEAPP_TOPMENU = new LogOperType("joymeapp", "create.joymeapp.topmenu");
    public static LogOperType MODIFY_JOYMEAPP_TOPMENU = new LogOperType("joymeapp", "modify.joymeapp.topmenu");
    public static LogOperType DELETE_JOYMEAPP_TOPMENU = new LogOperType("joymeapp", "delete.joymeapp.topmenu");
    public static LogOperType RECOVER_JOYMEAPP_TOPMENU = new LogOperType("joymeapp", "recover.joymeapp.topmenu");

    public static LogOperType CREATE_JOYMEAPP_MENU = new LogOperType("joymeapp", "create.joymeapp.menu");
    public static LogOperType MODIFY_JOYMEAPP_MENU = new LogOperType("joymeapp", "modify.joymeapp.menu");
    public static LogOperType DELETE_JOYMEAPP_MENU = new LogOperType("joymeapp", "delete.joymeapp.menu");
    public static LogOperType RECOVER_JOYMEAPP_MENU = new LogOperType("joymeapp", "recover.joymeapp.menu");


    public static LogOperType CREATE_JOYMEAPP_SUBMENU = new LogOperType("joymeapp", "create.joymeapp.submenu");
    public static LogOperType MODIFY_JOYMEAPP_SUBMENU = new LogOperType("joymeapp", "modify.joymeapp.submenu");
    public static LogOperType DELETE_JOYMEAPP_SUBMENU = new LogOperType("joymeapp", "delete.joymeapp.submenu");
    public static LogOperType RECOVER_JOYMEAPP_SUBMENU = new LogOperType("joymeapp", "recover.joymeapp.submenu");

    ///
    public static LogOperType ADVERTISE_APPURL_CREATE = new LogOperType("advertise", "create.advertise.appurl");
    public static LogOperType CREATE_ACTICVITY_EXCHANGE = new LogOperType("activity", "create.activity.exchange");
    public static LogOperType DELETE_ACTICVITY_EXCHANGE = new LogOperType("activity", "delete.activity.exchange");
    public static LogOperType RECOVER_ACTICVITY_EXCHANGE = new LogOperType("activity", "recover.activity.exchange");
    public static LogOperType MODIFY_ACTICVITY_EXCHANGE = new LogOperType("activity", "modify.activity.exchange");


    //new game preview
    public static final LogOperType VERIFY_NEW_GAME_INFO = new LogOperType("gameresource", "verify.newrelease.submenu");
    public static final LogOperType REMOVE_NEW_GAME_INFO = new LogOperType("gameresource", "remove.newrelease.submenu");
    //
    private String module;
    private String oper;
    public static final LogOperType DELETE_ACTIVITY_TOP_MENU = new LogOperType("joymeapp", "delete.activity.topmenu");
    public static final LogOperType RECOVER_ACTIVITY_TOP_MENU = new LogOperType("joymeapp", "recover.activity.topmenu");
    public static final LogOperType MODIFY_ACTIVITY_TOP_MENU = new LogOperType("joymeapp", "modify.activity.topmenu");

    public static final LogOperType DELETE_ACTICVITY = new LogOperType("activity", "delete.activity.goods");
    public static final LogOperType RECOVER_ACTICVITY = new LogOperType("activity", "recover.activity.goods");
    public static final LogOperType MODIFY_ACTICVITY = new LogOperType("activity", "modify.activity.goods");
    public static final LogOperType GROUP_MODIFY_ROLE = new LogOperType("gameresource", "group.role.modify");
    public static final LogOperType GROUP_DELETE_ROLE = new LogOperType("gameresource", "group.role.delete");
    public static final LogOperType GROUP_RECOVER_ROLE = new LogOperType("gameresource", "group.role.recover");
    public static final LogOperType GROUP_MODIFY_ROLE_PRIVILEGE_RELATION = new LogOperType("gameresource", "role.privilege.relation.modify");
    public static final LogOperType GROUP_DELETE_ROLE_PRIVILEGE_RELATION = new LogOperType("gameresource", "role.privilege.relation.delete");
    public static final LogOperType GROUP_RECOVER_ROLE_PRIVILEGE_RELATION = new LogOperType("gameresource", "role.privilege.relation.recover");
    public static final LogOperType CREATE_CLIENT_LINE = new LogOperType("joymeapp", "client.line.create");
    public static final LogOperType MODIFY_CLIENT_LINE = new LogOperType("joymeapp", "client.line.modify");
    public static final LogOperType DELETE_CLIENT_LINE = new LogOperType("joymeapp", "client.line.delete");
    public static final LogOperType RECOVER_CLIENT_LINE = new LogOperType("joymeapp", "client.line.recover");

    public static final LogOperType CREATE_CLIENT_LINE_ITEM = new LogOperType("joymeapp", "client.line.item.create");
    public static final LogOperType MODIFY_CLIENT_LINE_ITEM = new LogOperType("joymeapp", "client.line.item.modify");
    public static final LogOperType DELETE_CLIENT_LINE_ITEM = new LogOperType("joymeapp", "client.line.item.delete");
    public static final LogOperType RECOVER_CLIENT_LINE_ITEM = new LogOperType("joymeapp", "client.line.item.recover");
    public static final LogOperType SORT_CLIENT_LINE_ITEM = new LogOperType("joymeapp", "client.line.item.sort");
    public static final LogOperType UPDATE_CACHE_CLIENT_LINE = new LogOperType("joymeapp", "client.line.update.cache");

    public static final LogOperType MODIFY_CLIENT_LINE_FLAG = new LogOperType("joymeapp", "client.line.flag.modify");
    public static final LogOperType DELETE_CLIENT_LINE_FLAG = new LogOperType("joymeapp", "client.line.flag.delete");
    public static final LogOperType RECOVER_CLIENT_LINE_FLAG = new LogOperType("joymeapp", "client.line.flag.recover");
    public static final LogOperType MODIFY_FORIGN_CONTENT_REPLAY = new LogOperType("CONTENT", "forign.content.replay.modify");

    public static final LogOperType MODIFY_TASK = new LogOperType("event", "task.modify");
    public static final LogOperType CREATE_TASK = new LogOperType("event", "task.create");
    public static final LogOperType REMOVE_TASK = new LogOperType("event", "task.remove");
    public static final LogOperType PUBLISH_TASK = new LogOperType("event", "task.publish");
    public static final LogOperType RECOVER_TASK = new LogOperType("event", "task.recover");
    public static final LogOperType SORT_TASK = new LogOperType("event", "task.sort");

    public static final LogOperType MODIFY_TASK_GROUP = new LogOperType("event", "taskgroup.modify");
    public static final LogOperType CREATE_TASK_GROUP = new LogOperType("event", "taskgroup.create");
    public static final LogOperType REMOVE_TASK_GROUP = new LogOperType("event", "taskgroup.remove");
    public static final LogOperType RECOVER_TASK_GROUP = new LogOperType("event", "taskgroup.recover");
    public static final LogOperType PUBLISH_TASK_GROUP = new LogOperType("event", "taskgroup.publish");
    public static final LogOperType SORT_TASK_GROUP = new LogOperType("event", "taskgroup.sort");

    public static final LogOperType POINT_PW_R_WALL_APP_DELETE = new LogOperType("point", "pw_r_wall_app.delete");
    public static final LogOperType POINT_PW_WALL_DELETE = new LogOperType("point", "pw_wall.delete");
    public static final LogOperType POINT_PW_APP_DELETE = new LogOperType("point", "pw_app.delete");


    public static final LogOperType POINT_POINTWALL_WALL_ADD = new LogOperType("point", "pw_wall.add");
    public static final LogOperType POINT_POINTWALL_WALL_MODIFY = new LogOperType("point", "pw_wall.modify");

    public static final LogOperType POINT_POINTWALL_APP_ADD = new LogOperType("point", "pw_app.add");
    public static final LogOperType POINT_POINTWALL_APP_MODIFY = new LogOperType("point", "pw_app.modify");


    public static final LogOperType POINT_POINTWALL_R_APP_ADD = new LogOperType("point", "pw_r_wall_app.add");
    public static final LogOperType POINT_POINTWALL_R_APP_MODIFY = new LogOperType("point", "pw_r_wall_app.modify");

    public static final LogOperType POINT_TASKLOG_EXPORT = new LogOperType("point", "pw_tasklog.export");

    //////////////////////////外部系统用start========================
    public static final LogOperType WIKI_ADD = new LogOperType("wiki", "add");
    public static final LogOperType WIKI_UPDATE = new LogOperType("wiki", "update");
    public static final LogOperType WIKI_DELETE = new LogOperType("wiki", "delete");
    public static final LogOperType WIKI_QUERY = new LogOperType("wiki", "query");

    public static final LogOperType JOYMEWIKI_ADD = new LogOperType("joymewiki", "add");
    public static final LogOperType JOYMEWIKI_UPDATE = new LogOperType("joymewiki", "update");
    public static final LogOperType JOYMEWIKI_DELETE = new LogOperType("joymewiki", "delete");
    public static final LogOperType JOYMEWIKI_QUERY = new LogOperType("joymewiki", "query");

    public static final LogOperType ZOZOKA_ADD = new LogOperType("zozoka", "add");
    public static final LogOperType ZOZOKA_UPDATE = new LogOperType("zozoka", "update");
    public static final LogOperType ZOZOKA_DELETE = new LogOperType("zozoka", "delete");
    public static final LogOperType ZOZOKA_QUERY = new LogOperType("zozoka", "query");

    public static final LogOperType MARTICLE_ADD = new LogOperType("marticle", "add");
    public static final LogOperType MARTICLE_UPDATE = new LogOperType("marticle", "update");
    public static final LogOperType MARTICLE_DELETE = new LogOperType("marticle", "delete");
    public static final LogOperType MARTICLE_QUERY = new LogOperType("marticle", "query");

    public static final LogOperType CMSIMAGE_ADD = new LogOperType("cmsimage", "add");
    public static final LogOperType CMSIMAGE_UPDATE = new LogOperType("cmsimage", "update");
    public static final LogOperType CMSIMAGE_DELETE = new LogOperType("cmsimage", "delete");
    public static final LogOperType CMSIMAGE_QUERY = new LogOperType("cmsimage", "query");

    public static final LogOperType CMS_ADD = new LogOperType("cms", "add");
    public static final LogOperType CMS_UPDATE = new LogOperType("cms", "update");
    public static final LogOperType CMS_DELETE = new LogOperType("cms", "delete");
    public static final LogOperType CMS_QUERY = new LogOperType("cms", "query");
    public static final LogOperType CMS_REFRESH_ADD = new LogOperType("cms", "refresh.add");
    public static final LogOperType CMS_REFRESH_MODIFY = new LogOperType("cms", "refresh.modify");

    public static final LogOperType PHPRELEASE_ADD = new LogOperType("phprelease", "add");

    //////////////////////////外部系统用end========================

    //玩霸轮播图
    public static final LogOperType GAMECLIENT_CUSTOM_ADD = new LogOperType("gameclient", "custom.add");
    public static final LogOperType GAMECLIENT_CUSTOM_MODIFY = new LogOperType("gameclient", "custom.modify");
    public static final LogOperType GAMECLIENT_CUSTOM_DELETE = new LogOperType("gameclient", "custom.delete");


    //游戏资料库
    public static final LogOperType GAMEDB_ADD = new LogOperType("gamedb", "add");
    public static final LogOperType GAMEDB_MODIFY = new LogOperType("gamedb", "modify");
    public static final LogOperType GAMEDB_DELETE = new LogOperType("gamedb", "delete");
    public static final LogOperType GAMEDB_RECOVER = new LogOperType("gamedb", "recover");

    //新游开测，大家正在玩，热门
    public static final LogOperType GAMECLIENT_GAME_ADD = new LogOperType("gameclient", "game.add");
    public static final LogOperType GAMECLIENT_GAME_MODIFY = new LogOperType("gameclient", "game.modify");
    public static final LogOperType GAMECLIENT_GAME_DELETE = new LogOperType("gameclient", "game.delete");


    //热门页今日推荐
    public static final LogOperType GAMECLIENT_TODAY_RECOMMEND_ADD = new LogOperType("gameclient", "today_recommend.add");
    public static final LogOperType GAMECLIENT_TODAY_RECOMMEND_MODIFY = new LogOperType("gameclient", "today_recommend.modify");
    public static final LogOperType GAMECLIENT_TODAY_RECOMMEND_DELETE = new LogOperType("gameclient", "today_recommend.delete");


    //热门页自定义楼层
    public static final LogOperType GAMECLIENT_HOT_FLOOR_ADD = new LogOperType("gameclient", "hotfloor.add");
    public static final LogOperType GAMECLIENT_HOT_FLOOR_MODIFY = new LogOperType("gameclient", "hotfloor.modify");
    public static final LogOperType GAMECLIENT_HOT_FLOOR_DELETE = new LogOperType("gameclient", "hotfloor.delete");


    //热门页游戏分类
    public static final LogOperType GAMECLIENT_GAME_CATEGORY_FIRST_ADD = new LogOperType("gameclient", "game.category.first.add");
    public static final LogOperType GAMECLIENT_GAME_CATEGORY_FIRST_MODIFY = new LogOperType("gameclient", "game.category.first.modify");
    public static final LogOperType GAMECLIENT_GAME_CATEGORY_FIRST_ENABLED_OR_DISABLED = new LogOperType("gameclient", "game.category.first.enabled.or.disabled");

    public static final LogOperType GAMECLIENT_GAME_CATEGORY_SECOND_ADD = new LogOperType("gameclient", "game.category.second.add");
    public static final LogOperType GAMECLIENT_GAME_CATEGORY_SECOND_MODIFY = new LogOperType("gameclient", "game.category.second.modify");
    public static final LogOperType GAMECLIENT_GAME_CATEGORY_SECOND_ENABLED_OR_DISABLED = new LogOperType("gameclient", "game.category.second.enabled.or.disabled");

    public static final LogOperType GAMECLIENT_GAME_CATEGORY_ITEM_ADD = new LogOperType("gameclient", "game.category.item.add");
    public static final LogOperType GAMECLIENT_GAME_CATEGORY_ITEM_MODIFY = new LogOperType("gameclient", "game.category.item.modify");
    public static final LogOperType GAMECLIENT_GAME_CATEGORY_ITEM_DELETE = new LogOperType("gameclient", "game.category.item.delete");

    //评论 ，包括评论禁言用户管理
    public static final LogOperType COMMENT_FORBID_ADD = new LogOperType("comment", "forbid.add");
    public static final LogOperType COMMENT_FORBID_MODIFY = new LogOperType("comment", "forbid.modify");
    public static final LogOperType COMMENT_FORBID_DELETE = new LogOperType("comment", "forbid.delete");

    public static final LogOperType COMMENT_BATCHUPDATE = new LogOperType("comment", "batchupdate");
    public static final LogOperType COMMENT_ALLREMOVE = new LogOperType("comment", "allremove");
    public static final LogOperType COMMENT_REPLY = new LogOperType("comment", "reply");

    //wiki 投票管理
    public static final LogOperType WIKI_VOTE_CREATE = new LogOperType("wikivote", "add");
    public static final LogOperType WIKI_VOTE_MODIFY = new LogOperType("wikivote", "modify");
    public static final LogOperType WIKI_VOTE_DELETE = new LogOperType("wikivote", "delete");

    public static final LogOperType WIKI_VOTE_OPTION_CREATE = new LogOperType("wikivote", "option.add");
    public static final LogOperType WIKI_VOTE_OPTION_MODIFY = new LogOperType("wikivote", "option.modify");
    public static final LogOperType WIKI_VOTE_OPTION_DELETE = new LogOperType("wikivote", "option.delete");


    public static final LogOperType TAG_ANIME_ADD = new LogOperType("tag", "anime.add");
    public static final LogOperType TAG_ANIME_UPDATE = new LogOperType("tag", "anime.update");
    public static final LogOperType TAG_ANIME_UPDATE_STATUS = new LogOperType("tag", "anime.update.status");
    public static final LogOperType TAG_ANIME_SORT = new LogOperType("tag", "anime.sort");

    public static final LogOperType TAG_ADD = new LogOperType("tag", "add");
    public static final LogOperType TAG_UPDATE = new LogOperType("tag", "update");
    public static final LogOperType TAG_ARCHIVE_UPDATE = new LogOperType("tag", "archive.update");
    public static final LogOperType TAG_SORT = new LogOperType("tag", "sort");
    public static final LogOperType TAG_ARCHIVE_SORT = new LogOperType("tag", "archive.sort");
    public static final LogOperType DELETE_TAG_DEDE_ARCHIVES = new LogOperType("joymeapp", "delete.tag.dede.archives");
    public static final LogOperType RECOVER_TAG_DEDE_ARCHIVES = new LogOperType("joymeapp", "recover.tag.dede.archives");

    public static final LogOperType ANIME_HISTORY_ADD = new LogOperType("anime", "history.add");
    public static final LogOperType ANIME_HISTORY_UPDATE = new LogOperType("anime", "history.update");
    public static final LogOperType ANIME_HISTORY_UPDATE_STATUS = new LogOperType("anime", "history.update.status");

    public static final LogOperType ANIME_TV_ADD = new LogOperType("anime", "tv.add");
    public static final LogOperType ANIME_TV_SORT = new LogOperType("anime", "tv.sort");
    public static final LogOperType ANIME_TV_UPDATE = new LogOperType("anime", "tv.update");

    public static final LogOperType ANIME_INDEX_ADD = new LogOperType("anime", "index.add");
    public static final LogOperType ANIME_INDEX_RECOVER = new LogOperType("anime", "index.recover");
    public static final LogOperType ANIME_INDEX_UPDATE = new LogOperType("anime", "index.update");
    public static final LogOperType ANIME_INDEX_DETETE = new LogOperType("anime", "index.detele");

    public static final LogOperType ANIME_SPECIAL_ADD = new LogOperType("anime", "special.add");
    public static final LogOperType ANIME_SPECIAL_RECOVER = new LogOperType("anime", "special.recover");
    public static final LogOperType ANIME_SPECIAL_UPDATE = new LogOperType("anime", "special.update");
    public static final LogOperType ANIME_SPECIAL_DETETE = new LogOperType("anime", "special.detele");
    public static final LogOperType ANIME_SPECIAL_SORT = new LogOperType("anime", "special.sort");

    public static final LogOperType ANIME_SPECIAL_ITEM_ADD = new LogOperType("anime", "special.item.add");
    public static final LogOperType ANIME_SPECIAL_ITEM_RECOVER = new LogOperType("anime", "special.item.recover");
    public static final LogOperType ANIME_SPECIAL_ITEM_UPDATE = new LogOperType("anime", "special.item.update");
    public static final LogOperType ANIME_SPECIAL_ITEM_DETETE = new LogOperType("anime", "special.item.detele");
    public static final LogOperType ANIME_SPECIAL_ITEM_SORT = new LogOperType("anime", "special.item.sort");


    public static final LogOperType MODIFY_APP_ADD = new LogOperType("joymeapp", "app.add");
    public static final LogOperType MODIFY_APP_DELETE = new LogOperType("joymeapp", "app.delete");
    public static final LogOperType MODIFY_APP_MODIFYPAGE = new LogOperType("joymeapp", "app.modifypage");
    public static final LogOperType MODIFY_APP_MODIFY = new LogOperType("joymeapp", "app.modify");

    public static final LogOperType MODIFY_APP_CONFIG = new LogOperType("joymeapp", "app.config.modify");
    public static final LogOperType MODIFY_APP_CONFIG_PAGE = new LogOperType("joymeapp", "app.config.modifypage");

    public static final LogOperType ADVERTISE_APPLY_ADD = new LogOperType("advertise", "apply.add");
    public static final LogOperType ADVERTISE_APPLY_UPDATE = new LogOperType("advertise", "apply.update");

    public static final LogOperType ADVERTISE_MATERIAL_ADD = new LogOperType("advertise", "material.add");
    public static final LogOperType ADVERTISE_MATERIAL_UPDATE = new LogOperType("advertise", "material.update");


    public static final LogOperType WANBA_RED_MESSAHE = new LogOperType("wanba", "red.message.update");

    //wiki app
    public static final LogOperType WIKIAPP_INDEX_CLIENTLINE_MODIFY = new LogOperType("wikiapp", "wikiapp.index.clientline.modify");

    //玩霸迷友圈
    public static final LogOperType WANBA_MIYOU_DELETE = new LogOperType("wanba", "miyou.delete");
    public static final LogOperType WANBA_MIYOU_UPDATE = new LogOperType("wanba", "miyou.update");
    public static final LogOperType WANBA_MIYOU_SORT = new LogOperType("wanba", "miyou.sort");


    private LogOperType(String module, String oper) {
        this.module = module.toLowerCase();
        this.oper = oper.toLowerCase();

        map.put(this.module + "." + this.oper, this);
    }

    public String getCode() {
        return this.module + "." + this.oper;
    }

    public static LogOperType getByCode(String c) {
        if (Strings.isNullOrEmpty(c)) {
            return null;
        }

        return map.get(c.toLowerCase());
    }

    public static Collection<LogOperType> getAll() {
        return map.values();
    }

    public String getOper() {
        return oper;
    }

    public String getModule() {
        return module;
    }

    @Override
    public String toString() {
        return "LogOperType: module=" + this.module + ",oper" + this.oper;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LogOperType that = (LogOperType) o;

        if (!module.equals(that.module)) return false;
        if (!oper.equals(that.oper)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return module.hashCode() + oper.hashCode();
    }
}
