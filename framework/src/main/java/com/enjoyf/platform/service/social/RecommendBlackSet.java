package com.enjoyf.platform.service.social;

import com.enjoyf.platform.service.JsonBinder;
import com.enjoyf.platform.util.log.GAlerter;
import com.google.common.base.Strings;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 12-8-29
 * Time: 下午2:01
 * To change this template use File | Settings | File Templates.
 */
public class RecommendBlackSet implements Serializable {

    private Set<RecommendBlack> recommendBlakSet = new HashSet<RecommendBlack>();

    public RecommendBlackSet(Set<RecommendBlack> recommendBlakSet) {
        this.recommendBlakSet = recommendBlakSet;
    }

    public RecommendBlackSet() {
    }

    public void add(RecommendBlack detail) {
        recommendBlakSet.add(detail);
    }

    public RecommendBlackSet add(Collection<RecommendBlack> details) {
        recommendBlakSet.addAll(details);
        return this;
    }

    public Set<RecommendBlack> getRecommendBlaskSet() {
        return recommendBlakSet;
    }

    public void setRecommendBlakSet(Set<RecommendBlack> recommendBlakSet) {
        this.recommendBlakSet = recommendBlakSet;
    }

    public String toJsonStr() {
        return JsonBinder.buildNormalBinder().toJson(recommendBlakSet);
    }

    public static RecommendBlackSet parse(String jsonStr) {
        RecommendBlackSet returnValue = null;

        if (!Strings.isNullOrEmpty(jsonStr)) {
            try {
                Set<RecommendBlack> recommendBlackSet = JsonBinder.buildNormalBinder().getMapper().readValue(jsonStr, new TypeReference<HashSet<RecommendBlack>>() {
                });

                returnValue = new RecommendBlackSet(recommendBlackSet);
            } catch (IOException e) {
                GAlerter.lab("RecommendBlackSet parse error, jsonStr:" + jsonStr, e);
            }
        }

        return returnValue;
    }
}
