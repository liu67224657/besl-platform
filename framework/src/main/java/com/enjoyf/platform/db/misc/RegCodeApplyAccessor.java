/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.db.misc;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.misc.RegCodeApply;

import java.sql.Connection;

/**
 * @author <a href=mailto:yinpengyi@fivewh.com>Yin Pengyi</a>
 */
interface RegCodeApplyAccessor {
    //insert
    public RegCodeApply insert(RegCodeApply apply, Connection conn) throws DbException;

    //get by email.
    public RegCodeApply getByEmail(String email, Connection conn) throws DbException;

    //update.
    public boolean update(String email, RegCodeApply apply, Connection conn) throws DbException;

    //
    public boolean updateRegCode(String email, String regCode, Connection conn) throws DbException;

}
