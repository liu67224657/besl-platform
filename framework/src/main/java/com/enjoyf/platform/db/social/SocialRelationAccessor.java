/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.db.social;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.sequence.SequenceAccessor;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.social.RelationType;
import com.enjoyf.platform.service.social.SocialRelation;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;

import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author <a href=mailto:yinpengyi@fivewh.com>Yin Pengyi</a>
 */
interface SocialRelationAccessor {
    //insert
    public SocialRelation insert(SocialRelation relation, Connection conn) throws DbException;

    //get all src relations
    public List<SocialRelation> queryAllSrcRelations(String srcUno, RelationType type, ActStatus status, Connection conn) throws DbException;

    //query by type, scr uno and statuses
    public List<SocialRelation> querySrcRelations(String srcUno, RelationType type, ActStatus status, Pagination page, Connection conn) throws DbException;

    //query by type, scr uno and statuses
    public List<SocialRelation> querySrcCategoryRelations(String srcUno, RelationType type, ActStatus status, Long cateId, Pagination page, Connection conn) throws DbException;

    //check by type and destunos
    public Map<String, SocialRelation> queryRelationsByDestUnos(String srcUno, RelationType type, Set<String> destUnos, Connection conn) throws DbException;

    //query by type, scr uno and statuses
    public List<SocialRelation> queryDestRelations(String srcUno, RelationType type, ActStatus status, Pagination page, Connection conn) throws DbException;

    // get the relation.
    public SocialRelation get(String srcUno, String destUno, RelationType type, Connection conn) throws DbException;

    //update description
    public boolean updateSrcDescription(String srcUno, String destUno, RelationType type, String description, Connection conn) throws DbException;

    //break relation:
    //关注
    public boolean updateSrcStatus(String srcUno, String destUno, RelationType type, ActStatus status, Connection conn) throws DbException;

    //粉丝
    public boolean updateDestStatus(String srcUno, String destUno, RelationType type, ActStatus status, Connection conn) throws DbException;

    //remove the relation.
    public boolean remove(String srcUno, String destUno, RelationType type, Connection conn) throws DbException;


    public List<SocialRelation> query(String srcUno,RelationType type,QueryExpress queryExpress, Connection conn) throws DbException;

    public List<SocialRelation> querySocialRelationByRelationTypeAndTable_NO(RelationType relationType, int tablNo, Connection conn)throws DbException;

}
