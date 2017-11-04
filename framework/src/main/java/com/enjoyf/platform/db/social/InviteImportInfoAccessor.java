/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.db.social;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.sequence.SequenceAccessor;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.social.InviteImportInfo;

import java.sql.Connection;
import java.util.Date;
import java.util.List;

/**
 * @author <a href=mailto:yinpengyi@fivewh.com>Yin Pengyi</a>
 */
interface InviteImportInfoAccessor extends SequenceAccessor{
    //insert
    public InviteImportInfo insert(InviteImportInfo inviteImportInfo, Connection conn) throws DbException;

    //
    public InviteImportInfo getInvite(String srcUno,String inviteEmail,Connection conn)throws DbException;

    //
    public boolean updateInviteStatus(String srcUno,String destUno,String inviteEmail,ActStatus status,Connection conn)throws DbException;

    //
    public List<InviteImportInfo> selectByDateField(String uno, ActStatus inviteStatus, Date startDate, Date endDate, Connection conn)throws DbException;
}
