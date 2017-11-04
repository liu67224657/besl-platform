package com.enjoyf.platform.service.gameres;

import com.enjoyf.platform.service.JsonBinder;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: yujiaxiu
 * Date: 12-10-23
 * Time: 下午3:42
 * To change this template use File | Settings | File Templates.
 */
public class GameMediaScoreSet implements Serializable{
    private List<GameMediaScore> mediaScores = new ArrayList<GameMediaScore>();

    public GameMediaScoreSet(){}

    public GameMediaScoreSet(List<GameMediaScore> mediaScores){
        this.mediaScores = mediaScores;
    }

    public GameMediaScoreSet(Collection mediaScores) {
        this.mediaScores.addAll(mediaScores);
    }

    public GameMediaScoreSet(String jsonStr) {
        if (!StringUtil.isEmpty(jsonStr)) {
            try {
                mediaScores = JsonBinder.buildNormalBinder().getMapper().readValue(jsonStr, new TypeReference<ArrayList<GameMediaScore>>() {
                });
            } catch (IOException e) {
                //
                GAlerter.lab("GameMediaScoreSet constructor error, jsonStr:" + jsonStr, e);
            }
        }
    }

    public List<GameMediaScore> getMediaScores() {
        return mediaScores;
    }

    public void add(List<GameMediaScore> scores){
        mediaScores.addAll(scores);
    }

    public String toJsonStr() {
        return JsonBinder.buildNormalBinder().toJson(mediaScores);
    }

    public static GameMediaScoreSet parse(String jsonStr) {
        GameMediaScoreSet returnValue = new GameMediaScoreSet();

        if (!StringUtil.isEmpty(jsonStr)) {
            try {
                List<GameMediaScore> scores = JsonBinder.buildNormalBinder().getMapper().readValue(jsonStr, new TypeReference<ArrayList<GameMediaScore>>() {
                });

                returnValue.add(scores);
            } catch (IOException e) {
                GAlerter.lab("GameMediaScoreSet parse error, jsonStr:" + jsonStr, e);
            }
        }

        return returnValue;
    }

    @Override
    public String toString() {
        return "GameMediaScoreSet{" +
                "mediaScores=" + mediaScores +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GameMediaScoreSet that = (GameMediaScoreSet) o;

        if (mediaScores != null ? !mediaScores.equals(that.mediaScores) : that.mediaScores != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return mediaScores != null ? mediaScores.hashCode() : 0;
    }
}
