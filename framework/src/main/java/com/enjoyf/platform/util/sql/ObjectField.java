package com.enjoyf.platform.util.sql;

import java.io.Serializable;

/**
 * @Auther: <a mailto:yinpengyi@platform.com>Yin Pengyi</a>
 */
public interface ObjectField extends Serializable {
    
    public String getColumn();

    public ObjectFieldDBType getType();

    public boolean isModify();

    public boolean isUniquene();
}
