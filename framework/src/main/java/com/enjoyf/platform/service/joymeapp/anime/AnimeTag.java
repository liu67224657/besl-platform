package com.enjoyf.platform.service.joymeapp.anime;

import com.enjoyf.platform.service.ValidStatus;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import java.io.Serializable;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: zhimingli
 * Date: 14-10-26
 * Time: 下午3:22
 * To change this template use File | Settings | File Templates.
 */
public class AnimeTag implements Serializable {
    private Long tag_id;
    private String tag_name;

    private Long parent_tag_id;

    private String tag_desc;


    private AnimeTagPicParam picjson;
    private String ch_name; //玩霸2.1.0的tag_name
    private String en_name;

    private String reserved;

    private AnimeTagType animeTagType;
    private AnimeTagModel animeTagModel;

    private String volume;//-1表示出现在玩霸2.1.0

    private String ch_desc;
    private String en_desc;
    private Long play_num;          //todo   玩霸2.2以后代表圈子帖子数
    private Long favorite_num;     //todo   玩霸2.2以后代表圈子评论数

    private ValidStatus remove_status = ValidStatus.INVALID;
    private Date update_date;
    private Date create_date;
    private String create_user;


    private Integer display_order;
    private AnimeTagSearchType animeTagSearchType;


    private AnimeTagAppType app_type = AnimeTagAppType.ANIME;

    private Integer total_sum;  //玩霸2.2里面表示 关注人数

    public Long getTag_id() {
        return tag_id;
    }

    public void setTag_id(Long tag_id) {
        this.tag_id = tag_id;
    }

    public String getTag_name() {
        return tag_name;
    }

    public void setTag_name(String tag_name) {
        this.tag_name = tag_name;
    }

    public String getTag_desc() {
        return tag_desc;
    }

    public void setTag_desc(String tag_desc) {
        this.tag_desc = tag_desc;
    }

    public AnimeTagPicParam getPicjson() {
        return picjson;
    }

    public void setPicjson(AnimeTagPicParam picjson) {
        this.picjson = picjson;
    }

    public String getCh_name() {
        return ch_name;
    }

    public void setCh_name(String ch_name) {
        this.ch_name = ch_name;
    }

    public String getEn_name() {
        return en_name;
    }

    public void setEn_name(String en_name) {
        this.en_name = en_name;
    }

    public AnimeTagType getAnimeTagType() {
        return animeTagType;
    }

    public void setAnimeTagType(AnimeTagType animeTagType) {
        this.animeTagType = animeTagType;
    }

    public AnimeTagModel getAnimeTagModel() {
        return animeTagModel;
    }

    public void setAnimeTagModel(AnimeTagModel animeTagModel) {
        this.animeTagModel = animeTagModel;
    }

    public String getCh_desc() {
        return ch_desc;
    }

    public void setCh_desc(String ch_desc) {
        this.ch_desc = ch_desc;
    }

    public String getEn_desc() {
        return en_desc;
    }

    public void setEn_desc(String en_desc) {
        this.en_desc = en_desc;
    }

    public Long getPlay_num() {
        return play_num;
    }

    public String getReserved() {
        return reserved;
    }

    public void setReserved(String reserved) {
        this.reserved = reserved;
    }

    public void setPlay_num(Long play_num) {
        this.play_num = play_num;
    }

    public Long getFavorite_num() {
        return favorite_num;
    }

    public void setFavorite_num(Long favorite_num) {
        this.favorite_num = favorite_num;
    }

    public ValidStatus getRemove_status() {
        return remove_status;
    }

    public void setRemove_status(ValidStatus remove_status) {
        this.remove_status = remove_status;
    }

    public Date getUpdate_date() {
        return update_date;
    }

    public void setUpdate_date(Date update_date) {
        this.update_date = update_date;
    }

    public Date getCreate_date() {
        return create_date;
    }

    public void setCreate_date(Date create_date) {
        this.create_date = create_date;
    }

    public String getCreate_user() {
        return create_user;
    }

    public void setCreate_user(String create_user) {
        this.create_user = create_user;
    }

    public Long getParent_tag_id() {
        return parent_tag_id;
    }

    public void setParent_tag_id(Long parent_tag_id) {
        this.parent_tag_id = parent_tag_id;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public Integer getDisplay_order() {
        return display_order;
    }

    public void setDisplay_order(Integer display_order) {
        this.display_order = display_order;
    }

    public AnimeTagSearchType getAnimeTagSearchType() {
        return animeTagSearchType;
    }

    public void setAnimeTagSearchType(AnimeTagSearchType animeTagSearchType) {
        this.animeTagSearchType = animeTagSearchType;
    }

    public AnimeTagAppType getApp_type() {
        return app_type;
    }

    public void setApp_type(AnimeTagAppType app_type) {
        this.app_type = app_type;
    }

    public Integer getTotal_sum() {
        return total_sum;
    }

    public void setTotal_sum(Integer total_sum) {
        this.total_sum = total_sum;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
