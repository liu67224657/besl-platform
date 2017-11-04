package com.enjoyf.platform.thirdapi.friends;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: zhimingli
 * Date: 14-6-3
 * Time: 下午9:16
 * To change this template use File | Settings | File Templates.
 */
public class AtUser implements Serializable {
    private Long uid;
    private String nickname;
    private String remark;

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
