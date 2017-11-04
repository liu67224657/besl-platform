package com.enjoyf.platform.service.gameres;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * <p/>
 * Description:游戏关系类型
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>ericliu</a>
 */
public class GameRelationType implements Serializable {
    private static Map<String, GameRelationType> map = new HashMap<String, GameRelationType>();
    private String code;

    public static final GameRelationType GAME_RELATION_TYPE_BOARD = new GameRelationType("board");// group

    public static final GameRelationType GAME_RELATION_TYPE_LINK = new GameRelationType("link");//连接
    public static final GameRelationType GAME_RELATION_TYPE_COVER = new GameRelationType("cover");//封面 todo not use
    public static final GameRelationType GAME_RELATION_TYPE_INVITE = new GameRelationType("invite");//邀请

    public static final GameRelationType GAME_RELATION_TYPE_RELATED_GAME = new GameRelationType("relatedgame");//相关游戏
    public static final GameRelationType GAME_RELATION_TYPE_RELATED_GROUP = new GameRelationType("relatedgroup");//相关小组
    public static final GameRelationType GAME_RELATION_TYPE_BAIKE = new GameRelationType("baike");//百科
    public static final GameRelationType GAME_RELATION_TYPE_ARTICLE = new GameRelationType("article");//文章
    public static final GameRelationType GAME_RELATION_TYPE_DOWNLOAD = new GameRelationType("download");//相关下载
    public static final GameRelationType GAME_RELATION_TYPE_GROUPCONTENT = new GameRelationType("groupcontent");//相关内容
    public static final GameRelationType GAME_RELATION_TYPE_TALENT = new GameRelationType("talent");//相关游戏
    public static final GameRelationType GAME_RELATION_TYPE_MENU = new GameRelationType("menu");//菜单
    public static final GameRelationType GAME_RELATION_TYPE_CONTRIBUTE = new GameRelationType("contribute");//贡献者
    public static final GameRelationType GAME_RELATION_TYPE_HEADIMAGE = new GameRelationType("headimage");//头土

    public GameRelationType(String c) {
        this.code = c.toLowerCase();
        map.put(code, this);
    }

    public String getCode() {
        return code;
    }

    public static GameRelationType getByCode(String code) {
        return map.get(code.toLowerCase());
    }

    public static Collection<GameRelationType> getAll() {
        return map.values();
    }

    @Override
    public int hashCode() {
        return code.hashCode();
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }

    @Override
    public boolean equals(Object obj) {
        if ((obj == null) || !(obj instanceof GameRelationType)) {
            return false;
        }

        return code.equalsIgnoreCase(((GameRelationType) obj).getCode());
    }
}
