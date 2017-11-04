package com.enjoyf.webapps.joyme.dto.vote;

import com.enjoyf.platform.service.JsonBinder;
import com.enjoyf.platform.service.profile.ProfileBlogHeadIcon;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.reflect.ReflectUtil;
import com.google.common.base.Strings;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-10-22
 * Time: 上午11:24
 * To change this template use File | Settings | File Templates.
 */
public class JSONWikiVoteSet {

    private Set<JSONWikiVote> voteSet = new LinkedHashSet<JSONWikiVote>();

    public JSONWikiVoteSet() {
    }

    public JSONWikiVoteSet(Set<JSONWikiVote> voteSet) {
        this.voteSet = voteSet;
    }

    public String toJsonStr() {
        return JsonBinder.buildNormalBinder().toJson(voteSet);
    }

    public static JSONWikiVoteSet parse(String jsonStr) {
        JSONWikiVoteSet returnValue = new JSONWikiVoteSet();

        if (!Strings.isNullOrEmpty(jsonStr)) {
            try {
                Set<JSONWikiVote> votes = JsonBinder.buildNormalBinder().getMapper().readValue(jsonStr, new TypeReference<LinkedHashSet<JSONWikiVote>>() {
                });

                returnValue.addAll(votes);
            } catch (IOException e) {
                GAlerter.lab("ProfileBlogHeadIconSet parse error, jsonStr:" + jsonStr, e);
            }
        }

        return returnValue;
    }

    public void addAll(Set<JSONWikiVote> votes) {
        this.voteSet.addAll(votes);
    }

    public Set<JSONWikiVote> getVoteSet() {
        return voteSet;
    }

    public void setVoteSet(Set<JSONWikiVote> voteSet) {
        this.voteSet = voteSet;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }

    @Override
    public int hashCode() {
        return voteSet.hashCode();
    }
}
