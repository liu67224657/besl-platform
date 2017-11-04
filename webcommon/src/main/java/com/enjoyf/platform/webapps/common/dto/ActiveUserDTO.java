/**
 * CopyRight 2012 joyme.com
 */
package com.enjoyf.platform.webapps.common.dto;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Author: zhaoxin
 * Date: 12-3-13
 * Time: 下午1:36
 * Desc:
 */
public class ActiveUserDTO implements Serializable {


    private String uno;
    private int num;

    public ActiveUserDTO() {
    }

    public ActiveUserDTO(String uno, int num) {
        this.uno = uno;
        this.num = num;
    }

    public String getUno() {
        return uno;
    }

    public void setUno(String uno) {
        this.uno = uno;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    @Override
    public int hashCode() {
        return uno != null ? uno.hashCode() : 0;
    }


    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }

}

