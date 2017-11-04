package com.enjoyf.platform.service.gameres;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-8-20
 * Time: 下午4:49
 * To change this template use File | Settings | File Templates.
 */
public class GamePropertyKeyNameCode implements Serializable{
    private static Map<String, GamePropertyKeyNameCode> map = new HashMap<String, GamePropertyKeyNameCode>();
    public static final GamePropertyKeyNameCode ID = new GamePropertyKeyNameCode("id");
    public static final GamePropertyKeyNameCode NAME = new GamePropertyKeyNameCode("name");
    public static final GamePropertyKeyNameCode ICON = new GamePropertyKeyNameCode("icon");
    public static final GamePropertyKeyNameCode DEVELOPER = new GamePropertyKeyNameCode("developer");
    public static final GamePropertyKeyNameCode URL = new GamePropertyKeyNameCode("url");
    public static final GamePropertyKeyNameCode PLATFORM = new GamePropertyKeyNameCode("platform");
    public static final GamePropertyKeyNameCode CHANNEL = new GamePropertyKeyNameCode("channel");

    private String value;

    public GamePropertyKeyNameCode(){

    }

    public GamePropertyKeyNameCode(String v) {
        this.value = v;
        map.put(v, this);
    }

    public String getValue(){
        return value;
    }

    public static GamePropertyKeyNameCode getByValue(String v){
        return new GamePropertyKeyNameCode(v);
    }

    public static Collection<GamePropertyKeyNameCode> getAll(){
        return map.values();
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if(this == o)
            return true;
        if(o == null || this.getClass() != o.getClass())
            return false;
        if(value != ((GamePropertyKeyNameCode)o).value)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "GamePropertyKeyNameCode: value"+value;
    }
}
