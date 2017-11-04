package com.enjoyf.platform.service.vote;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: yongmingxu
 * Date: 12-9-19
 * Time: 上午10:20
 * Description: 投票选项
 */
public class VoteOption implements Serializable {
    private long optionId;

    private String description;
    private VoteOptionType voteOptionType = VoteOptionType.SELECT;

    private String subjectID;

    private int optionNum;

    private boolean checked = false;

    public long getOptionId() {
        return optionId;
    }

    public void setOptionId(long optionId) {
        this.optionId = optionId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public VoteOptionType getVoteOptionType() {
        return voteOptionType;
    }

    public void setVoteOptionType(VoteOptionType voteOptionType) {
        this.voteOptionType = voteOptionType;
    }

    public String getSubjectID() {
        return subjectID;
    }

    public void setSubjectID(String subjectID) {
        this.subjectID = subjectID;
    }

    public int getOptionNum() {
        return optionNum;
    }

    public void setOptionNum(int optionNum) {
        this.optionNum = optionNum;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }

}
