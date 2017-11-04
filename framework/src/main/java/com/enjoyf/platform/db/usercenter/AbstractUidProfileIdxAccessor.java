package com.enjoyf.platform.db.usercenter;

import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.usercenter.UidProfileIdx;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.rmi.server.UID;
import java.sql.*;
import java.util.List;
import java.util.UUID;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 14/11/19
 * Description:
 */
public abstract class AbstractUidProfileIdxAccessor extends AbstractBaseTableAccessor<UidProfileIdx> implements UidProfileIdxAccessor{

    private static Logger logger= LoggerFactory.getLogger(AbstractUidProfileIdxAccessor.class);

    private static final String KEY_TABLE_NAME="uid_profile_idx";


    @Override
    public int bachInsert(List<UidProfileIdx> idxList, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        try {
            //uid,uno,createtime,status,profileid,profilekey
            pstmt=conn.prepareStatement(getInsertSql());

            for(UidProfileIdx uidProfileIdx:idxList){
                pstmt.setLong(1, uidProfileIdx.getUid());
                pstmt.setString(2, uidProfileIdx.getUno());
                pstmt.setTimestamp(3, new Timestamp(uidProfileIdx.getCreateTime() == null ? System.currentTimeMillis() : uidProfileIdx.getCreateTime().getTime()));
                pstmt.setString(4, uidProfileIdx.getStatus().getCode());
                pstmt.setString(5, uidProfileIdx.getProfileId());
                pstmt.setString(6, uidProfileIdx.getProfileKey());
                pstmt.addBatch();
            }

            return pstmt.executeBatch().length;
        } catch (SQLException e) {
            logger.error("On insert token, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }
    }

    @Override
    public UidProfileIdx insert(UidProfileIdx uidProfileIdx, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        try {
            //uid,uno,createtime,status,profileid,profilekey
            pstmt=conn.prepareStatement(getInsertSql());
            pstmt.setLong(1, uidProfileIdx.getUid());
            pstmt.setString(2, uidProfileIdx.getUno());
            pstmt.setTimestamp(3, new Timestamp(uidProfileIdx.getCreateTime() == null ? System.currentTimeMillis() : uidProfileIdx.getCreateTime().getTime()));
            pstmt.setString(4, uidProfileIdx.getStatus().getCode());
            pstmt.setString(5, uidProfileIdx.getProfileId());
            pstmt.setString(6, uidProfileIdx.getProfileKey());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            logger.error("On insert token, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }

        return uidProfileIdx;
    }

    @Override
    protected UidProfileIdx rsToObject(ResultSet rs) throws SQLException {
        UidProfileIdx idx= new UidProfileIdx();

        idx.setUid(rs.getLong("uid"));
        idx.setUno(rs.getString("uno"));
        idx.setCreateTime(rs.getTimestamp("createtime"));
        idx.setProfileId(rs.getString("profileid"));
        idx.setProfileKey(rs.getString("profilekey"));
        idx.setStatus(ActStatus.getByCode(rs.getString("status")));

        return idx;
    }

    @Override
    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException {
        return super.update(KEY_TABLE_NAME,updateExpress,queryExpress,conn);
    }

    @Override
    public UidProfileIdx get(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.get(KEY_TABLE_NAME,queryExpress,conn);
    }


    private String getInsertSql(){
        String sql = "INSERT INTO "+KEY_TABLE_NAME+"(uid,uno,createtime,status,profileid,profilekey) VALUES(?,?,?,?,?,?)";
//        if(logger.isDebugEnabled()){
//            logger.debug("uid idx insert sql"+sql);
//        }
        return sql;
    }
}
