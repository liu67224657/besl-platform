package com.enjoyf.webapps.joyme.dto.joymeapp.ask;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;

/**
 * Created by zhimingli on 2016/9/12 0012.
 */
public class QuestionConfigDTO implements Serializable {
    private String time;//限制时间
    private String score;//时间对应的积分
    private String timestr;//显示的时间

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }


    public String getTimestr() {
        return timestr;
    }

    public void setTimestr(String timestr) {
        this.timestr = timestr;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
