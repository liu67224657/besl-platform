package com.enjoyf.platform.db.tools;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.sequence.SequenceAccessor;
import com.enjoyf.platform.service.tools.ContentType;
import com.enjoyf.platform.service.tools.AuditContent;
import com.enjoyf.platform.service.tools.AuditUser;
import com.enjoyf.platform.util.sql.ObjectField;

import java.sql.Connection;
import java.util.Map;

/**
 * Author: zhaoxin
 * Date: 11-10-28
 * Time: 下午2:58
 * Desc:
 */
interface ToolsAuditContentAccessor extends SequenceAccessor {
    //
    public AuditContent insertAuditContent(AuditContent auditContent, Connection conn) throws DbException;

    //
    public boolean updateAuditContent(Long auditId,String uno,Map<ObjectField, Object> map, Connection conn) throws DbException;


    //
    public AuditContent findAuditContentById(Long aid,String cid,String uno,  Connection conn) throws DbException;

    //
    public AuditContent findAuditContentByCIdCT(String cid, ContentType contentType,String uno ,Connection conn) throws DbException;


}
