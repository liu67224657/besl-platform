package com.enjoyf.platform.service.content;

import com.enjoyf.platform.service.JsonBinder;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.reflect.ReflectUtil;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-2-19
 * Time: 下午5:22
 * To change this template use File | Settings | File Templates.
 */
public class GameContentSet implements Serializable {
    //
    private Set<GameContent> games = new LinkedHashSet<GameContent>();

    private Boolean validStatus = true; //默认可以显示


    public GameContentSet() {
    }

    public GameContentSet(Collection<GameContent> games) {
        if (!CollectionUtil.isEmpty(games)) {
            this.games.addAll(games);
        }
    }


    public GameContentSet(String jsonStr) {
        if (!StringUtil.isEmpty(jsonStr)) {
            try {
                games = JsonBinder.buildNormalBinder().getMapper().readValue(jsonStr, new TypeReference<LinkedHashSet<GameContentSet>>() {
                });
            } catch (IOException e) {
                GAlerter.lab("ResourceImageSet constructor error, jsonStr:" + jsonStr, e);
            }
        }
    }

    public Set<GameContent> getGames() {
        return games;
    }

    public void add(GameContent gameContent) {
        games.add(gameContent);
    }

    public void add(Set<GameContent> gameContents) {
        this.games.addAll(gameContents);
    }

    public String toJsonStr() {
        return JsonBinder.buildNormalBinder().toJson(games);
    }

    public static GameContentSet parse(String jsonStr) {
        GameContentSet returnValue = new GameContentSet();

        if (!StringUtil.isEmpty(jsonStr)) {
            try {
                Set<GameContent> GameContentSets = JsonBinder.buildNormalBinder().getMapper().readValue(jsonStr, new TypeReference<LinkedHashSet<GameContent>>() {
                });

                returnValue.add(GameContentSets);
            } catch (IOException e) {
                GAlerter.lab("ResourceImageSet parse error, jsonStr:" + jsonStr, e);
            }
        }

        return returnValue;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }

    @Override
    public int hashCode() {
        return games.hashCode();
    }

    public Boolean getValidStatus() {
        return validStatus;
    }

    public void setValidStatus(Boolean validStatus) {
        this.validStatus = validStatus;
    }
}
