package com.enjoyf.webapps.joyme.weblogic.comment;

import com.enjoyf.platform.service.comment.CommentBean;
import com.enjoyf.platform.webapps.common.ResultCodeConstants;

class ResultBean {
    private CommentBean bean;
    private ResultCodeConstants result;

    public ResultBean(ResultCodeConstants result, CommentBean bean) {
        this.bean = bean;
        this.result = result;
    }

    public CommentBean getBean() {
        return bean;
    }

    public ResultCodeConstants getResult() {
        return result;
    }
}