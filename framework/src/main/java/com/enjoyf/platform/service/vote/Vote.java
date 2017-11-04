package com.enjoyf.platform.service.vote;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: yongmingxu
 * Date: 12-9-19
 * Time: 上午10:22
 * Description: 投票主题与选项

 */
public class Vote implements Serializable {
    private VoteSubject voteSubject;
    private Map<Long, VoteOption> voteOptionMap = new LinkedHashMap<Long, VoteOption>();
    private boolean expired;
    private int voteNum;//投票总数

    public VoteSubject getVoteSubject() {
        return voteSubject;
    }

    public void setVoteSubject(VoteSubject voteSubject) {
        this.voteSubject = voteSubject;
    }

    public Map<Long, VoteOption> getVoteOptionMap() {
        return voteOptionMap;
    }

    public void setVoteOptionMap(Map<Long, VoteOption> voteOptionMap) {
        this.voteOptionMap = voteOptionMap;
    }

    public boolean isExpired() {
        return expired;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }

    public int getVoteNum() {
        return voteNum;
    }

    public void setVoteNum(int voteNum) {
        this.voteNum = voteNum;
    }
}
