package com.enjoyf.platform.util.sql;

import java.io.Serializable;

@SuppressWarnings("serial")
public class UpdateType implements Serializable {
    //
    public static UpdateType SET = new UpdateType("set");
    public static UpdateType INCREASE = new UpdateType("increase");

    private String code;

    //
    private UpdateType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public String toString() {
        return "UpdateType code:" + code;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj instanceof UpdateType) {
            return code.equals(((UpdateType) obj).getCode());
        } else {
            return false;
        }
    }

    public int hashCode() {
        return code.hashCode();
    }
}
