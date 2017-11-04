/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.content;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>  ,zx
 * Create time: 11-8-17 下午1:23
 * Description:
 */
public class ActivityCategoryType implements Serializable {
    private static Map<Integer, ActivityCategoryType> map = new HashMap<Integer, ActivityCategoryType>();

    public static final int KAPAI = 1; //卡牌

    public static final int RPG = 2;    //RPG

    public static final int CASUAL = 4; //休闲

    public static final int PUZZLE = 8; //益智

    public static final int MOTION = 16; //动作

    public static final int SHOOT = 32; //射击

    public static final int TACTICS = 64; //策略

    public static final int ROLE = 128; //角色扮演

    public static final int OPERATE = 256; //模拟经营

    public static final int PHYSICAL = 512; //体育

    public static final int SPEED = 1024; //竞速

    public static final int RELAX = 2048; //休闲

    public static final int MUSIC = 4096; //音乐

    public static final int ADOPT = 8192; //养成

    public static final int CHESS = 16384; //棋牌

    public static final int TRIPLE = 32768; //三消

    public static final int DEFENCE = 65536; //塔防

    public static final int RIDDLE = 131072; //解谜

    public static final int LOVE = 262144; //恋爱


    private int value = 0;

    //
    public ActivityCategoryType() {
    }

    private ActivityCategoryType(int v) {
        this.value = v;

        map.put(v, this);
    }

    public ActivityCategoryType has(int v) {
        value += v;

        return this;
    }

    public boolean hasKaiPai() {
        return (value & KAPAI) > 0;
    }

    public boolean hasRpg() {
        return (value & RPG) > 0;
    }


    public boolean hasCasual() {
        return (value & CASUAL) > 0;
    }


    public boolean hasPuzzle() {
        return (value & PUZZLE) > 0;
    }

    public boolean hasMotion(){
        return (value & MOTION) > 0;
    }

    public boolean hasShoot(){
        return (value & SHOOT) > 0;
    }

    public boolean hasTactics(){
        return (value & TACTICS) > 0;
    }

    public boolean hasRole(){
        return (value & ROLE) > 0;
    }

    public boolean hasOperate(){
        return (value & OPERATE) > 0;
    }

    public boolean hasPhysical(){
        return (value & PHYSICAL) > 0;
    }

    public boolean hasSpeed(){
        return (value & SPEED) > 0;
    }

    public boolean hasRelax(){
        return (value & RELAX) > 0;
    }

    public boolean hasMusic(){
        return (value & MUSIC) > 0;
    }

    public boolean hasAdopt(){
        return (value & ADOPT) > 0;
    }

    public boolean hasChess(){
        return (value & CHESS) > 0;
    }

    public boolean hasTriple(){
        return (value & TRIPLE) > 0;
    }

    public boolean hasDefence(){
        return (value & DEFENCE) > 0;
    }

    public boolean hasRiddle(){
        return (value & RIDDLE) > 0;
    }

    public boolean hasLove(){
        return (value & LOVE) > 0;
    }

    public int getValue() {
        return value;
    }


    @Override
    public int hashCode() {
        return value;
    }

    @Override
    public String toString() {
        return "ContentType: value=" + value;
    }

    public static ActivityCategoryType getByValue(Integer v) {
        return new ActivityCategoryType(v);
    }

    public Collection<ActivityCategoryType> getAll() {
        return map.values();
    }


}
