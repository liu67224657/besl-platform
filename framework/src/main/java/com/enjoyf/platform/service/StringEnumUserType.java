/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.service;

import java.sql.Types;

public class StringEnumUserType extends EnumUserType {
    private static final int[] SQL_TYPES = new int[]{Types.VARCHAR};

    protected Class primitiveType() {
        return String.class;
    }

    public int[] sqlTypes() {
        return SQL_TYPES;
    }
}
