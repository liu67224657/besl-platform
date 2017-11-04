package com.enjoyf.platform.service.ask.wiki;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by zhimingli on 2017-3-28 0028.
 */
public class ContentSum implements Serializable {
    private Long id;
    private Integer agree_num;//点赞数
    private Date createDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAgree_num() {
        return agree_num;
    }

    public void setAgree_num(Integer agree_num) {
        this.agree_num = agree_num;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
