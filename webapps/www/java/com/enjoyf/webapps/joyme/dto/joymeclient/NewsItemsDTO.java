package com.enjoyf.webapps.joyme.dto.joymeclient;

import com.enjoyf.platform.service.joymeapp.ParamTextJson;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-9-17
 * Time: 下午3:06
 * To change this template use File | Settings | File Templates.
 */
public class NewsItemsDTO {
    private String title;
    private String author;
    private String pic;
    private String url;
    private String desc;
    private int order;
    private int menu_type;
    private long date;
    private String category_color;
    private String category;
    private int d_type;//显示类型
    private ParamTextJson param;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }


    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getCategory_color() {
        return category_color;
    }

    public void setCategory_color(String category_color) {
        this.category_color = category_color;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getMenu_type() {
        return menu_type;
    }

    public void setMenu_type(int menu_type) {
        this.menu_type = menu_type;
    }

    public int getD_type() {
        return d_type;
    }

    public void setD_type(int d_type) {
        this.d_type = d_type;
    }

    public ParamTextJson getParam() {
        return param;
    }

    public void setParam(ParamTextJson param) {
        this.param = param;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
