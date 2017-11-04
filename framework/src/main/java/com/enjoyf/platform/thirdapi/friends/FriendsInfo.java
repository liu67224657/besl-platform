package com.enjoyf.platform.thirdapi.friends;


import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zhimingli
 * Date: 14-6-3
 * Time: 下午6:11
 * To change this template use File | Settings | File Templates.
 */
public class FriendsInfo implements Serializable {
    private List<FriendsMessage> users;
    private Integer next_curso;
    private Integer previous_cursor;
    private Integer total_number;

    public List<FriendsMessage> getUsers() {
        return users;
    }

    public void setUsers(List<FriendsMessage> users) {
        this.users = users;
    }

    public Integer getPrevious_cursor() {
        return previous_cursor;
    }

    public void setPrevious_cursor(Integer previous_cursor) {
        this.previous_cursor = previous_cursor;
    }

    public Integer getTotal_number() {
        return total_number;
    }

    public void setTotal_number(Integer total_number) {
        this.total_number = total_number;
    }

    public Integer getNext_curso() {
        return next_curso;
    }

    public void setNext_curso(Integer next_curso) {
        this.next_curso = next_curso;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
