package com.enjoyf.platform.service.tools;

import com.enjoyf.platform.service.content.ContentReply;
import freemarker.ext.rhino.RhinoScriptableModel;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: yujiaxiu
 * Date: 11-12-18
 * Time: 下午2:52
 * To change this template use File | Settings | File Templates.
 */
public class ContentReplyAuditStatus implements Serializable {
    //标记：文本回复审核过与否
    public static final Integer AUDIT_REPLY = 1;
    //标记：审核过的文本回复通过与否
    public static final Integer ILLEGAL_REPLY = 2;

    private Integer value;
    private Integer result;

    public ContentReplyAuditStatus(){

    }
//    public ContentReplyAuditStatus(Integer v){
//        super(v);
//    }
    public ContentReplyAuditStatus(Integer value, Integer result) {
        this.value = value;
        this.result = result;
    }
    public ContentReplyAuditStatus(Integer value) {
        this.value = value;
    }

    public boolean hasAuditReply(){
        return (value & AUDIT_REPLY) > 0;
    }

    public boolean isReplyPass() {
        return (value & ILLEGAL_REPLY) <= 0;
    }

    public Integer getValue() {
        return value;
    }

    public Integer getResult() {
        return result;
    }

    @Override
    public int hashCode() {
        return value;
    }

    @Override
    public String toString() {
        return "ContentReplyAuditStatus value=" + value + ":result=" + result;
    }
    public static ContentReplyAuditStatus getByValue(Integer v){
        return new ContentReplyAuditStatus(v);
    }
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj instanceof ContentReplyAuditStatus) {
            return value.equals(((ContentReplyAuditStatus) obj).getValue()) && result.equals(((ContentReplyAuditStatus) obj).getResult());
        } else {
            return false;
        }
    }
}
