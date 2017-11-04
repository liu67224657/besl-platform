package com.enjoyf.platform.service.gameres.gamedb;

import com.enjoyf.platform.service.JsonBinder;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  13-11-21 下午2:26
 * Description:
 */
public class GameCategoryType implements Serializable {
    private static Map<Integer, GameCategoryType> map = new HashMap<Integer, GameCategoryType>();

    public static final GameCategoryType RPG = new GameCategoryType(1, "RPG");
    public static final GameCategoryType XIUXIAN = new GameCategoryType(5, "休闲");
//    public static final GameCategoryType YIZHI = new GameCategoryType(4, "益智");//归入休闲
//    public static final GameCategoryType YANGCHENG = new GameCategoryType(21, "养成");//归入休闲
//    public static final GameCategoryType SANXIAO = new GameCategoryType(33, "三消");//归入休闲
    public static final GameCategoryType DONGZUO = new GameCategoryType(7, "动作");
    public static final GameCategoryType MAOXIAN = new GameCategoryType(16, "冒险");//归入动作
//    public static final GameCategoryType KONGBU = new GameCategoryType(23, "恐怖");//归入动作
    public static final GameCategoryType JINGSU = new GameCategoryType(18, "竞速");
//    public static final GameCategoryType SAICHE = new GameCategoryType(15, "赛车");//归入竞速
//    public static final GameCategoryType PAOKU = new GameCategoryType(19, "跑酷");//归入竞速
    public static final GameCategoryType TIYU = new GameCategoryType(9, "体育");
    public static final GameCategoryType QIPAI = new GameCategoryType(6, "棋牌");
    public static final GameCategoryType MONI = new GameCategoryType(3, "模拟");
//    public static final GameCategoryType JINGYING = new GameCategoryType(11, "经营");//归入模拟
    public static final GameCategoryType CELUE = new GameCategoryType(2, "策略");
    public static final GameCategoryType KAPAI = new GameCategoryType(12, "卡牌");//归入策略
    public static final GameCategoryType JIEMI = new GameCategoryType(17, "解谜");//归入策略
//    public static final GameCategoryType TAFANG = new GameCategoryType(20, "塔防");//归入策略
//    public static final GameCategoryType TUOCHU = new GameCategoryType(22, "脱出");//归入策略
    public static final GameCategoryType GEDOU = new GameCategoryType(8, "格斗");
    public static final GameCategoryType SHEJI = new GameCategoryType(14, "射击");
//    public static final GameCategoryType DANMU = new GameCategoryType(24, "弹幕");//归入射击
    public static final GameCategoryType YINYUE = new GameCategoryType(25, "音乐");
//    public static final GameCategoryType MOBA = new GameCategoryType(34, "MOBA");
    public static final GameCategoryType JINGJI = new GameCategoryType(35, "竞技");

    private int code;
    private String value;

    public GameCategoryType(int code, String value) {
        this.code = code;
        this.value = value;
        map.put(code, this);
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return JsonBinder.buildNormalBinder().toJson(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || this.getClass() != o.getClass())
            return false;
        if (code != ((GameCategoryType) o).code)
            return false;
        return true;
    }

    public static GameCategoryType getByCode(int c) {
        return map.get(c);
    }

    public static Collection<GameCategoryType> getAll() {
        return map.values();
    }
}
