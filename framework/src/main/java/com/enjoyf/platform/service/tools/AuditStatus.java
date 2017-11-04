package com.enjoyf.platform.service.tools;

import java.io.Serializable;

/**
 * Author: zhaoxin
 * Date: 11-10-27
 * Time: 下午6:09
 * Desc: 二进制 1111111111(1023) 十位判断位  按位与进行判断
 */
public class AuditStatus implements Serializable {
    //审核过文本
    public static final Integer AUDIT_STATUS_TEXT = 1;
    //审核过图片
    public static final Integer AUDIT_STATUS_IMG  = 2;
    //审核过视频
    public static final Integer AUDIT_STATUS_VIDEO =4;
    //审核过音乐
    public static final Integer AUDIT_STATUS_AUDIO =8;
    //审核过人
    public static final Integer AUDIT_STATUS_PROFILE = 16;

    private Integer value = 0;

    public AuditStatus() {
    }

    public AuditStatus(Integer v){
        this.value = v;
    }

    //是否审核过文字
    public boolean hasAuditText(){
        return  (value & AUDIT_STATUS_TEXT) > 0;
    }

     //是否审核过图片
    public boolean hasAuditImg(){
        return  (value & AUDIT_STATUS_IMG) > 0;
    }

     //是否审核过视频
    public boolean hasAuditVideo(){
        return  (value & AUDIT_STATUS_VIDEO) > 0;
    }

     //是否审核过音乐
    public boolean hasAuditAudio(){
        return  (value & AUDIT_STATUS_AUDIO) > 0;
    }

      //是否审核过音乐
    public boolean hasProfile(){
        return  (value & AUDIT_STATUS_PROFILE) > 0;
    }
    public boolean hasAudit(Integer auditContent){
        return (value & auditContent) > 0;
    }


    public AuditStatus has(Integer v){
        this.value += v;
        return this;
    }


    public Integer getValue() {
        return value;
    }

    @Override
    public int hashCode() {
        return value;
    }

    @Override
    public String toString() {
        return "AuditStatus value=" + value;
    }

    public static AuditStatus getByValue(Integer v){
        return new AuditStatus(v);
    }
}
