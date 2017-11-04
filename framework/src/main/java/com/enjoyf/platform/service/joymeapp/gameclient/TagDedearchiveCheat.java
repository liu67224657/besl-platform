package com.enjoyf.platform.service.joymeapp.gameclient;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by zhimingli
 * Date: 2015/01/07
 * Time: 18:39
 */
public class TagDedearchiveCheat implements Serializable {
    private Long dede_archives_id;
    private Integer read_num;
    private Integer agree_num;
    private Double agree_percent = 0.00;
    private Date cheating_time;
    private Integer real_read_num;
    private Integer real_agree_num;
    private TagDedearchiveCheatType archive_type = TagDedearchiveCheatType.WANBA;


    public Long getDede_archives_id() {
        return dede_archives_id;
    }

    public void setDede_archives_id(Long dede_archives_id) {
        this.dede_archives_id = dede_archives_id;
    }

    public Integer getRead_num() {
        return read_num;
    }

    public void setRead_num(Integer read_num) {
        this.read_num = read_num;
    }

    public Double getAgree_percent() {
        return agree_percent;
    }

    public void setAgree_percent(Double agree_percent) {
        this.agree_percent = agree_percent;
    }

    public Date getCheating_time() {
        return cheating_time;
    }

    public void setCheating_time(Date cheating_time) {
        this.cheating_time = cheating_time;
    }

    public Integer getAgree_num() {
        return agree_num;
    }

    public void setAgree_num(Integer agree_num) {
        this.agree_num = agree_num;
    }

    public Integer getReal_read_num() {
        return real_read_num;
    }

    public void setReal_read_num(Integer real_read_num) {
        this.real_read_num = real_read_num;
    }

    public Integer getReal_agree_num() {
        return real_agree_num;
    }

    public void setReal_agree_num(Integer real_agree_num) {
        this.real_agree_num = real_agree_num;
    }

    public TagDedearchiveCheatType getArchive_type() {
        return archive_type;
    }

    public void setArchive_type(TagDedearchiveCheatType archive_type) {
        this.archive_type = archive_type;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
