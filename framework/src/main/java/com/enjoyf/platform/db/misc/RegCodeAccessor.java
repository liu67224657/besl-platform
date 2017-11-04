/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.db.misc;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.misc.RegCode;

import java.sql.Connection;

/**
 * @author <a href=mailto:yinpengyi@fivewh.com>Yin Pengyi</a>
 */
interface RegCodeAccessor {
    //get a regcode.
    public RegCode get(String regCode, Connection conn) throws DbException;

    //update use info
    public boolean updateUseInfo(String regCode, String useUno, String useUserid, Connection conn) throws DbException;

    //
    public boolean updateApplyInfo(String regCode, String applyEmail, String applyReason, Connection conn) throws DbException;

}
