package com.enjoyf.platform.service.social;

import com.enjoyf.platform.service.JsonBinder;
import com.enjoyf.platform.service.content.ImageContent;
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
public class RecommendDetailSet implements Serializable{

    private List<RecommendDetail> recommendDetails = new ArrayList<RecommendDetail>();

    public RecommendDetailSet(List<RecommendDetail> recommendDetails) {
        this.recommendDetails = recommendDetails;
    }

    public RecommendDetailSet() {
    }

    public void add(RecommendDetail detail) {
        recommendDetails.add(detail);
    }

    public void add(Collection<RecommendDetail> details) {
        recommendDetails.addAll(details);
    }

    public List<RecommendDetail> getRecommendDetails() {
        return recommendDetails;
    }

    public void setRecommendDetails(List<RecommendDetail> recommendDetails) {
        this.recommendDetails = recommendDetails;
    }

    public String toJsonStr() {
        return JsonBinder.buildNormalBinder().toJson(recommendDetails);
    }

    public static RecommendDetailSet parse(String jsonStr) {
        RecommendDetailSet returnValue = null;

        if (!Strings.isNullOrEmpty(jsonStr)) {
            try {
                List<RecommendDetail> recommendDetails = JsonBinder.buildNormalBinder().getMapper().readValue(jsonStr, new TypeReference<ArrayList<RecommendDetail>>() {
                });

                returnValue=new RecommendDetailSet(recommendDetails);
            } catch (IOException e) {
                GAlerter.lab("RecommendDetailSet parse error, jsonStr:" + jsonStr, e);
            }
        }

        return returnValue;
    }
}
